package com.example.datarest.multithreading;

import com.example.datarest.multithreading.processor.DemoTask;
import com.example.datarest.multithreading.processor.DemoTaskProcessor;
import com.example.datarest.multithreading.processor.DemoTaskProcessorFactory;
import com.example.datarest.multithreading.processor.ProcessorTypeEnum;
import com.example.datarest.multithreading.processor.TimeoutProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class DemoMultiThreadingServiceEngine {

    private static ExecutorService realTimeRequestService;
    private ScheduledExecutorService timeoutMonitorService;

    private Timer threadPoolMonitorTimer;
    private TimerTask threadPoolMonitorTask;

    @Value("${worker.count:3}")
    private Integer poolSize; // Worker threads number

    @Value("${queue.size:5}")
    private Integer queueSize;

    private static AtomicBoolean shutdownBoolean = new AtomicBoolean( false );

    // Simulate a list of tasks to run
    private static List<DemoTaskProcessor> demoTaskProcessorList = new ArrayList<>();

    public void init() {
        // Configure this in application.properties as required for Prod code

        // Number of tasks executed by threads and waiting on queue is bounded by: poolSize + queueSize
        if ( realTimeRequestService == null ) {
            realTimeRequestService = new BlockingExecutor(poolSize, queueSize);
        }

        if ( timeoutMonitorService == null ) {
            log.info("Initializing timeoutMonitorService....");
            timeoutMonitorService = Executors.newScheduledThreadPool(1);
        }
    }

    public void monitorThreadPool() {
        threadPoolMonitorTask = new TimerTask() {
            public void run() {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)realTimeRequestService;
                System.out.println();
                int activeCount = threadPoolExecutor.getActiveCount();
                long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
                System.out.println("Active threads running tasks: " + activeCount);
                System.out.println("Tasks completed: " + completedTaskCount);
                System.out.println("Tasks waiting in queue: " + threadPoolExecutor.getQueue().size());
                System.out.println("Unprocessed tasks outside queue: " + demoTaskProcessorList.size());
                System.out.println();
            }
        };
        threadPoolMonitorTimer = new Timer("threadPoolMonitorTimer");

        long delay = 1000L;
        threadPoolMonitorTimer.scheduleAtFixedRate(threadPoolMonitorTask, 0, 3*delay);
    }

    public void cancelMonitorThreadPool() {
        threadPoolMonitorTask.cancel();
        threadPoolMonitorTimer.cancel();
    }

    public void run() {
        init();

        System.out.println("Entering DemoMultiThreadingServiceEngine.run()...");
        int TIMEOUT_IN_SECONDS = 20;
        while ( !shutdownBoolean.get() ) {

            // In real world, actual taskProcessor should be returned at runtime using Factory pattern with
            // something like below where demoTask would be retrieved from a database or a JMS queue
            //      DemoTaskProcessor taskProcessor = DemoTaskProcessorFactory.createProcessor( demoTask );

            while ( demoTaskProcessorList.size() > 0 ) {
                DemoTaskProcessor demoTaskProcessor = demoTaskProcessorList.remove(0);

                // This line will block if queue size is full to throttle processing rate for better throughput.
                Future<DemoTask> future = realTimeRequestService.submit( demoTaskProcessor );

                DemoTask demoTask = demoTaskProcessor.getDemoTask();
                Future timeoutProcessFuture = timeoutMonitorService.schedule(new TimeoutProcessor(future, demoTask),
                        TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
                demoTaskProcessor.setTimeoutFuture(timeoutProcessFuture);
            }

            try {
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Exiting DemoMultiThreadingServiceEngine.run()...");
    }

    public void restart() {
        shutdownBoolean.set( false );
        run();
    }

    public void shutdown() {
        shutdownBoolean.set( true );
    }

    public static void mockData(int numberOfTasks) {
        for ( int i=0; i < numberOfTasks; i++ ) {
            DemoTask demoTask = new DemoTask();
            demoTask.setTaskId( Integer.toString(i) );
            DemoTaskProcessor demoTaskProcessor = getRandomDemoTaskProcessor( demoTask );
            demoTaskProcessorList.add( demoTaskProcessor );
        }
    }

    private static DemoTaskProcessor getRandomDemoTaskProcessor(DemoTask demoTask) {
        int lowerBoundIndex = 0;
        int upperBoundIndex = 2;
        Random random = new Random();
        int randomIndex = random.nextInt(upperBoundIndex - lowerBoundIndex + 1) + lowerBoundIndex;

        ProcessorTypeEnum processorTypeEnumArray[] = ProcessorTypeEnum.values();
        return DemoTaskProcessorFactory.createProcessor(
           processorTypeEnumArray[randomIndex], demoTask );
    }

    /*
    public static void main(String args[]) throws Exception {
        DemoMultiThreadingServiceEngine demoServiceEngine = new
                DemoMultiThreadingServiceEngine();
        mockData( 50 );
        demoServiceEngine.run();
    }*/
}

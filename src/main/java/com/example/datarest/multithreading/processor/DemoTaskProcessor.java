package com.example.datarest.multithreading.processor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
public abstract class DemoTaskProcessor<T> implements Callable<DemoTask> {

    @Getter @Setter
    private DemoTask demoTask;

    private Future<?> timeoutFuture;

     public DemoTaskProcessor(DemoTask demoTask) {
       this.demoTask = demoTask;
     }

    public DemoTask getDemoTask () {
        return demoTask;
    }

    public void setTimeoutFuture(Future<?> f) {
        this.timeoutFuture = f;
    }

    public DemoTask call() {
       // Do work
        try {
            Instant start = Instant.now();
            log.info( this.getClass().getName() + " with ID "  + getDemoTask().getTaskId() + " starts processing...");

            hardlyWorking();
            process();
            logPerformance( start );

        } catch (Exception ex) {
            log.error("Exception happened: ", ex);
        } finally {
            // clean up, logging...
        }
       return demoTask;
    }

    private void logPerformance(Instant start) {
        // Time spent processing task
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        log.info(this.getClass().getName() + " with ID " + getDemoTask().getTaskId()
           + " finishes processing after " + timeElapsed.getSeconds() + " sec");
    }

    // Simulate work
    private void hardlyWorking() {
        // Sleep random seconds between min & max to simulate processing...
        int min = 1;
        int max = 5;
        Random random = new Random();
        int sleepSeconds = random.nextInt(max - min + 1) + min;
        try {
            // Simulate some work...
            Thread.sleep( sleepSeconds * 1000L );
        } catch (Exception ex) {
        }
    }


    public abstract void process() throws Exception;

}

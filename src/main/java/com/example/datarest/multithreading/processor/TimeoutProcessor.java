package com.example.datarest.multithreading.processor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

@Slf4j
public class TimeoutProcessor implements Runnable {

    Future<DemoTask> handle;

    DemoTask demoTask;

    public TimeoutProcessor(Future<DemoTask> handle, DemoTask demoTask) {
        this.handle = handle;
        this.demoTask = demoTask;
    }

    @Override
    public void run() {

        // If processing is taking too long & still not finished then do something here
        if (!handle.isDone()) {
            String taskId = demoTask.getTaskId();

            log.warn("Timeout happens for DemoTask {}.", taskId);
            handle.cancel(true); // Cancel execution of demoTask
        }
    }
}

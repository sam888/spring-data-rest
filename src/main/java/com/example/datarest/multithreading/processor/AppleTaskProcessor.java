package com.example.datarest.multithreading.processor;

import com.example.datarest.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppleTaskProcessor extends DemoTaskProcessor {

    public AppleTaskProcessor(DemoTask demoTask) {
        super(demoTask);
    }

    @Override
    @LogExecutionTime // This will not work as thread doesn't seem to have access to AOP proxy
    public void process() throws Exception {

    }
}

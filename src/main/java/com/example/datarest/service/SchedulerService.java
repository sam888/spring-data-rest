package com.example.datarest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

    // Uncomment the line @Scheduled(...) below to run polling() once every ${polling.fixedDelay.in.milliseconds}
    // ms where polling.fixedDelay.in.milliseconds is configured in application.properties file. See
    // See https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html
    // for more details.
    //
    // @Scheduled(fixedDelayString = "${polling.fixedDelay.in.milliseconds}", initialDelay = 5000)
    public void polling() {
        process();
    }

    private void process() {
        // System.out.println("Processing/polling/querying data periodically...");
        log.info("Processing/polling/querying data periodically...");
    }


}

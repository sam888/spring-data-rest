package com.example.datarest.service;

import com.example.datarest.aop.SkipExecution;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

    @Getter @Setter
    private Boolean disablePolling = false;

    // Uncomment the line @Scheduled(...) below to run polling() once every ${polling.fixedDelay.in.milliseconds}
    // ms where polling.fixedDelay.in.milliseconds is configured in application.properties file. See
    // See https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html
    // for more details.
    //
    @Scheduled(fixedDelayString = "${polling.fixedDelay.in.milliseconds}", initialDelay = 5000)
    @SkipExecution(skipFlag = "disablePolling")
    public void polling() {
        process();
    }

    private void process() {
        // System.out.println("Processing/polling/querying data periodically...");
        log.info("Processing/polling/querying data periodically...");
    }

    public void stopPolling(boolean disablingPolling) {
      this.disablePolling = disablingPolling;
    }


}

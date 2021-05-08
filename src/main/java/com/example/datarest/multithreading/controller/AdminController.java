package com.example.datarest.multithreading.controller;

import com.example.datarest.multithreading.DemoMultiThreadingServiceEngine;
import com.example.datarest.aop.LogExecutionTime;
import com.example.datarest.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequestMapping("/admin")
@RestController
@Slf4j
public class AdminController {

   @Autowired
   private DemoMultiThreadingServiceEngine demoMultiThreadingServiceEngine;

   @Autowired
   private SchedulerService schedulerService;


   @LogExecutionTime
   @RequestMapping("/start")
   public ResponseEntity<String> start() throws Exception {
      // Without CompletableFuture.runAsync(..), code will never get to ResponseEntity.accepted()...
      CompletableFuture.runAsync(() -> {
         demoMultiThreadingServiceEngine.restart();
      });
      return ResponseEntity.accepted().body("DemoMultiThreadingServiceEngine started... " );
   }

   @LogExecutionTime
   @RequestMapping("/stop")
   public ResponseEntity<String> stop() throws Exception {
      demoMultiThreadingServiceEngine.shutdown();
      return ResponseEntity.accepted().body("DemoMultiThreadingServiceEngine stopped... " );
   }

   @LogExecutionTime
   @RequestMapping("/monitor")
   public ResponseEntity<String> monitor(@RequestParam(value="enable") String enable) {
      if ( "t".equalsIgnoreCase( enable ) ) {
         demoMultiThreadingServiceEngine.monitorThreadPool();
      } else if ( "f".equalsIgnoreCase( enable ) ) {
         demoMultiThreadingServiceEngine.cancelMonitorThreadPool();
      }

      return ResponseEntity.accepted().body("Timer enabled: " + enable );
   }

   @LogExecutionTime
   @RequestMapping("/mock")
   public ResponseEntity<String> mockTestData(@RequestParam(value="testData") String number) {

      int testDataNumber = 0;
      try {
         testDataNumber = Integer.parseInt( number );
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      DemoMultiThreadingServiceEngine.mockData( testDataNumber );

      return ResponseEntity.accepted().body("Number of test data simulated: " + testDataNumber );
   }

   @RequestMapping("/polling")
   public ResponseEntity<String> poll(@RequestParam(value="disablePolling") String disablePolling) {
      if ( "t".equalsIgnoreCase( disablePolling ) || "f".equalsIgnoreCase( disablePolling )) {
         schedulerService.stopPolling( BooleanUtils.toBoolean( disablePolling ) );
      }

      return ResponseEntity.accepted().body("Polling disabled: " + BooleanUtils.toBoolean( disablePolling )  );
   }

}

package com.example.datarest.multithreading.processor;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Constructor;

@Slf4j
public class DemoTaskProcessorFactory {

   public static DemoTaskProcessor createProcessor(ProcessorTypeEnum processorTypeEnum, DemoTask demoTask) {

      Class<?> clazz = processorTypeEnum.getClassValue();
      if (clazz == null) {
         log.error("Unknown processor type: {}", processorTypeEnum);
         throw new RuntimeException("Unknown processor type: " + processorTypeEnum);
      }

      try {
         Constructor<?> cons = clazz.getConstructor( DemoTask.class );
         return (DemoTaskProcessor)cons.newInstance( demoTask );
      } catch (Exception ex) {
         log.error("Error in creating DemoTaskProcessor with {}", demoTask);
         throw new RuntimeException(ex);
      }
   }
}

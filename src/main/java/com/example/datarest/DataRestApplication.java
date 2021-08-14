package com.example.datarest;

import com.example.datarest.multithreading.DemoMultiThreadingServiceEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
@EnableScheduling
public class DataRestApplication {

   public static void main(String[] args) {
      ApplicationContext context = SpringApplication.run(DataRestApplication.class, args);

      // Using @Autowired here for DemoMultiThreadingServiceEngine won't work. Workaround is using
      // ApplicationContext after all spring beans have been loaded.
      DemoMultiThreadingServiceEngine demoMultiThreadingServiceEngine =
              (DemoMultiThreadingServiceEngine)context.getBean("demoMultiThreadingServiceEngine");
      demoMultiThreadingServiceEngine.run();
   }

   @Value("${rest.client.timeout:30000}")
   private Integer timeout;

   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplateBuilder().setConnectTimeout( Duration.ofMillis(timeout) ).
         build();
   }
}

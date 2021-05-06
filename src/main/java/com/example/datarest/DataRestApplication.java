package com.example.datarest;

import com.example.datarest.multithreading.DemoMultiThreadingServiceEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

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
}

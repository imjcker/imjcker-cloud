package com.imjcker.spring.cloud.service.mail;

import com.imjcker.spring.cloud.service.mail.queue.Queue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCloudServiceMailApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudServiceMailApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Queue.getInstance().start();
	}
}

package com.imjcker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@SpringBootApplication
public class ImjckerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ImjckerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            for (String arg : args) {
                log.info("start argument: {}", arg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

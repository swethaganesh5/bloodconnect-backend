package com.bloodconnect.bloodconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure
        .SpringBootApplication;
import org.springframework.scheduling.annotation
        .EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BloodconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            BloodconnectApplication.class, args);
    }
}
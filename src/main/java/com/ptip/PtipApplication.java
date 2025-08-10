package com.ptip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PtipApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtipApplication.class, args);
    }

}

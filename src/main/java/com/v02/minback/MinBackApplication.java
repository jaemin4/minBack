package com.v02.minback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MinBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinBackApplication.class, args);
    }

}

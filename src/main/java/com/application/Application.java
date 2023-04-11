package com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        BootLoader.bootApplicationProperties();
        SpringApplication.run(Application.class, args);

    }

}
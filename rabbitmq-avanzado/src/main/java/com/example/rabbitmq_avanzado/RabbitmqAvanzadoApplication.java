package com.example.rabbitmq_avanzado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitmqAvanzadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqAvanzadoApplication.class, args);
    }

}
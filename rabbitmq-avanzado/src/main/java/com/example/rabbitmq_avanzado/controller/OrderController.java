package com.example.rabbitmq_avanzado.controller;

import com.example.rabbitmq_avanzado.producer.OrderProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProducer orderProducer;

    @PostMapping("/send")
    public ResponseEntity<String> sendOrder(
            @RequestParam String message) {

        orderProducer.sendOrder(message);

        return ResponseEntity.ok(
                "Mensaje enviado: " + message
        );
    }
}

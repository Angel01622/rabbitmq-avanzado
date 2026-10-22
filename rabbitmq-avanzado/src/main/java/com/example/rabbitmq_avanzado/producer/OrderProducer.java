package com.example.rabbitmq_avanzado.producer;

import com.example.rabbitmq_avanzado.config.RabbitTopologyConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrder(String message) {

        rabbitTemplate.convertAndSend(
                RabbitTopologyConfig.ORDERS_EXCHANGE,
                RabbitTopologyConfig.ORDERS_ROUTING_KEY,
                message
        );

        log.info("Mensaje enviado: {}", message);
    }
}
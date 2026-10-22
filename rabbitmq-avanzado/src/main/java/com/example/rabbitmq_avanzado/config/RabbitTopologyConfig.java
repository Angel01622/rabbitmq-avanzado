package com.example.rabbitmq_avanzado.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    public static final String ORDERS_QUEUE = "orders.queue";
    public static final String ORDERS_EXCHANGE = "orders.exchange";
    public static final String ORDERS_ROUTING_KEY = "orders.key";

    public static final String ORDERS_DLQ = "orders.dlq";
    public static final String ORDERS_DLX = "orders.dlx";
    public static final String ORDERS_DLQ_ROUTING_KEY = "orders.error";

    @Bean
    public Queue ordersQueue() {
        return QueueBuilder
                .durable(ORDERS_QUEUE)
                .deadLetterExchange(ORDERS_DLX)
                .deadLetterRoutingKey(ORDERS_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(ORDERS_EXCHANGE);
    }

    @Bean
    public Binding ordersBinding() {
        return BindingBuilder
                .bind(ordersQueue())
                .to(ordersExchange())
                .with(ORDERS_ROUTING_KEY);
    }

    @Bean
    public Queue ordersDeadLetterQueue() {
        return QueueBuilder
                .durable(ORDERS_DLQ)
                .build();
    }

    @Bean
    public DirectExchange ordersDeadLetterExchange() {
        return new DirectExchange(ORDERS_DLX);
    }

    @Bean
    public Binding ordersDeadLetterBinding() {
        return BindingBuilder
                .bind(ordersDeadLetterQueue())
                .to(ordersDeadLetterExchange())
                .with(ORDERS_DLQ_ROUTING_KEY);
    }
}
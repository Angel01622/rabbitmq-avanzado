package com.example.rabbitmq_avanzado.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitListenerConfig {

    @Bean(name = "manualAckListenerFactory")
    public SimpleRabbitListenerContainerFactory manualAckListenerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);

        return factory;
    }
}

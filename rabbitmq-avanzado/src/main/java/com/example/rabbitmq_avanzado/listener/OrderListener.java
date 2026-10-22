package com.example.rabbitmq_avanzado.listener;

import com.example.rabbitmq_avanzado.config.RabbitTopologyConfig;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderListener {

    @RabbitListener(
            queues = RabbitTopologyConfig.ORDERS_QUEUE,
            containerFactory = "manualAckListenerFactory"
    )
    public void processOrder(
            String payload,
            Message message,
            Channel channel) throws Exception {

        long deliveryTag =
                message.getMessageProperties().getDeliveryTag();

        try {

            log.info("Mensaje recibido: {}", payload);

            if (payload.toUpperCase().contains("ERROR")) {
                throw new RuntimeException(
                        "Error simulado procesando la orden"
                );
            }

            log.info(
                    "Orden procesada correctamente: {}",
                    payload
            );

            channel.basicAck(deliveryTag, false);

            log.info("ACK enviado correctamente");

        } catch (Exception e) {

            log.error(
                    "Error procesando mensaje: {}",
                    e.getMessage()
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );

            log.warn(
                    "NACK enviado. Mensaje dirigido a DLQ"
            );
        }
    }
}
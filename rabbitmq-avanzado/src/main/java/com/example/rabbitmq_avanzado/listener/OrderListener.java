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

    private static final int MAX_ATTEMPTS = 3;
    private static final long BASE_RETRY_DELAY_MS = 1000;

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

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            try {

                log.info(
                        "Procesando mensaje. Intento {}/{}: {}",
                        attempt,
                        MAX_ATTEMPTS,
                        payload
                );

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

                return;

            } catch (Exception e) {

                log.error(
                        "Falló intento {}/{}: {}",
                        attempt,
                        MAX_ATTEMPTS,
                        e.getMessage()
                );

                if (attempt < MAX_ATTEMPTS) {

                    long delay =
                            BASE_RETRY_DELAY_MS * attempt;

                    log.warn(
                            "Reintentando mensaje en {} ms...",
                            delay
                    );

                    try {
                        Thread.sleep(delay);

                    } catch (InterruptedException interruptedException) {

                        Thread.currentThread().interrupt();

                        log.error(
                                "Reintento interrumpido"
                        );

                        break;
                    }
                }
            }
        }

        channel.basicNack(
                deliveryTag,
                false,
                false
        );

        log.warn(
                "Se agotaron los reintentos. NACK enviado y mensaje dirigido a DLQ: {}",
                payload
        );
    }
}
package com.example.rabbitmq_avanzado.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rabbitmq_avanzado.service.RabbitResourceManager;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rabbitmq")
@RequiredArgsConstructor
public class RabbitMQController {

    private final RabbitResourceManager resourceManager;

    @PostMapping("/queues")
    public ResponseEntity<String> createQueue(
            @RequestParam String queueName) {

        if (isInvalid(queueName)) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: el nombre de la cola no puede estar vacío");
        }

        resourceManager.createQueue(queueName);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Cola '" + queueName + "' creada exitosamente");
    }

    @PostMapping("/exchanges")
    public ResponseEntity<String> createExchange(
            @RequestParam String exchangeName) {

        if (isInvalid(exchangeName)) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: el nombre del exchange no puede estar vacío");
        }

        resourceManager.createExchange(exchangeName);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Exchange '" + exchangeName + "' creado exitosamente");
    }

    @PostMapping("/bindings")
    public ResponseEntity<String> createBinding(
            @RequestParam String queueName,
            @RequestParam String exchangeName,
            @RequestParam String routingKey) {

        if (isInvalid(queueName)
                || isInvalid(exchangeName)
                || isInvalid(routingKey)) {

            return ResponseEntity
                    .badRequest()
                    .body("Error: queueName, exchangeName y routingKey son obligatorios");
        }

        resourceManager.createBinding(
                queueName,
                exchangeName,
                routingKey
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        "Binding creado: "
                                + queueName
                                + " <-> "
                                + exchangeName
                );
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<String> deleteBinding(
            @RequestParam String queueName,
            @RequestParam String exchangeName,
            @RequestParam String routingKey) {

        if (isInvalid(queueName)
                || isInvalid(exchangeName)
                || isInvalid(routingKey)) {

            return ResponseEntity
                    .badRequest()
                    .body("Error: queueName, exchangeName y routingKey son obligatorios");
        }

        resourceManager.deleteBinding(
                queueName,
                exchangeName,
                routingKey
        );

        return ResponseEntity.ok(
                "Binding eliminado: "
                        + queueName
                        + " <-> "
                        + exchangeName
        );
    }

    @GetMapping("/queues/{queueName}")
    public ResponseEntity<?> getQueueInfo(
            @PathVariable String queueName) {

        var info = resourceManager.getQueueInfo(queueName);

        return info != null
                ? ResponseEntity.ok(info)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/queues/{queueName}")
    public ResponseEntity<String> deleteQueue(
            @PathVariable String queueName) {

        resourceManager.deleteQueue(queueName);

        return ResponseEntity.ok(
                "Cola '" + queueName + "' eliminada"
        );
    }

    @DeleteMapping("/exchanges/{exchangeName}")
    public ResponseEntity<String> deleteExchange(
            @PathVariable String exchangeName) {

        resourceManager.deleteExchange(exchangeName);

        return ResponseEntity.ok(
                "Exchange '" + exchangeName + "' eliminado"
        );
    }

    @PostMapping("/queues/{queueName}/purge")
    public ResponseEntity<String> purgeQueue(
            @PathVariable String queueName) {

        resourceManager.purgeQueue(queueName);

        return ResponseEntity.ok(
                "Cola '" + queueName + "' purgada"
        );
    }

    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}
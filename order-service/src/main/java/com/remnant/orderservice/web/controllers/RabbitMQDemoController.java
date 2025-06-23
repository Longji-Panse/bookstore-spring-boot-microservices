package com.remnant.orderservice.web.controllers;

import com.remnant.orderservice.ApplicationProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQDemoController {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;


    public RabbitMQDemoController(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MyMessage message) {
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), message.routingKey(),message.payload());
    }
}

record MyMessage(String routingKey, MyPayload payload) {}
record MyPayload(String content) {}

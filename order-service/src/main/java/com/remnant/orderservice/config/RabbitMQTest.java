package com.remnant.orderservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQTest {
    @Autowired
    private ConnectionFactory connectionFactory;

    @PostConstruct
    public void testConnection() {
        try (Connection connection = connectionFactory.createConnection()) {
            System.out.println("Successfully connected to RabbitMQ!");
        } catch (Exception e) {
            System.err.println("Failed to connect to RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

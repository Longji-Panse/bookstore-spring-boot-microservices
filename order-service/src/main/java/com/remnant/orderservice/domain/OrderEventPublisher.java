package com.remnant.orderservice.domain;

import com.remnant.orderservice.ApplicationProperties;
import com.remnant.orderservice.domain.models.OrderCancelledEvent;
import com.remnant.orderservice.domain.models.OrderCreatedEvent;
import com.remnant.orderservice.domain.models.OrderDeliveredEvent;
import com.remnant.orderservice.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    public void publish(OrderCreatedEvent event) {
        try {
            logger.info("Publishing OrderCreatedEvent to queue: {}, event: {}", properties.newOrdersQueue(), event);
            send(properties.newOrdersQueue(), event);
            logger.info("Successfully published OrderCreatedEvent for order: {}", event.orderNumber());
        } catch (Exception e) {
            logger.error("Failed to publish OrderCreatedEvent for order: {}. Error: {}", event.orderNumber(), e.getMessage(), e);
            throw e; // Or handle differently, e.g., move to error queue
        }
    }
    public void publish(OrderDeliveredEvent event) {
        try {
            logger.info("Publishing OrderDeliveredEvent to queue: {}, event: {}", properties.deliveredOrdersQueue(), event);
            send(properties.deliveredOrdersQueue(), event);
            logger.info("Successfully published OrderDeliveredEvent for order: {}", event.orderNumber());
        } catch (Exception e) {
            logger.error("Failed to publish OrderDeliveredEvent for order: {}. Error: {}", event.orderNumber(), e.getMessage(), e);
            throw e; // Or handle differently, e.g., move to error queue
        }
    }
    public void publish(OrderCancelledEvent event) {
        try {
            logger.info("Publishing OrderCancelledEvent to queue: {}, event: {}", properties.cancelledOrdersQueue(), event);
            send(properties.cancelledOrdersQueue(), event);
            logger.info("Successfully published OrderCancelledEvent for order: {}", event.orderNumber());
        } catch (Exception e) {
            logger.error("Failed to publish OrderCancelledEvent for order: {}. Error: {}", event.orderNumber(), e.getMessage(), e);
            throw e; // Or handle differently, e.g., move to error queue
        }
    }

    public void publish(OrderErrorEvent event) {
        try {
            logger.info("Publishing OrderErrorEvent to queue: {}, event: {}", properties.errorOrdersQueue(), event);
            send(properties.errorOrdersQueue(), event);
            logger.info("Successfully published OrderErrorEvent for order: {}", event.orderNumber());
        } catch (Exception e) {
            logger.error("Failed to publish OrderErrorEvent for order: {}. Error: {}", event.orderNumber(), e.getMessage(), e);
            throw e; // Or handle differently, e.g., move to error queue
        }
    }

    private void send(String routingKey, Object payload){
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(),routingKey, payload);
    }


}

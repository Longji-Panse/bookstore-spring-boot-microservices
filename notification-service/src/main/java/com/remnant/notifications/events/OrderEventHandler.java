package com.remnant.notifications.events;

import com.remnant.notifications.domain.NotificationService;
import com.remnant.notifications.domain.OrderEventEntity;
import com.remnant.notifications.domain.OrderEventRepository;
import com.remnant.notifications.domain.models.OrderCancelledEvent;
import com.remnant.notifications.domain.models.OrderCreatedEvent;
import com.remnant.notifications.domain.models.OrderDeliveredEvent;
import com.remnant.notifications.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {
    private final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);
    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("Order Created Event: {}", orderCreatedEvent);
        if (orderEventRepository.existsByEventId(orderCreatedEvent.eventId())) {
            log.warn("Order Created Event already exists for eventId: {}", orderCreatedEvent.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(orderCreatedEvent);
        OrderEventEntity orderEvent = new OrderEventEntity(orderCreatedEvent.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleDeliveredEvent(OrderDeliveredEvent orderDeliveredEvent) {
        log.info("Order Delivered Event: {}", orderDeliveredEvent);
        if (orderEventRepository.existsByEventId(orderDeliveredEvent.eventId())) {
            log.warn("Order Delivered Event already exists for eventId: {}", orderDeliveredEvent.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(orderDeliveredEvent);
        OrderEventEntity orderEvent = new OrderEventEntity(orderDeliveredEvent.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleCancelledEvent(OrderCancelledEvent orderCancelledEvent) {
        log.info("Order Cancelled Event: {}", orderCancelledEvent);
        if (orderEventRepository.existsByEventId(orderCancelledEvent.eventId())) {
            log.warn("Order Cancelled Event already exists for eventId: {}", orderCancelledEvent.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(orderCancelledEvent);
        OrderEventEntity orderEvent = new OrderEventEntity(orderCancelledEvent.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleErrorEvent(OrderErrorEvent orderErrorEvent) {
        log.info("Order Error Event: {}", orderErrorEvent);
        if (orderEventRepository.existsByEventId(orderErrorEvent.eventId())) {
            log.warn("Order Error Event already exists for eventId: {}", orderErrorEvent.eventId());
            return;
        }
        notificationService.sendOrderErrorNotification(orderErrorEvent);
        OrderEventEntity orderEvent = new OrderEventEntity(orderErrorEvent.eventId());
        orderEventRepository.save(orderEvent);
    }
}

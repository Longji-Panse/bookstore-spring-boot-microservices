package com.remnant.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remnant.orderservice.domain.models.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderEventService {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;


    public void save(OrderCreatedEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayLoad(event));

       orderEventRepository.save(orderEvent);
    }

    public void save(OrderErrorEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayLoad(event));

        orderEventRepository.save(orderEvent);
    }

    public void save(OrderCancelledEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayLoad(event));

        orderEventRepository.save(orderEvent);

    }

    public void save(OrderDeliveredEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayLoad(event));

       orderEventRepository.save(orderEvent);

    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        logger.info("Found {} orders to be published", events.size());
        for (OrderEventEntity event : events) {
            publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayLoad(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                logger.info("Published OrderCreatedEvent for order: {}", event.getOrderNumber());
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent = fromJsonPayLoad(event.getPayload(), OrderDeliveredEvent.class);
                logger.info("Deserialized OrderDeliveredEvent: {}", orderDeliveredEvent);
                orderEventPublisher.publish(orderDeliveredEvent);
                logger.info("Published OrderDeliveredEvent for order: {}", event.getOrderNumber());
                break;
            case ORDER_CANCELLED:
                OrderCancelledEvent orderCancelledEvent = fromJsonPayLoad(event.getPayload(), OrderCancelledEvent.class);
                logger.info("Deserialized OrderCancelledEvent: {}", orderCancelledEvent);
                orderEventPublisher.publish(orderCancelledEvent);
                logger.info("Published OrderCancelledEvent for order: {}", event.getOrderNumber());
                break;
            case ORDER_PROCESSING_FAILED:
                OrderErrorEvent orderErrorEvent = fromJsonPayLoad(event.getPayload(), OrderErrorEvent.class);
                logger.info("Deserialized OrderErrorEvent: {}", orderErrorEvent);
                orderEventPublisher.publish(orderErrorEvent);
                logger.info("Published OrderErrorEvent for order: {}", event.getOrderNumber());
                break;

            default:
                logger.warn("Unsupported OrderEventType: {}", eventType);
        }
    }



    private String toJsonPayLoad(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayLoad(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

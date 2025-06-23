package com.remnant.orderservice.domain;

import com.remnant.orderservice.domain.models.CreateOrderRequest;
import com.remnant.orderservice.domain.models.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    public static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Transactional
    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(username);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Order created with orderNumber: {}", savedOrder.getOrderNumber());
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}

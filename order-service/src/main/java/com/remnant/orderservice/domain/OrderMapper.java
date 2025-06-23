package com.remnant.orderservice.domain;

import com.remnant.orderservice.domain.models.CreateOrderRequest;
import com.remnant.orderservice.domain.models.OrderItem;
import com.remnant.orderservice.domain.models.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OrderMapper {

    static OrderEntity convertToEntity(CreateOrderRequest request) {
        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(request.customer());
        newOrder.setDeliveryAddress(request.deliveryAddress());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for(OrderItem item : request.items()){
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCode(item.code());
            orderItem.setQuantity(item.quantity());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setOrder(newOrder);

            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }
}

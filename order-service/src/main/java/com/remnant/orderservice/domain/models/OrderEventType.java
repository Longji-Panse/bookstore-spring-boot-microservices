package com.remnant.orderservice.domain.models;

public enum OrderEventType {
    ORDER_CREATED,
    ORDER_CANCELLED,
    ORDER_DELIVERED,
    ORDER_PROCESSING_FAILED,
    ORDER_ERROR
}

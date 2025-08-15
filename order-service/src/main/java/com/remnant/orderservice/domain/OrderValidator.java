package com.remnant.orderservice.domain;

import com.remnant.orderservice.clients.catalog.ProductDto;
import com.remnant.orderservice.clients.catalog.ProductServiceClient;
import com.remnant.orderservice.domain.models.CreateOrderRequest;
import com.remnant.orderservice.domain.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderValidator {
    private static final Logger logger = LoggerFactory.getLogger(OrderValidator.class);
    private final ProductServiceClient client;

    public OrderValidator(ProductServiceClient client) {
        this.client = client;
    }

    void validate(CreateOrderRequest orderRequest) {
        Set<OrderItem> items = orderRequest.items();
        for (OrderItem item : items) {
            ProductDto productDto = client.getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid product code: " + item.code()));
            if (item.price().compareTo(productDto.price()) != 0) {
                logger.error("Product price does not match: Actual price:{}, received price:{}", item.price(), productDto.price());
                throw new InvalidOrderException("Product price does not match!");
            }
        }
    }
}

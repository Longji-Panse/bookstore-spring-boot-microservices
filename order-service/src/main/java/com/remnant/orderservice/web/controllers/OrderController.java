package com.remnant.orderservice.web.controllers;

import com.remnant.orderservice.domain.models.CreateOrderRequest;
import com.remnant.orderservice.domain.models.CreateOrderResponse;
import com.remnant.orderservice.domain.OrderService;
import com.remnant.orderservice.domain.SecurityService;
import jakarta.validation.Valid;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        String userName = securityService.getLoggedInUser();
        log.info("Creating order for user {}", userName);
        return orderService.createOrder(userName, request);
    }
}

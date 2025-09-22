package com.remnant.orderservice.web.controllers;

import com.remnant.orderservice.domain.OrderNotFoundException;
import com.remnant.orderservice.domain.OrderService;
import com.remnant.orderservice.domain.SecurityService;
import com.remnant.orderservice.domain.models.CreateOrderRequest;
import com.remnant.orderservice.domain.models.CreateOrderResponse;
import com.remnant.orderservice.domain.models.OrderDTO;
import com.remnant.orderservice.domain.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
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
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = securityService.getLoggedInUser();
        log.info("Creating order for user {}", userName);
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String username = securityService.getLoggedInUser();
        log.info("Retrieving orders for user {}", username);
        return orderService.findOrdersByUserName(username);
    }

    @GetMapping(value = "/{orderNumber}")
    OrderDTO getOrder(@PathVariable("orderNumber") String orderNumber) {
        String username = securityService.getLoggedInUser();
        log.info("Retrieving order {} for user {}", orderNumber, username);
        return orderService
                .findUserOrder(username, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}

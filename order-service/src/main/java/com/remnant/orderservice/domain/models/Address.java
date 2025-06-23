package com.remnant.orderservice.domain.models;


import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "Delivery address is required") String deliveryAddress,
        @NotBlank(message = "Delivery country is required") String deliveryCountry) {}

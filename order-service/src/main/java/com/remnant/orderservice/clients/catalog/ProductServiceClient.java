package com.remnant.orderservice.clients.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);
    private final RestClient restClient;

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCOdeFallBack")
    public Optional<ProductDto> getProductByCode(String code){
        log.info("Fetching product by code: {}", code);

            var product = restClient
                    .get()
                    .uri("/api/products/{code}", code)
                    .retrieve()
                    .body(ProductDto.class);
            return Optional.ofNullable(product);
    }

    Optional<ProductDto> getProductByCOdeFallBack(String code, Throwable t){
        log.info("Could not fetch product by code: {}", code);
        return Optional.empty();
    }
}

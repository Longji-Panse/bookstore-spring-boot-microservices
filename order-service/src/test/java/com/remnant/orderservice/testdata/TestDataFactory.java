package com.remnant.orderservice.testdata;

import com.remnant.orderservice.domain.models.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestDataFactory {
    static final List<String> VALID_COUNTRIES = List.of("India", "Germany");
    static final Set<OrderItem> VALID_ORDER_ITEMS =
            Set.of(new OrderItem("P100", "Product 1", new BigDecimal("23.20"), 1));
    static final Set<OrderItem> INVALID_ORDER_ITEMS =
            Set.of(new OrderItem("ABCD", "Product 1", new BigDecimal("23.20"), 1));
}

package com.remnant.orderservice.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.remnant.orderservice.AbstractIT;
import com.remnant.orderservice.domain.models.OrderSummary;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
public class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrder() {
            mockGetByProductCode("P100", "product", new BigDecimal("25.50"));
            var payload =
                    """
                            {
                                "customer": {
                                    "name":"longji panse",
                                    "email": "longjipanse@gmail.com",
                                    "phone": "09166312527"
                                },
                                "deliveryAddress":{
                                    "deliveryAddress": "Rantya, Jos Plateau State",
                                    "deliveryCountry": "Nigeria"
                                },
                                "items":[
                                    {
                                        "code":"P100",
                                        "name": "product",
                                        "price": 25.50,
                                        "quantity":1
                                    }
                                ]
                            }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequest() {
            var payload =
                    """
                            {
                                "customer": {
                                    "name":"longji panse",
                                    "email": "longjipanse@gmail.com",
                                    "phone": "09166312527"
                                },
                                "deliveryAddress":{
                                    "deliveryAddress": "",
                                    "deliveryCountry": ""
                                },
                                "items":[
                                    {
                                        "code":"P100",
                                        "name": "product",
                                        "price": 25.50,
                                        "quantity":1
                                    }
                                ]
                            }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrderTests {
        @Test
        void shouldGetOrderSuccessfully() {
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}

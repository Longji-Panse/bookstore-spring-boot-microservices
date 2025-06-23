package com.remnant.orderservice.web.controllers;

import com.remnant.orderservice.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests{
        @Test
        void shouldCreateOrder(){
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
                                    },
                                    {
                                        "code":"P200",
                                        "name": "another product",
                                        "price": 105.80,
                                        "quantity":2
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
        void shouldReturnBadRequest(){
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
}

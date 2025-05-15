package com.remnant.catalogservice.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.remnant.catalogservice.AbstractIntegrationTest;
import com.remnant.catalogservice.domain.ProductEntity;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
public class ProductControllerTest extends AbstractIntegrationTest {

    @Test
    public void shouldReturnProducts() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements", is(40))
                .body("pageNumber", is(1))
                .body("totalPages", is(4))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    public void shouldGetProductByCode() {
        ProductEntity product = given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", "BOOK005")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .body()
                .as(ProductEntity.class);

        assertThat(product.getCode()).isEqualTo("BOOK005");
        assertThat(product.getName()).isEqualTo("JavaScript: The Definitive Guide");
        assertThat(product.getDescription()).isEqualTo("Deep dive into JavaScript.");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("32.10"));
    }

    @Test
    public void shouldReturnNotFoundWhenProductCodeNotExists() {
        String code = "invalid_product_code";
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", code)
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("title", is("Product Not Found"))
                .body("detail", is("Product with code " + code + " not found"));
    }
}

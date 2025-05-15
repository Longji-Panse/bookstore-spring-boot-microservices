package com.remnant.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {"spring.test.database.replace=none", "spring.datasource.url=jdbc:tc:postgresql:latest:///db"})
// @Import(TestcontainersConfiguration.class)
@Sql("/test-data.sql")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(40);
    }

    @Test
    public void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("BOOK005").orElseThrow();
        assertThat(product.getCode()).isEqualTo("BOOK005");
        assertThat(product.getName()).isEqualTo("JavaScript: The Definitive Guide");
        assertThat(product.getDescription()).isEqualTo("Deep dive into JavaScript.");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("32.10"));
    }

    @Test
    public void shouldReturnEmptyWhenProductCodeDoesNotExist() {
        assertThat(productRepository.findByCode("invalid_product_code")).isEmpty();
    }
}

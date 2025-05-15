package com.remnant.catalogservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_sequence")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Product Code is required")
    private String code;

    @Column(nullable = false)
    @NotEmpty(message = "Product Name is required")
    private String name;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    @NotEmpty(message = "Product Name is required")
    @DecimalMin("0.1")
    private BigDecimal price;
}

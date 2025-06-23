package com.remnant.orderservice.domain;

import com.remnant.orderservice.domain.models.Address;
import com.remnant.orderservice.domain.models.Customer;
import com.remnant.orderservice.domain.models.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "orders")
@Getter
@Setter

class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(name = "order_id_generator",sequenceName = "order_id_seq")
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    @Column(name = "username", nullable = false)
    private String userName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItemEntity> items;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
                    @AttributeOverride(name = "email", column = @Column(name = "customer_email")),
                    @AttributeOverride(name = "phone", column = @Column(name = "customer_phone"))
            })
    private Customer customer;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "deliveryAddress", column = @Column(name = "delivery_address")),
                    @AttributeOverride(name = "deliveryCountry", column = @Column(name = "delivery_country"))
            })
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}

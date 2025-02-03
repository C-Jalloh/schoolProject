package com.schoolProject.ecommerceApplication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    private BigDecimal amount;
    private String method;
    private String status;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    // Make this return a list of orders instead of only one Order because the annotation on it (@OneToMany) will return list of orders
    private List<Order> order;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}

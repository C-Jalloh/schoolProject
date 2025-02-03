package com.schoolProject.ecommerceApplication.repository;

import com.schoolProject.ecommerceApplication.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// You forgot to add this annotation
@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
}

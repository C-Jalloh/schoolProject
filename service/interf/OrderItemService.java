package com.schoolProject.ecommerceApplication.service.interf;

import com.schoolProject.ecommerceApplication.dto.OrderRequest;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderItemService {

    Response placeOrder(OrderRequest orderRequest);

    Response updateOrderItemStatus(Long orderItem, String status);

    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);
}

package com.schoolProject.ecommerceApplication.service.impl;

import com.schoolProject.ecommerceApplication.dto.OrderItemDto;
import com.schoolProject.ecommerceApplication.dto.OrderRequest;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.entity.Order;
import com.schoolProject.ecommerceApplication.entity.OrderItem;
import com.schoolProject.ecommerceApplication.entity.Product;
import com.schoolProject.ecommerceApplication.entity.User;
import com.schoolProject.ecommerceApplication.enums.OrderStatus;
import com.schoolProject.ecommerceApplication.exception.NotFoundException;
import com.schoolProject.ecommerceApplication.mapper.EntityDtoMapper;
import com.schoolProject.ecommerceApplication.repository.OrderItemRepo;
import com.schoolProject.ecommerceApplication.repository.OrderRepo;
import com.schoolProject.ecommerceApplication.repository.ProductRepo;
import com.schoolProject.ecommerceApplication.service.interf.OrderItemService;
import com.schoolProject.ecommerceApplication.service.interf.UserService;
import com.schoolProject.ecommerceApplication.specification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private  final EntityDtoMapper entityDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {
        User user = userService.getLoginUser();

        List<OrderItem>  orderItems = orderRequest.getItems().stream().map(orderItemRequest ->{
            Product product = productRepo.findById(orderItemRequest.getProductId())
                    .orElseThrow(()-> new NotFoundException("product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));
            orderItem.setStatus(OrderStatus.PENDING);
            orderItem.setUser(user);
            return orderItem;
        }).collect(Collectors.toList());

        BigDecimal totalPrice= orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0
                ? orderRequest.getTotalPrice(): orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        orderRepo.save(order);


        return Response.builder()
                .status(200)
                .message("order is successfully placed")
                .build();
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem1 = orderItemRepo.findById(orderItemId)
                .orElseThrow(()-> new NotFoundException("order not found"));
        orderItem1.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepo.save(orderItem1);


        return Response.builder()
                .status(200)
                .message("order status updated successfully")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);
        if (orderItemPage.isEmpty()){
            throw new NotFoundException("oder not found");
        }

        List<OrderItemDto> orderItemDtos = orderItemPage.getContent().stream().map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElement(orderItemPage.getTotalElements())
                .build();
    }
}

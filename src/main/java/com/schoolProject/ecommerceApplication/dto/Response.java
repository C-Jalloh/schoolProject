package com.schoolProject.ecommerceApplication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
// add all args constructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;
    private final LocalDateTime timeStamp = LocalDateTime.now();

    private String token;
    private String role;
    private String expirationTime;

    private int totalPage;
    private long totalElement;

    private AddressDto address;

    private UserDto user;
    private List<UserDto> userList;

    private CategoryDto category;
    private List<CategoryDto> categoryList;

    private ProductDto product;
    private List<ProductDto> productList;

    private OrderItemDto orderItem;
    private List<OrderItemDto> orderItemList;

    private OrderDto order;
    private List<OrderDto> OrderList;

    public Response() {

    }


    public static Builder builder() {
        return new Builder();
    }

    // Nested Builder class
    public static class Builder {
        private int status;
        private String message;
        private String token;
        private String role;
        private String expirationTime;
        private int totalPage;
        private long totalElement;
        private AddressDto address;
        private UserDto user;
        private List<UserDto> userList;
        private CategoryDto category;
        private List<CategoryDto> categoryList;
        private ProductDto product;
        private List<ProductDto> productList;
        private OrderItemDto orderItem;
        private List<OrderItemDto> orderItemList;
        private OrderDto order;
        private List<OrderDto> OrderList;

        // Builder setter methods
        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder expirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public Builder totalPage(int totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public Builder totalElement(long totalElement) {
            this.totalElement = totalElement;
            return this;
        }

        public Builder address(AddressDto address) {
            this.address = address;
            return this;
        }

        public Builder user(UserDto user) {
            this.user = user;
            return this;
        }

        public Builder userList(List<UserDto> userList) {
            this.userList = userList;
            return this;
        }

        public Builder category(CategoryDto category) {
            this.category = category;
            return this;
        }

        public Builder categoryList(List<CategoryDto> categoryList) {
            this.categoryList = categoryList;
            return this;
        }

        public Builder product(ProductDto product) {
            this.product = product;
            return this;
        }

        public Builder productList(List<ProductDto> productList) {
            this.productList = productList;
            return this;
        }

        public Builder orderItem(OrderItemDto orderItem) {
            this.orderItem = orderItem;
            return this;
        }

        public Builder orderItemList(List<OrderItemDto> orderItemList) {
            this.orderItemList = orderItemList;
            return this;
        }

        public Builder order(OrderDto order) {
            this.order = order;
            return this;
        }

        public Builder orderList(List<OrderDto> OrderList) {
            this.OrderList = OrderList;
            return this;
        }

        // Build method to return the final Response object
        public Response build() {
            Response response = new Response();
            response.status = this.status;
            response.message = this.message;
            response.token = this.token;
            response.role = this.role;
            response.expirationTime = this.expirationTime;
            response.totalPage = this.totalPage;
            response.totalElement = this.totalElement;
            response.address = this.address;
            response.user = this.user;
            response.userList = this.userList;
            response.category = this.category;
            response.categoryList = this.categoryList;
            response.product = this.product;
            response.productList = this.productList;
            response.orderItem = this.orderItem;
            response.orderItemList = this.orderItemList;
            response.order = this.order;
            response.OrderList = this.OrderList;

            // Automatically set the timestamp
//            Response.timeStamp = LocalDateTime.now();

            return response;
        }
    }


}


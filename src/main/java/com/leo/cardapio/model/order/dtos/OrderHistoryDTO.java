package com.leo.cardapio.model.order.dtos;

import com.leo.cardapio.model.order.Order;
import com.leo.cardapio.model.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderHistoryDTO(UUID orderId, LocalDateTime orderDate, OrderStatus status, Double totalAmount) {
    public OrderHistoryDTO(Order order) {
        this(order.getId(), order.getOrderDate(), order.getStatus(), order.getTotalAmount());
    }
}

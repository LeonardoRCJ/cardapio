package com.leo.cardapio.model.order.dtos;

import com.leo.cardapio.model.orderItem.dtos.OrderItemRequestDTO;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items, PaymentRequestDTO paymentData) {
}

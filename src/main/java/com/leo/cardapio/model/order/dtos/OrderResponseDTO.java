package com.leo.cardapio.model.order.dtos;

import java.util.UUID;

public record OrderResponseDTO(UUID orderId, String orderStatus, String paymentId) {
}

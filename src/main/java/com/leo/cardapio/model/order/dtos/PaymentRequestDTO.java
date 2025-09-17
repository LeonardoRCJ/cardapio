package com.leo.cardapio.model.order.dtos;

public record PaymentRequestDTO(
        String token,
        String issuerId,
        String paymentMethodId,
        Integer installments,
        String email
) {
}

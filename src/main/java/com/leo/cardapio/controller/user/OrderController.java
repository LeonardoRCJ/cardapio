package com.leo.cardapio.controller.user;

import com.leo.cardapio.model.order.dtos.OrderHistoryDTO;
import com.leo.cardapio.model.order.dtos.OrderRequestDTO;
import com.leo.cardapio.model.order.dtos.OrderResponseDTO;
import com.leo.cardapio.model.order.dtos.WebhookNotificationDTO;
import com.leo.cardapio.services.OrderService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO orderRequest,
            Authentication authentication
            ) throws MPException, MPApiException {
        String userEmail = authentication.getName();
        OrderResponseDTO response = orderService.createOrderAndPayment(orderRequest, userEmail);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payments/webhook")
    public ResponseEntity<Void> paymentWebhook(@RequestBody WebhookNotificationDTO notification) {
        // Apenas para debugging, para ver as notificações a chegar no console.
        System.out.println("Webhook recebido: " + notification);

        orderService.handlePaymentWebhook(notification);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderHistoryDTO>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrderHistoryDTO> orderHistory = orderService.getOrderHistoryForUser(userEmail);
        return ResponseEntity.ok(orderHistory);
    }
}

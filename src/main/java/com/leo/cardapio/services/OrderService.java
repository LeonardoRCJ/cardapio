package com.leo.cardapio.services;

import com.leo.cardapio.model.food.Food;
import com.leo.cardapio.model.food.exceptions.FoodNotFoundException;
import com.leo.cardapio.model.order.Order;
import com.leo.cardapio.model.order.OrderStatus;
import com.leo.cardapio.model.order.dtos.OrderRequestDTO;
import com.leo.cardapio.model.order.dtos.OrderResponseDTO;
import com.leo.cardapio.model.order.dtos.PaymentRequestDTO;
import com.leo.cardapio.model.order.exceptions.PaymentException;
import com.leo.cardapio.model.orderItem.OrderItem;
import com.leo.cardapio.model.user.User;
import com.leo.cardapio.model.user.exceptions.UserNotFoundException;
import com.leo.cardapio.repositories.FoodRepository;
import com.leo.cardapio.repositories.OrderRepository;
import com.leo.cardapio.repositories.UserRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, FoodRepository foodRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponseDTO createOrderAndPayment(OrderRequestDTO orderRequest, String userEmail) throws MPException, MPApiException {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilizador não encontrado"));

        Order order = new Order();

        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        final double[] totalAmount = {0.0};
        List<OrderItem> orderItems = orderRequest.items().stream().map(itemDto -> {
            Food food = foodRepository.findById(itemDto.foodId())
                    .orElseThrow(() -> new FoodNotFoundException("Produto com ID: " + itemDto.foodId() + " não encontrado."));

            OrderItem orderItem = new OrderItem();
            orderItem.setFood(food);
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(food.getPrice());
            orderItem.setOrder(order);

            totalAmount[0] += food.getPrice() * itemDto.quantity();

            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount[0]);

        orderRepository.save(order);

        PaymentRequestDTO paymentData = orderRequest.paymentData();

        PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                .transactionAmount(new BigDecimal(order.getTotalAmount()))
                .description("Pedido de " + user.getFullname())
                .installments(paymentData.installments())
                .paymentMethodId(paymentData.paymentMethodId())
                .issuerId(paymentData.issuerId())
                .payer(PaymentPayerRequest.builder().email(paymentData.email()).build())
                .build();

        PaymentClient client = new PaymentClient();
        Payment payment = client.create(createRequest);

        String paymentStatus = payment.getStatus();

        switch (paymentStatus) {
            case "approved":
                order.setStatus(OrderStatus.PAID);
                break;

            case "in_process":
                order.setStatus(OrderStatus.PENDING);
                break;

            case "rejected":
                order.setStatus(OrderStatus.CANCELLED);
                throw new PaymentException("Pagamento recusado pelo processador.");

            default:
                order.setStatus(OrderStatus.PENDING);
                break;
        }

        order.setPaymentId(payment.getId());
        orderRepository.save(order);

        return new OrderResponseDTO(order.getId(), order.getStatus().name(), String.valueOf(payment.getId()));
    }
}

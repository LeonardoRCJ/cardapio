package com.leo.cardapio.services;

import com.google.gson.Gson;
import com.leo.cardapio.model.food.Food;
import com.leo.cardapio.model.food.exceptions.FoodNotFoundException;
import com.leo.cardapio.model.order.Order;
import com.leo.cardapio.model.order.OrderStatus;
import com.leo.cardapio.model.order.dtos.*;
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

    /**
     * Orquestra a criação do pedido e o processamento do pagamento.
     * Este método não é transacional para separar a lógica de BD da chamada de API externa.
     */
    public OrderResponseDTO createOrderAndPayment(OrderRequestDTO orderRequest, String userEmail) {
        // Fase 1: Cria e salva o pedido na base de dados numa transação isolada.
        Order order = createAndSaveInitialOrder(orderRequest, userEmail);

        // Fase 2: Processa o pagamento através da API do Mercado Pago.
        Payment payment = processPayment(order, orderRequest.paymentData());

        // Fase 3: Atualiza o estado do pedido com base no resultado do pagamento, numa nova transação.
        updateOrderStatus(order, payment);

        return new OrderResponseDTO(order.getId(), order.getStatus().name(), String.valueOf(payment.getId()));
    }

    /**
     * Cria e persiste o pedido inicial com o estado PENDING.
     * Este método é transacional.
     */
    @Transactional
    public Order createAndSaveInitialOrder(OrderRequestDTO orderRequest, String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilizador não encontrado"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        final double[] totalAmount = {0.0};
        List<OrderItem> orderItems = orderRequest.items().stream().map(itemDto -> {
            Food food = foodRepository.findById(itemDto.foodId())
                    .orElseThrow(() -> new FoodNotFoundException("Produto com ID " + itemDto.foodId() + " não encontrado."));

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

        return orderRepository.save(order);
    }

    /**
     * Envia a requisição de pagamento para o Mercado Pago.
     * Este método não é transacional.
     */
    private Payment processPayment(Order order, PaymentRequestDTO paymentData) {
        try {
            PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                    .transactionAmount(new BigDecimal(order.getTotalAmount()))
                    .token(paymentData.token())
                    .description("Pedido #" + order.getId())
                    .installments(paymentData.installments())
                    .paymentMethodId(paymentData.paymentMethodId())
                    .issuerId(paymentData.issuerId())
                    .payer(PaymentPayerRequest.builder().email(paymentData.email()).build())
                    .build();

            PaymentClient client = new PaymentClient();
            return client.create(createRequest);
        } catch (MPApiException ex) {
            String errorBody = ex.getApiResponse().getContent();
            System.err.println("Erro da API do Mercado Pago: " + errorBody);
            throw new PaymentException("Erro ao processar o pagamento");
        } catch (MPException ex) {
            System.err.println("Erro no SDK do Mercado Pago: " + ex.getMessage());
            throw new PaymentException("Erro na comunicação com o serviço de pagamento.");
        }
    }

    /**
     * Atualiza o estado do pedido com base no resultado do pagamento.
     * Este método é transacional.
     */
    @Transactional
    public void updateOrderStatus(Order order, Payment payment) {
        order.setPaymentId(payment.getId());
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
                orderRepository.save(order); // Salva o estado CANCELADO antes de lançar a exceção
                throw new PaymentException("Pagamento recusado pelo processador.");
            default:
                order.setStatus(OrderStatus.PENDING);
                break;
        }
        orderRepository.save(order);
    }

    /**
     * Lida com as notificações de webhook recebidas do Mercado Pago.
     */
    @Transactional
    public void handlePaymentWebhook(WebhookNotificationDTO notification) {
        if (notification != null && "payment.updated".equals(notification.action())) {
            Long paymentId = Long.parseLong(notification.data().id());

            // Numa aplicação de produção, aqui faríamos uma chamada à API do MP para obter
            // o estado final e confirmado do pagamento, para maior segurança.
            // Ex: Payment payment = new PaymentClient().get(paymentId);
            // String status = payment.getStatus();

            Order order = orderRepository.findByPaymentId(paymentId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado para o pagamento ID: " + paymentId));

            // Lógica simplificada: se recebermos uma notificação de um pedido pendente,
            // atualizamos para PAGO.
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getOrderHistoryForUser(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(()-> new UserNotFoundException("Utilizador não encontrado"));

        List<Order> orders = orderRepository.findByUserIdOrderByDateDesc(user.getId());

        return orders.stream()
                .map(OrderHistoryDTO::new)
                .collect(Collectors.toList());
    }
}
package com.leo.cardapio.repositories;

import com.leo.cardapio.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByPaymentId(Long paymentId);

    List<Order> findByUserIdOrderByDateDesc(UUID userId);
}

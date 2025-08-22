package com.leo.cardapio.repositories;

import com.leo.cardapio.model.food.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {


}

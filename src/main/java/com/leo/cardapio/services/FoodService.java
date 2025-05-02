package com.leo.cardapio.services;

import com.leo.cardapio.food.Food;
import com.leo.cardapio.food.dtos.UpdateFoodDTO;
import com.leo.cardapio.food.exceptions.FoodNotFoundException;
import com.leo.cardapio.repositories.FoodRepository;
import com.leo.cardapio.food.dtos.FoodRequestDTO;
import com.leo.cardapio.food.dtos.FoodResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository repository;

    public FoodService(FoodRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Long saveFood(FoodRequestDTO data){
        Food newFood = data.toEntity();

        return repository.save(newFood).getId();
    }

    @Transactional(readOnly = true)
    public List<FoodResponseDTO> getAllFoods(){
        return repository.findAll().stream().map(FoodResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public FoodResponseDTO getFoodById(Long id){
        var food = repository.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found by ID: " + id));

        return new FoodResponseDTO(food);
    }

    @Transactional
    public void deleteFoodById(Long id){
        var food = repository.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found by ID: " + id));

        repository.deleteById(id);
    }


    @Transactional
    public void updateFoodById(Long id, UpdateFoodDTO update){
        var foodToBeUpdated = repository.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found by ID: " + id));

        if (!update.title().isEmpty()){
            foodToBeUpdated.setTitle(update.title());
        }
        if (!update.price().isEmpty()){
            foodToBeUpdated.setPrice(Double.valueOf(update.price()));
        }
        if (!update.image().isEmpty()){
            foodToBeUpdated.setImage(update.image());
        }

        repository.save(foodToBeUpdated);
    }
}

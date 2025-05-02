package com.leo.cardapio.controller;

import com.leo.cardapio.food.Food;
import com.leo.cardapio.food.dtos.UpdateFoodDTO;
import com.leo.cardapio.repositories.FoodRepository;
import com.leo.cardapio.food.dtos.FoodRequestDTO;
import com.leo.cardapio.food.dtos.FoodResponseDTO;
import com.leo.cardapio.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/foods")
public class FoodController {

    private final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Void> saveFood(@RequestBody FoodRequestDTO data){
        var foodId = service.saveFood(data);

        return ResponseEntity.created(URI.create("api/foods/" + foodId)).build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<List<FoodResponseDTO>> getAllFoods(){
        List<FoodResponseDTO> foodList = service.getAllFoods();
        return ResponseEntity.ok().body(foodList);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDTO> getFoodById(@PathVariable("id") Long id){
        var food = service.getFoodById(id);

        return ResponseEntity.ok().body(food);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodById(@PathVariable("id") Long id){
        service.deleteFoodById(id);

        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFoodById(@PathVariable("id") Long id,
                                               @RequestBody UpdateFoodDTO update){
        service.updateFoodById(id, update);

        return ResponseEntity.noContent().build();
    }
}

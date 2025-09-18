package com.leo.cardapio.controller.admin;

import com.leo.cardapio.model.food.dtos.FoodRequestDTO;
import com.leo.cardapio.model.food.dtos.UpdateFoodDTO;
import com.leo.cardapio.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/foods")
public class FoodAdminController {

    private final FoodService service;

    public FoodAdminController(FoodService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Void> saveFood(@RequestBody FoodRequestDTO data){
        var foodId = service.saveFood(data);

        return ResponseEntity.created(URI.create("api/foods/" + foodId)).build();
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

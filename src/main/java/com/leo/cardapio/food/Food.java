package com.leo.cardapio.food;

import com.leo.cardapio.food.dtos.FoodRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "foods")
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String image;
    private Double price;
}

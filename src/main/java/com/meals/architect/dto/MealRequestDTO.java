package com.meals.architect.dto;

import lombok.Data;
import java.util.List;

@Data
public class MealRequestDTO {
    private Long userId; // The user saving the meal
    private String name; // e.g., "Post-Workout Bowl"
    private List<IngredientRequest> ingredients;

    // A small nested class to hold the ID and quantity
    @Data
    public static class IngredientRequest {
        private Long ingredientId;
        private Integer quantity;
    }
}
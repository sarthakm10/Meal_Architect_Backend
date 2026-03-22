package com.meals.architect.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meal_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links back to the Meal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    // Links to the specific Ingredient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    // Stores how much of the ingredient is in this specific meal
    @Column(nullable = false)
    private Integer quantity;
}
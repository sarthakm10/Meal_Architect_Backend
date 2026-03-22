package com.meals.architect.controller;

import com.meals.architect.models.Ingredient;
import com.meals.architect.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    // GET all ingredients
    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientRepository.findAll());
    }

    // GET filtered search (e.g., /api/ingredients/search?category=PROTEIN)
    @GetMapping("/search")
    public ResponseEntity<List<Ingredient>> searchIngredients(
            @RequestParam(required = false) Ingredient.Category category) {
        
        if (category != null) {
            return ResponseEntity.ok(ingredientRepository.findByCategory(category));
        }
        return ResponseEntity.ok(ingredientRepository.findAll());
    }
}
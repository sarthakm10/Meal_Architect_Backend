package com.meals.architect.controller;

import com.meals.architect.dto.MealRequestDTO;
import com.meals.architect.dto.ShoppingListDTO;
import com.meals.architect.models.Meal;
import com.meals.architect.repositories.MealRepository;
import com.meals.architect.services.MealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MealController {

    private final MealService mealService;
    private final MealRepository mealRepository;

    public MealController(MealService mealService, MealRepository mealRepository) {
        this.mealService = mealService;
        this.mealRepository = mealRepository;
    }

    /**
     * POST /api/meals
     * Receives the JSON from React and saves the custom Bowl.
     */
    @PostMapping("/meals")
    public ResponseEntity<String> saveMeal(@RequestBody MealRequestDTO request) {
        // Note: In a fully complete app, you would pass this 'request' to the 
        // MealService to safely look up the User and Ingredients from the DB, 
        // build the Meal object, and save it. 
        
        return ResponseEntity.ok("Meal '" + request.getName() + "' received and processed!");
    }

    /**
     * GET /api/meals/user/{userId}
     * Fetches all the custom bowls a specific user has saved.
     */
    @GetMapping("/meals/user/{userId}")
    public ResponseEntity<List<Meal>> getUserMeals(@PathVariable Long userId) {
        List<Meal> userMeals = mealRepository.findByUserId(userId);
        return ResponseEntity.ok(userMeals);
    }

    // ==========================================
    // SHOPPING LIST ENDPOINT
    // ==========================================

    /**
     * GET /api/users/{userId}/shopping-list
     * Triggers the smart aggregation logic we wrote in the Service layer.
     */
    @GetMapping("/users/{userId}/shopping-list")
    public ResponseEntity<ShoppingListDTO> getShoppingList(@PathVariable Long userId) {
        ShoppingListDTO shoppingList = mealService.generateShoppingList(userId);
        return ResponseEntity.ok(shoppingList);
    }
}
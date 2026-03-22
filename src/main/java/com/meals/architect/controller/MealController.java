package com.meals.architect.controller;

import com.meals.architect.dto.MealRequestDTO;
import com.meals.architect.dto.ShoppingListDTO;
import com.meals.architect.models.Ingredient;
import com.meals.architect.models.Meal;
import com.meals.architect.models.MealIngredient;
import com.meals.architect.models.User;
import com.meals.architect.repositories.IngredientRepository;
import com.meals.architect.repositories.MealRepository;
import com.meals.architect.repositories.UserRepository;
import com.meals.architect.services.MealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MealController {

    private final MealService mealService;
    private final MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    public MealController(MealService mealService, MealRepository mealRepository) {
        this.mealService = mealService;
        this.mealRepository = mealRepository;
    }

    /**
     * POST /api/meals
     * Receives the JSON from React and saves the custom Bowl.
     */
    @PostMapping("/meals")
    public ResponseEntity<Meal> saveMeal(@RequestBody MealRequestDTO request) {
        // 1. Find the user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Create the new Meal
        Meal newMeal = new Meal();
        newMeal.setName(request.getName());
        newMeal.setUser(user);

        // 3. Loop through the requested ingredients and build the mapping
        for (MealRequestDTO.IngredientRequest item : request.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(item.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));

            MealIngredient mealIngredient = new MealIngredient();
            mealIngredient.setMeal(newMeal); // Link to the new meal
            mealIngredient.setIngredient(ingredient);
            mealIngredient.setQuantity(item.getQuantity());

            newMeal.getMealIngredients().add(mealIngredient);
        }

        // 4. Save to database (Cascade rules will automatically save the MealIngredients too)
        Meal savedMeal = mealRepository.save(newMeal);
        return ResponseEntity.ok(savedMeal);
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

    // GET a single meal by ID
    @GetMapping("/meals/{mealId}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long mealId) {
        return mealRepository.findById(mealId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a meal
    @DeleteMapping("/meals/{mealId}")
    public ResponseEntity<String> deleteMeal(@PathVariable Long mealId) {
        if (mealRepository.existsById(mealId)) {
            mealRepository.deleteById(mealId);
            return ResponseEntity.ok("Meal deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    // POST Duplicate a meal
    @PostMapping("/meals/{mealId}/duplicate")
    public ResponseEntity<Meal> duplicateMeal(@PathVariable Long mealId) {
        Meal originalMeal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        Meal copy = new Meal();
        copy.setName(originalMeal.getName() + " - Copy");
        copy.setUser(originalMeal.getUser());

        // Copy all ingredients precisely
        for (MealIngredient originalItem : originalMeal.getMealIngredients()) {
            MealIngredient newItem = new MealIngredient();
            newItem.setMeal(copy);
            newItem.setIngredient(originalItem.getIngredient());
            newItem.setQuantity(originalItem.getQuantity());
            copy.getMealIngredients().add(newItem);
        }

        return ResponseEntity.ok(mealRepository.save(copy));
    }
}
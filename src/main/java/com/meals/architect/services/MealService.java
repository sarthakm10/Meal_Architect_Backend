package com.meals.architect.services;

import com.meals.architect.dto.ShoppingListDTO;
import com.meals.architect.dto.ShoppingListItemDTO;
import com.meals.architect.models.Ingredient;
import com.meals.architect.models.Meal;
import com.meals.architect.models.MealIngredient;
import com.meals.architect.repositories.MealRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MealService {

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    /**
     * Generates a deduplicated, categorized shopping list for a user.
     */
    public ShoppingListDTO generateShoppingList(Long userId) {
        // 1. Fetch all meals for this user using our optimized repository query
        List<Meal> userMeals = mealRepository.findMealsWithIngredientsByUserId(userId);

        // 2. Aggregate the ingredients (Sum up the quantities)
        // Using a Map where the Ingredient is the key, and total quantity is the value
        Map<Ingredient, Integer> aggregatedIngredients = new HashMap<>();

        for (Meal meal : userMeals) {
            for (MealIngredient mi : meal.getMealIngredients()) {
                Ingredient ingredient = mi.getIngredient();
                int currentQuantity = aggregatedIngredients.getOrDefault(ingredient, 0);
                aggregatedIngredients.put(ingredient, currentQuantity + mi.getQuantity());
            }
        }

        // 3. Transform into DTOs and Group by Category
        Map<String, List<ShoppingListItemDTO>> categorizedList = new HashMap<>();
        double grandTotalCost = 0.0;

        for (Map.Entry<Ingredient, Integer> entry : aggregatedIngredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int totalQuantity = entry.getValue();
            
            // Handle potential null costs to prevent math errors
            double costPerUnit = (ingredient.getCost() != null) ? ingredient.getCost() : 0.0;
            double totalItemCost = costPerUnit * totalQuantity;
            
            grandTotalCost += totalItemCost;

            ShoppingListItemDTO itemDTO = new ShoppingListItemDTO(
                    ingredient.getName(),
                    totalQuantity,
                    costPerUnit,
                    totalItemCost
            );

            // Group by the Enum category name (e.g., "PROTEIN", "VEGGIE")
            String categoryName = ingredient.getCategory().name();
            
            // If the category list doesn't exist yet in our map, create it. Then add the item.
            categorizedList.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(itemDTO);
        }

        // 4. Return the final formatted package
        return new ShoppingListDTO(userId, grandTotalCost, categorizedList);
    }
}
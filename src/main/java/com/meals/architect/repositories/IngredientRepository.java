package com.meals.architect.repositories;

import com.meals.architect.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    // Fetch all ingredients of a specific type (e.g., all PROTEIN)
    List<Ingredient> findByCategory(Ingredient.Category category);

    // Fetch ingredients that cost less than a certain amount
    List<Ingredient> findByCostLessThanEqual(Double maxCost);
    
    // A more complex query using JPQL to find ingredients matching a specific tag
    // SELECT i FROM Ingredient i JOIN i.tags t WHERE t.name = ?
    List<Ingredient> findByTags_Name(String tagName);
}
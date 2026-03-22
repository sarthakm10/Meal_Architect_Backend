package com.meals.architect.repositories;

import com.meals.architect.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByCategory(Ingredient.Category category);

    List<Ingredient> findByCostLessThanEqual(Double maxCost);
    
    List<Ingredient> findByTags_Name(String tagName);
}
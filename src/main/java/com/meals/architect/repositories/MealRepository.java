package com.meals.architect.repositories;

import com.meals.architect.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    // Find all meals created by a specific user
    List<Meal> findByUserId(Long userId);

    // An optimized query: When we fetch a meal, also fetch its ingredients in one trip
    // to prevent the "N+1 query problem" and speed up your API.
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.mealIngredients mi LEFT JOIN FETCH mi.ingredient WHERE m.user.id = :userId")
    List<Meal> findMealsWithIngredientsByUserId(@Param("userId") Long userId);
}
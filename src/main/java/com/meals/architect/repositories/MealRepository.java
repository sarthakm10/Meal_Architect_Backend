package com.meals.architect.repositories;

import com.meals.architect.models.Meal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUserId(Long userId);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.mealIngredients mi LEFT JOIN FETCH mi.ingredient WHERE m.user.id = :userId")
    List<Meal> findMealsWithIngredientsByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id <> :userId ORDER BY m.id DESC")
    List<Meal> findCommunityMeals(@Param("userId") Long userId, Pageable pageable);
}
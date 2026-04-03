package com.meals.architect.repositories;

import com.meals.architect.models.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    
    // Spring writes the SQL: SELECT * FROM meal_logs WHERE user_id = ? AND consumed_date = ?
    List<MealLog> findByUserIdAndConsumedDate(Long userId, LocalDate date);
}
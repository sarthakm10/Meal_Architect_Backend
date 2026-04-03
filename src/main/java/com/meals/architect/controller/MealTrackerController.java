package com.meals.architect.controller;

import com.meals.architect.models.Meal;
import com.meals.architect.models.MealLog;
import com.meals.architect.models.User;
import com.meals.architect.repositories.MealLogRepository;
import com.meals.architect.repositories.MealRepository;
import com.meals.architect.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MealTrackerController {

    @Autowired
    private MealLogRepository mealLogRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================================
    // GET — Fetch all meal logs for a specific date
    // ==========================================

    /**
     * GET /api/users/{userId}/meal-logs?date=2026-04-03
     * Returns all MealLog entries with full meal + ingredient details.
     */
    @GetMapping("/users/{userId}/meal-logs")
    public ResponseEntity<List<MealLog>> getMealLogs(
            @PathVariable Long userId,
            @RequestParam String date) {

        LocalDate parsedDate = LocalDate.parse(date);
        List<MealLog> logs = mealLogRepository.findByUserIdAndConsumedDate(userId, parsedDate);
        return ResponseEntity.ok(logs);
    }

    // ==========================================
    // POST — Log a saved meal to the tracker
    // ==========================================

    /**
     * POST /api/users/{userId}/meal-logs
     * Body: { "mealId": 5, "date": "2026-04-03" }
     * Logs an existing saved meal to the user's daily tracker.
     */
    @PostMapping("/users/{userId}/meal-logs")
    public ResponseEntity<MealLog> logMeal(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        Long mealId = Long.parseLong(request.get("mealId"));
        LocalDate date = LocalDate.parse(request.get("date"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        MealLog log = new MealLog();
        log.setUser(user);
        log.setMeal(meal);
        log.setConsumedDate(date);

        String mealTypeStr = request.get("mealType");
        if (mealTypeStr != null && !mealTypeStr.isBlank()) {
            log.setMealType(MealLog.MealType.valueOf(mealTypeStr.toUpperCase()));
        }

        MealLog savedLog = mealLogRepository.save(log);
        return ResponseEntity.ok(savedLog);
    }

    // ==========================================
    // DELETE — Remove a meal log entry
    // ==========================================

    /**
     * DELETE /api/meal-logs/{logId}
     * Removes a single meal log entry.
     */
    @DeleteMapping("/meal-logs/{logId}")
    public ResponseEntity<String> deleteMealLog(@PathVariable Long logId) {
        if (mealLogRepository.existsById(logId)) {
            mealLogRepository.deleteById(logId);
            return ResponseEntity.ok("Meal log removed.");
        }
        return ResponseEntity.notFound().build();
    }
}

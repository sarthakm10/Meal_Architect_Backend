package com.meals.architect.services;

import com.meals.architect.dto.DailyProgressDTO;
import com.meals.architect.models.MealIngredient;
import com.meals.architect.models.MealLog;
import com.meals.architect.models.UserProfile;
import com.meals.architect.repositories.MealLogRepository;
import com.meals.architect.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {

    @Autowired
    private MealLogRepository mealLogRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;

    public DailyProgressDTO calculateDailyProgress(Long userId, LocalDate date) {
        // 1. Fetch the user's targets (Default to 0 if they haven't set up a profile yet)
        UserProfile profile = userProfileRepository.findById(userId).orElse(new UserProfile());
        Integer targetCals = profile.getDailyCalorieGoal() != null ? profile.getDailyCalorieGoal() : 0;
        Double targetProtein = profile.getDailyProteinGoal() != null ? profile.getDailyProteinGoal() : 0.0;

        // 2. Fetch all meals logged for this specific day
        List<MealLog> dailyLogs = mealLogRepository.findByUserIdAndConsumedDate(userId, date);

        // 3. Calculate totals
        int totalCalories = 0;
        double totalProtein = 0.0;

        for (MealLog log : dailyLogs) {
            for (MealIngredient mi : log.getMeal().getMealIngredients()) {
                int quantity = mi.getQuantity();
                
                // Add Calories
                if (mi.getIngredient().getCalories() != null) {
                    totalCalories += (mi.getIngredient().getCalories() * quantity);
                }
                
                // Add Protein
                if (mi.getIngredient().getProteinGrams() != null) {
                    totalProtein += (mi.getIngredient().getProteinGrams() * quantity);
                }
            }
        }

        // 4. Determine if goals are met (Only if a target is actually set above 0)
        boolean calMet = targetCals > 0 && totalCalories >= targetCals;
        boolean proMet = targetProtein > 0 && totalProtein >= targetProtein;

        return new DailyProgressDTO(targetCals, totalCalories, targetProtein, totalProtein, calMet, proMet);
    }
}
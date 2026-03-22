package com.meals.architect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyProgressDTO {
    private Integer targetCalories;
    private Integer consumedCalories;
    
    private Double targetProtein;
    private Double consumedProtein;
    
    // A helpful boolean for the React UI to show a "Goal Met!" animation
    private boolean calorieGoalMet;
    private boolean proteinGoalMet;
}
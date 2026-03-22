package com.meals.architect.controller;

import com.meals.architect.dto.DailyProgressDTO;
import com.meals.architect.models.UserProfile;
import com.meals.architect.repositories.UserProfileRepository;
import com.meals.architect.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    @Autowired
    private UserProfileRepository profileRepository;

    @Autowired
    private ProgressService progressService;

    // ==========================================
    // GOALS ENDPOINTS
    // ==========================================

    /**
     * GET /api/users/{userId}/goals
     * Retrieves the user's saved macro and budget targets.
     */
    @GetMapping("/{userId}/goals")
    public ResponseEntity<UserProfile> getUserGoals(@PathVariable Long userId) {
        return profileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/users/{userId}/goals
     * Updates or creates the user's targets.
     */
    @PutMapping("/{userId}/goals")
    public ResponseEntity<UserProfile> updateGoals(@PathVariable Long userId, @RequestBody UserProfile updatedProfile) {
        // Ensure the ID in the body matches the URL path for security
        updatedProfile.setUserId(userId);
        
        // save() acts as an "upsert" - it updates if it exists, or inserts if it's new
        UserProfile savedProfile = profileRepository.save(updatedProfile);
        return ResponseEntity.ok(savedProfile);
    }

    // ==========================================
    // PROGRESS ENDPOINT
    // ==========================================

    /**
     * GET /api/users/{userId}/progress?date=2026-03-22
     * Calculates how close the user is to their goals based on the meals they ate today.
     */
    @GetMapping("/{userId}/progress")
    public ResponseEntity<DailyProgressDTO> getDailyProgress(
            @PathVariable Long userId, 
            @RequestParam String date) {
        
        // Convert the string "YYYY-MM-DD" from the URL into a Java LocalDate
        LocalDate parsedDate = LocalDate.parse(date);
        
        DailyProgressDTO progress = progressService.calculateDailyProgress(userId, parsedDate);
        return ResponseEntity.ok(progress);
    }
}
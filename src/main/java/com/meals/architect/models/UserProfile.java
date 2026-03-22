package com.meals.architect.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private Long userId; // Uses the exact same ID as the User

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Tells Hibernate to share the Primary Key with the User table
    @JoinColumn(name = "user_id")
    @JsonIgnore // Prevents infinite JSON loops!
    private User user;

    @Column(name = "daily_calorie_goal")
    private Integer dailyCalorieGoal;

    @Column(name = "daily_protein_goal")
    private Double dailyProteinGoal;

    @Column(name = "weekly_budget_goal")
    private Double weeklyBudgetGoal;
}
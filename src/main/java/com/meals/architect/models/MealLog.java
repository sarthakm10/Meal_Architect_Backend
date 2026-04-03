package com.meals.architect.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "meal_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Column(name = "consumed_date", nullable = false)
    private LocalDate consumedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    public enum MealType {
        BREAKFAST, LUNCH, SNACK, DINNER
    }
}
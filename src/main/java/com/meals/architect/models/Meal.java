package com.meals.architect.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Links this meal to a specific user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Links this meal to its specific ingredients and quantities
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> mealIngredients = new ArrayList<>();
}
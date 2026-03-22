package com.meals.architect.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private Integer calories;

    @Column(name = "protein_grams")
    private Double proteinGrams;

    private Double cost;

    @Column(name = "icon_url")
    private String iconUrl;

    // Creates the mapping table 'ingredient_tags' automatically
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ingredient_tags",
        joinColumns = @JoinColumn(name = "ingredient_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    public enum Category {
        PROTEIN, VEGGIE, CARB, SPICE, LIQUID
    }
}
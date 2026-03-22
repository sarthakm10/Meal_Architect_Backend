package com.meals.architect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ShoppingListDTO {
    private Long userId;
    private Double totalEstimatedCost;
    
    // A Map where the key is the Category (e.g., "PROTEIN") 
    // and the value is the list of items in that category.
    private Map<String, List<ShoppingListItemDTO>> groceryList;
}
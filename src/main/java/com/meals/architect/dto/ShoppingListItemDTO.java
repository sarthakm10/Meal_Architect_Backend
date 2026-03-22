package com.meals.architect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingListItemDTO {
    private String name;
    private Integer quantity;
    private Double costPerUnit;
    private Double totalItemCost;
}
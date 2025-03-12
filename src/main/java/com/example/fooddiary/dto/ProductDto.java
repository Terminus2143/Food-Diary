package com.example.fooddiary.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
}
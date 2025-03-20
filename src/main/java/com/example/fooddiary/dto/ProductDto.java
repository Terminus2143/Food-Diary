package com.example.fooddiary.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;
    private Double calories;
}
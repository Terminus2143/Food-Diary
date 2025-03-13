package com.example.fooddiary.dto;

import lombok.Data;

@Data
public class DishDto {
    private String name;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;
    private Double calories;
    private Integer userId;
}

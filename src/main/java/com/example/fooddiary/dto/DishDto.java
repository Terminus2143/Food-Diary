package com.example.fooddiary.dto;

import java.util.List;
import lombok.Data;

@Data
public class DishDto {
    private String name;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;
    private Double calories;
    private Integer userId;
    private List<String> productNames;
}

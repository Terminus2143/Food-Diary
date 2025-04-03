package com.example.fooddiary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class DishDto {

    @NotBlank(message = "Название блюда не может быть пустым")
    private String name;

    private Double proteins;
    private Double fats;
    private Double carbohydrates;
    private Double calories;
    private Integer userId;

    @NotNull(message = "Список продуктов не может быть null")
    @Size(min = 1, message = "Блюдо должно содержать хотя бы один продукт")
    private List<String> productNames;
}

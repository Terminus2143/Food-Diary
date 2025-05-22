package com.example.fooddiary.dto;

import com.example.fooddiary.validation.MaxTotalGrams;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@MaxTotalGrams
public class ProductDto {
    @NotBlank(message = "Название продукта не может быть пустым")
    private String name;

    @NotNull(message = "Количество белков не может быть null")
    @PositiveOrZero(message = "Количество белков должно быть положительным числом")
    private Double proteins;

    @NotNull(message = "Количество жиров не может быть null")
    @PositiveOrZero(message = "Количество жиров должно быть положительным числом")
    private Double fats;

    @NotNull(message = "Количество углеводов не может быть null")
    @PositiveOrZero(message = "Количество углеводов должно быть положительным числом")
    private Double carbohydrates;

    @NotNull(message = "Количество калорий не может быть null")
    @PositiveOrZero(message = "Количество калорий должно быть положительным числом")
    private Double calories;
}
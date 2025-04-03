package com.example.fooddiary.validation;

import com.example.fooddiary.dto.ProductDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxTotalGramsValidator implements ConstraintValidator<MaxTotalGrams, ProductDto> {

    @Override
    public boolean isValid(ProductDto productDto, ConstraintValidatorContext context) {
        if (productDto == null) {
            return true;
        }

        double total = (productDto.getProteins() != null ? productDto.getProteins() : 0)
                + (productDto.getFats() != null ? productDto.getFats() : 0)
                + (productDto.getCarbohydrates() != null ? productDto.getCarbohydrates() : 0);

        return total <= 100;
    }
}

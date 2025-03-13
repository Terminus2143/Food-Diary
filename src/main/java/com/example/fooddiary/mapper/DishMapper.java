package com.example.fooddiary.mapper;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {

    public DishDto toDto(Dish dish) {
        DishDto dishDto = new DishDto();
        dishDto.setName(dish.getName());
        dishDto.setProteins(dish.getProteins());
        dishDto.setFats(dish.getFats());
        dishDto.setCarbohydrates(dish.getCarbohydrates());
        dishDto.setCalories(dish.getCalories());
        dishDto.setUserId(dish.getUser().getId());
        return dishDto;
    }

    public Dish toEntity(DishDto dishDto) {
        Dish dish = new Dish();
        dish.setName(dishDto.getName());
        dish.setProteins(dishDto.getProteins());
        dish.setFats(dishDto.getFats());
        dish.setCarbohydrates(dishDto.getCarbohydrates());
        dish.setCalories(dishDto.getCalories());
        return dish;
    }
}

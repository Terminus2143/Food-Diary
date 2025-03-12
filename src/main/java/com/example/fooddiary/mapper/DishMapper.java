package com.example.fooddiary.mapper;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {

    public DishDto toDto(Dish user) {
        DishDto userDto = new DishDto();
        userDto.setName(user.getName());
        userDto.setProteins(user.getProteins());
        userDto.setFats(user.getFats());
        userDto.setCarbohydrates(user.getCarbohydrates());
        userDto.setCalories(user.getCalories());
        return userDto;
    }

    public Dish toEntity(DishDto userDto) {
        Dish user = new Dish();
        user.setName(userDto.getName());
        user.setProteins(userDto.getProteins());
        user.setFats(userDto.getFats());
        user.setCarbohydrates(userDto.getCarbohydrates());
        user.setCalories(userDto.getCalories());
        return user;
    }
}

package com.example.fooddiary.controller;

import com.example.fooddiary.model.FoodDiaryEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food")
public class ProductAndDishesController {

    @GetMapping(value = "/food-entry", produces = "application/json")
    public FoodDiaryEntry getProductEntry(
            @RequestParam(name = "product", defaultValue = "Unknown Food") String foodName,
            @RequestParam(name = "proteins", defaultValue = "0.0") double proteins,
            @RequestParam(name = "fats", defaultValue = "0.0") double fats,
            @RequestParam(name = "carbohydrates", defaultValue = "0.0") double carbohydrates) {
        return new FoodDiaryEntry(foodName, proteins, fats, carbohydrates);
    }

    @GetMapping(value = "/food-entry/{foodName}", produces = "application/json")
    public FoodDiaryEntry getDishWithPathParam(
            @PathVariable("foodName") String foodName,
            @RequestParam(name = "proteins", defaultValue = "0.0") double proteins,
            @RequestParam(name = "fats", defaultValue = "0.0") double fats,
            @RequestParam(name = "carbohydrates", defaultValue = "0.0") double carbohydrates) {
        return new FoodDiaryEntry(foodName, proteins, fats, carbohydrates);
    }
}

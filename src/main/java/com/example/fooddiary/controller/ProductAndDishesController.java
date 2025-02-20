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
    public FoodDiaryEntry getProductEntry(@RequestParam(name = "product",
            defaultValue = "Unknown Food") String foodName) {
        return new FoodDiaryEntry(foodName);
    }

    @GetMapping(value = "/food-entry/{foodName}", produces = "application/json")
    public FoodDiaryEntry getDishWithPathParam(@PathVariable("foodName") String foodName) {
        return new FoodDiaryEntry(foodName);
    }
}

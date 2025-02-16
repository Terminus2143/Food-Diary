package com.example.fooddiary.controller;

import com.example.fooddiary.model.FoodDiaryEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for food diary entries.
 */
@RestController
@RequestMapping("/api")
public class Controller {

  /**
   * Returns a food diary entry using a query parameter for food name.
   *
   * @param foodName The name of the food. Default is "Unknown Food".
   * @return A JSON object representing the food diary entry.
   */
  @GetMapping(value = "/food-entry", produces = "application/json")
  public FoodDiaryEntry getFoodEntry(@RequestParam(name = "foodName", defaultValue = "Unknown Food")
                                       String foodName) {
    return new FoodDiaryEntry(foodName);
  }

  /**
   * Returns a food diary entry using a path parameter for food name.
   *
   * @param foodName The name of the food.
   * @return A JSON object representing the food diary entry.
   */
  @GetMapping(value = "/food-entry/{foodName}", produces = "application/json")
  public FoodDiaryEntry getFoodEntryWithPathParam(@PathVariable("foodName") String foodName) {
    return new FoodDiaryEntry(foodName);
  }
}

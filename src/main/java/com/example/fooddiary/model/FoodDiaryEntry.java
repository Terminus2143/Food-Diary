package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a food diary entry.
 */
@Setter
@Getter
public class FoodDiaryEntry {
  @JsonProperty("foodName")
  private String foodName;

  /**
   * Constructs a new {@code FoodDiaryEntry} with the specified food name.
   *
   * @param foodName The name of the food item.
   */
  public FoodDiaryEntry(String foodName) {
    this.foodName = foodName;
  }
}

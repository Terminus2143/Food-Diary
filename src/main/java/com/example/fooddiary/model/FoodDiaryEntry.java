package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FoodDiaryEntry {
    @JsonProperty("foodName")
    private String foodName;

    public FoodDiaryEntry(String foodName) {
        this.foodName = foodName;
    }
}

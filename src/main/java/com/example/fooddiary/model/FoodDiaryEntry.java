package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodDiaryEntry {

    @JsonProperty("foodName")
    private String foodName;

    @JsonProperty("proteins")
    private double proteins;

    @JsonProperty("fats")
    private double fats;

    @JsonProperty("carbohydrates")
    private double carbohydrates;

    public FoodDiaryEntry(String foodName, double proteins, double fats, double carbohydrates) {
        this.foodName = foodName;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}

//http://localhost:8080/food/food-entry?product=Apple&proteins=0.3&fats=0.2&carbohydrates=14.0
//http://localhost:8080/food/food-entry/Apple?proteins=0.3&fats=0.2
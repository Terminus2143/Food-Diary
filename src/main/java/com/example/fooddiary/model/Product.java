package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonIgnore
    @JsonProperty("id")
    private int id;

    @JsonProperty("foodName")
    private String productName;

    @JsonProperty("proteins")
    private double proteins;

    @JsonProperty("fats")
    private double fats;

    @JsonProperty("carbohydrates")
    private double carbohydrates;

    public Product(int id,
                   String productName,
                   double proteins,
                   double fats,
                   double carbohydrates) {
        this.id = id;
        this.productName = productName;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }
}
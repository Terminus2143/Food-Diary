package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @org.hibernate.annotations.ColumnTransformer(read = "LOWER(name)", write = "LOWER(?)")
    private String name;

    private Double proteins;
    private Double fats;
    private Double carbohydrates;
    private Double calories;

    @JsonIgnore
    @ManyToMany(
            mappedBy = "products",
            cascade = CascadeType.ALL
    )
    private List<Dish> dishes = new ArrayList<>();
}

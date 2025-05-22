package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @org.hibernate.annotations.ColumnTransformer(read = "LOWER(name)", write = "LOWER(?)")
    private String name;

    private Double proteins;
    private Double fats;
    private Double carbohydrates;
    private Double calories;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "dish_products",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void roundValues() {
        this.proteins = roundToOneDecimal(this.proteins);
        this.fats = roundToOneDecimal(this.fats);
        this.carbohydrates = roundToOneDecimal(this.carbohydrates);
        this.calories = roundToOneDecimal(this.calories);
    }

    private Double roundToOneDecimal(Double value) {
        if (value != null) {
            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(1, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        return value;
    }
}

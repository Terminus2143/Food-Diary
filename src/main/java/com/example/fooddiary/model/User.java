package com.example.fooddiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @org.hibernate.annotations.ColumnTransformer(read = "LOWER(username)", write = "LOWER(?)")
    private String username;

    @Column(unique = true, nullable = false)
    @org.hibernate.annotations.ColumnTransformer(read = "LOWER(email)", write = "LOWER(?)")
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private String gender;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Dish> dishes = new ArrayList<>();
}

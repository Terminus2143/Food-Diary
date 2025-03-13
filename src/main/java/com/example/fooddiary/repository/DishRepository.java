package com.example.fooddiary.repository;

import com.example.fooddiary.model.Dish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    Optional<Dish> findByName(String dishName);
    List<Dish> findByUserId(Integer userId);
    Optional<Dish> findByIdAndUserId(Integer id, Integer userId);
}

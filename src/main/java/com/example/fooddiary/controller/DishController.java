package com.example.fooddiary.controller;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.service.DishService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/users/{userId}/dishes")
    public ResponseEntity<Dish> addDish(
            @PathVariable Integer userId,
            @RequestBody DishDto dishDto
    ) {
        return dishService.addDish(userId, dishDto);
    }

    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getAllDishes() {
        List<Dish> dishes = dishService.getAllDishes();
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Integer id) {
        Dish dish = dishService.getDishById(id);
        return ResponseEntity.ok(dish);
    }

    @PutMapping("/dishes/{id}")
    public Dish updateDish(@PathVariable Integer id, @RequestBody DishDto dishDto) {
        return dishService.updateDish(id, dishDto);
    }

    @DeleteMapping("/dishes/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Integer id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dishes-by-user/{userId}")
    public ResponseEntity<List<Dish>> getDishesByUserIdNative(@PathVariable Integer userId) {
        return ResponseEntity.ok(dishService.getAllDishesByUserIdNative(userId));
    }
}
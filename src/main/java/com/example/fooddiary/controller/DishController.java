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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/dishes")
    public Dish addDish(@RequestBody DishDto dishDto) {
        return dishService.addDish(dishDto);
    }

    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getAllDishs() {
        return dishService.getAllDishs();
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Integer id) {
        return dishService.getDishById(id);
    }

    @GetMapping("/food_dish")
    public ResponseEntity<Dish> getDishByName(@RequestParam String dish) {
        return dishService.getDishByName(dish);
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
}
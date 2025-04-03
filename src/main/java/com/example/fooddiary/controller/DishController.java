package com.example.fooddiary.controller;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Блюда", description = "Управление блюдами")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/users/{userId}/dishes")
    @Operation(
            summary = "Добавить новое блюдо",
            description = "Добавляет блюдо с указанными продуктами")
    public ResponseEntity<Dish> addDish(
            @PathVariable Integer userId,
            @Valid @RequestBody DishDto dishDto
    ) {
        return dishService.addDish(userId, dishDto);
    }

    @GetMapping("/dishes")
    @Operation(
            summary = "Получить все блюда",
            description = "Извлекает список всех блюд")
    public ResponseEntity<List<Dish>> getAllDishes() {
        List<Dish> dishes = dishService.getAllDishes();
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/dishes/{id}")
    @Operation(
            summary = "Получить блюдо по ID",
            description = "Извлекает блюдо с указанным ID")
    public ResponseEntity<Dish> getDishById(@PathVariable Integer id) {
        Dish dish = dishService.getDishById(id);
        return ResponseEntity.ok(dish);
    }

    @PutMapping("/dishes/{id}")
    @Operation(
            summary = "Обновить блюдо по ID",
            description = "Обновляет блюдо с указанным ID")
    public Dish updateDish(@PathVariable Integer id, @Valid @RequestBody DishDto dishDto) {
        return dishService.updateDish(id, dishDto);
    }

    @DeleteMapping("/dishes/{id}")
    @Operation(
            summary = "Удалить блюдо по ID",
            description = "Удаляет блюдо с указанным ID")
    public ResponseEntity<Void> deleteDish(@PathVariable Integer id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-product")
    @Operation(
            summary = "Найти блюда по имени продукта",
            description = "Извлекает все блюда с указанным продуктом")
    public ResponseEntity<List<Dish>> findDishesByProductNameNative(
            @RequestParam String productName
    ) {
        return ResponseEntity.ok(dishService.findDishesByProductNameNative(productName));
    }
}
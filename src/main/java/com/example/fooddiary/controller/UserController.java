package com.example.fooddiary.controller;

import com.example.fooddiary.dto.UserDto;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.User;
import com.example.fooddiary.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @Operation(
            summary = "Добавить нового пользователя",
            description = "Добавляет пользователя с указанными параметрами")
    public User addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/users")
    @Operation(
            summary = "Получить всех пользователей",
            description = "Извлекает список всех пользователей")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Извлекает пользователя с указанным ID")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}")
    @Operation(
            summary = "Обновить пользователя по ID",
            description = "Обновляет пользователя с указанным ID")
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/users/{id}")
    @Operation(
            summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя с указанным ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/dishes")
    @Operation(
            summary = "Получить все блюда пользователя",
            description = "Извлекает все блюда пользователя с указанным ID")
    public ResponseEntity<List<Dish>> getUserDishes(@PathVariable Integer userId) {
        return userService.getUserDishes(userId);
    }

    @GetMapping("/users/{userId}/dishes/{dishId}")
    @Operation(
            summary = "Получить блюда пользователя по ID",
            description = "Извлекает блюдо с указанным ID пользователя с указанным ID")
    public ResponseEntity<Dish> getUserDishById(
            @PathVariable Integer userId,
            @PathVariable Integer dishId
    ) {
        return userService.getUserDishById(userId, dishId);
    }
}

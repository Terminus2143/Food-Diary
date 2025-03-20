package com.example.fooddiary.controller;

import com.example.fooddiary.dto.UserDto;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.User;
import com.example.fooddiary.service.UserService;
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
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/dishes")
    public ResponseEntity<List<User>> getAllUsersWithDishes() {
        return userService.getAllUsersWithDishes();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/dishes")
    public ResponseEntity<List<Dish>> getUserDishes(@PathVariable Integer userId) {
        return userService.getUserDishes(userId);
    }

    @GetMapping("/users/{userId}/dishes/{dishId}")
    public ResponseEntity<Dish> getUserDishById(
            @PathVariable Integer userId,
            @PathVariable Integer dishId
    ) {
        return userService.getUserDishById(userId, dishId);
    }
}

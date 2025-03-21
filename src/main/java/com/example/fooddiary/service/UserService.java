package com.example.fooddiary.service;

import com.example.fooddiary.cache.UserCache;
import com.example.fooddiary.dto.UserDto;
import com.example.fooddiary.exception.AlreadyExistsException;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.UserMapper;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.User;
import com.example.fooddiary.repository.DishRepository;
import com.example.fooddiary.repository.UserRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private static final String
            USER_NOT_FOUND_MESSAGE_ID = "Пользователь с ID %d не найден";
    private static final String
            USER_EXISTENCE_MESSAGE = "Пользователь с таким именем или почтой уже существует";
    private static final String
            DISH_NOT_FOUND_MESSAGE_ID = "Блюдо с ID %d не найдено";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DishRepository dishRepository;
    private final UserCache userCache;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            DishRepository dishRepository,
            UserCache userCache) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.dishRepository = dishRepository;
        this.userCache = userCache;
    }

    public User addUser(@RequestBody UserDto userDto) {
        try {
            User user = userMapper.toEntity(userDto);
            User savedUser = userRepository.save(user);
            userCache.put(savedUser.getId().longValue(), savedUser);
            return savedUser;
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(USER_EXISTENCE_MESSAGE);
        }
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<List<User>> getAllUsersWithDishes() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.getDishes().forEach(dish -> {}));
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<User> getUserById(Integer id) {
        User cachedUser = userCache.get(id.longValue());
        if (cachedUser != null) {
            return ResponseEntity.ok(cachedUser);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE_ID, id)
                ));

        userCache.put(user.getId().longValue(), user);
        return ResponseEntity.ok(user);
    }

    public User updateUser(Integer id, @RequestBody UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE_ID, id)
                ));

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getAge() > 0) {
            user.setAge(userDto.getAge());
        }
        if (userDto.getHeight() > 0) {
            user.setHeight(userDto.getHeight());
        }
        if (userDto.getWeight() > 0) {
            user.setWeight(userDto.getWeight());
        }
        if (userDto.getGender() != null) {
            user.setGender(userDto.getGender());
        }

        try {
            User updatedUser = userRepository.save(user);
            userCache.put(updatedUser.getId().longValue(), updatedUser);
            return updatedUser;
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(USER_EXISTENCE_MESSAGE);
        }
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(
                    String.format(USER_NOT_FOUND_MESSAGE_ID, id)
            );
        }
        userRepository.deleteById(id);
        userCache.remove(id.longValue());
    }

    public ResponseEntity<List<Dish>> getUserDishes(Integer userId) {
        List<Dish> dishes = dishRepository.findByUserId(userId);
        return ResponseEntity.ok(dishes);
    }

    public ResponseEntity<Dish> getUserDishById(Integer userId, Integer dishId) {
        Dish dish = dishRepository.findByIdAndUserId(dishId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_ID, dishId)
                ));
        return ResponseEntity.ok(dish);
    }
}

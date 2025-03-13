package com.example.fooddiary.service;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.DishMapper;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.User;
import com.example.fooddiary.repository.DishRepository;
import java.util.List;
import java.util.Optional;
import com.example.fooddiary.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DishService {

    private static final String
            USER_NOT_FOUND_MESSAGE_ID = "Пользователь с ID %d не найден";
    private static final String
            DISH_NOT_FOUND_MESSAGE_ID = "Блюдо с ID %d не найдено";
    private static final String
            DISH_NOT_FOUND_MESSAGE_NAME = "Блюдо с именем %s не найдено";

    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final DishMapper dishMapper;

    public DishService(DishRepository dishRepository,UserRepository userRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.dishMapper = dishMapper;
    }

    public Dish addDish(@RequestBody DishDto dishDto) {
        Optional<User> optionalUser = userRepository.findById(dishDto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(
                    String.format(USER_NOT_FOUND_MESSAGE_ID, dishDto.getUserId())
            );
        }
        User user = optionalUser.get();
        Dish dish = dishMapper.toEntity(dishDto);
        dish.setUser(user);
        return dishRepository.save(dish);
    }


    public ResponseEntity<List<DishDto>> getAllDishes() {
        List<DishDto> dishesDto = dishRepository.findAll().stream()
                .map(dish -> {
                    DishDto dishDto = dishMapper.toDto(dish);
                    dishDto.setUserId(dish.getUser().getId());
                    return dishDto;
                })
                .toList();
        return ResponseEntity.ok(dishesDto);
    }

    public ResponseEntity<DishDto> getDishById(Integer id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_ID, id)
                ));
        DishDto dishDto = dishMapper.toDto(dish);
        dishDto.setUserId(dish.getUser().getId());
        return ResponseEntity.ok(dishDto);
    }

    public ResponseEntity<DishDto> getDishByName(String name) {
        Dish dish = dishRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_NAME, name)
                ));
        DishDto dishDto = dishMapper.toDto(dish);
        dishDto.setUserId(dish.getUser().getId());
        return ResponseEntity.ok(dishDto);
    }

    public Dish updateDish(Integer id, @RequestBody DishDto dishDto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_ID, id)
                ));

        if (dishDto.getName() != null) {
            dish.setName(dishDto.getName());
        }
        if (dishDto.getProteins() != null) {
            dish.setProteins(dishDto.getProteins());
        }
        if (dishDto.getFats() != null) {
            dish.setFats(dishDto.getFats());
        }
        if (dishDto.getCarbohydrates() != null) {
            dish.setCarbohydrates(dishDto.getCarbohydrates());
        }
        if (dishDto.getCalories() != null) {
            dish.setCalories(dishDto.getCalories());
        }
        return dishRepository.save(dish);
    }


    public void deleteDish(Integer id) {
        if (!dishRepository.existsById(id)) {
            throw new NotFoundException(
                    String.format(DISH_NOT_FOUND_MESSAGE_ID, id)
            );
        }
        dishRepository.deleteById(id);
    }
}

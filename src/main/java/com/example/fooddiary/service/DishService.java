package com.example.fooddiary.service;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.exception.AlreadyExistsException;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.DishMapper;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.repository.DishRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DishService {

    private static final String
            DISH_EXISTENCE_MESSAGE = "Блюдо с именем %s уже существует";
    private static final String
            DISHES_NOT_FOUND = "Блюд не обнаружено";
    private static final String
            DISH_NOT_FOUND_MESSAGE_ID = "Блюдо с ID %d не найдено";
    private static final String
            DISH_NOT_FOUND_MESSAGE_NAME = "Блюдо с именем %s не найдено";

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    public DishService(DishRepository dishRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.dishMapper = dishMapper;
    }

    public Dish addDish(@RequestBody DishDto dishDto) {
        try {
            Dish dish = dishMapper.toEntity(dishDto);
            return dishRepository.save(dish);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(
                    String.format(DISH_EXISTENCE_MESSAGE, dishDto.getName())
            );
        }
    }

    public ResponseEntity<List<Dish>> getAllDishs() {
        List<Dish> dishs = dishRepository.findAll();
        if (dishs.isEmpty()) {
            throw new NotFoundException(DISHES_NOT_FOUND);
        }
        return ResponseEntity.ok(dishs);
    }

    public ResponseEntity<Dish> getDishById(Integer id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_ID, id)
                ));
        return ResponseEntity.ok(dish);
    }

    public ResponseEntity<Dish> getDishByName(String name) {
        Dish dish = dishRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_NAME, name)
                ));
        return ResponseEntity.ok(dish);
    }

    public Dish updateDish(Integer id, @RequestBody DishDto dishDto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_ID, id)
                ));

        if (dishDto.getName() != null) {
            dish.setName(dishDto.getName());
        }
        try {
            return dishRepository.save(dish);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(
                    String.format(DISH_EXISTENCE_MESSAGE, dishDto.getName())
            );
        }
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

package com.example.fooddiary.service;

import com.example.fooddiary.cache.DishCache;
import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.Product;
import com.example.fooddiary.model.User;
import com.example.fooddiary.repository.DishRepository;
import com.example.fooddiary.repository.ProductRepository;
import com.example.fooddiary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DishCache dishCache;
    @InjectMocks
    private DishService dishService;

    private Dish dish;
    private DishDto dishDto;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);

        product = new Product();
        product.setId(1);
        product.setName("Яблоко");
        product.setProteins(0.4);
        product.setFats(0.4);
        product.setCarbohydrates(11.8);
        product.setCalories(52.0);

        dish = new Dish();
        dish.setId(1);
        dish.setName("Салат");
        dish.setUser(user);
        dish.setProducts(Set.of(product));

        dishDto = new DishDto();
        dishDto.setName("Салат");
        dishDto.setProductNames(List.of("Яблоко"));
        dishDto.setUserId(1);
    }

    @Test
    void addDish_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findByName("Яблоко")).thenReturn(Optional.of(product));
        when(dishRepository.save(any())).thenReturn(dish);

        ResponseEntity<Dish> response = dishService.addDish(1, dishDto);

        assertNotNull(response.getBody());
        assertEquals("Салат", response.getBody().getName());
        verify(dishCache).put(1L, dish);
    }

    @Test
    void addDish_userNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> dishService.addDish(1, dishDto));
    }

    @Test
    void addDish_productNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findByName("Яблоко")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> dishService.addDish(1, dishDto));
    }

    @Test
    void addDish_alreadyExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findByName("Яблоко")).thenReturn(Optional.of(product));
        when(dishRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> dishService.addDish(1, dishDto));
    }

    @Test
    void getAllDishes_success() {
        when(dishRepository.findAll()).thenReturn(List.of(dish));

        List<Dish> result = dishService.getAllDishes();

        assertEquals(1, result.size());
        assertEquals("Салат", result.get(0).getName());
    }

    @Test
    void getAllDishes_notFound() {
        when(dishRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> dishService.getAllDishes());
    }

    @Test
    void getDishById_fromCache() {
        when(dishCache.get(1L)).thenReturn(dish);

        Dish result = dishService.getDishById(1);

        assertEquals("Салат", result.getName());
        verify(dishRepository, never()).findById(any());
    }

    @Test
    void getDishById_fromDatabase() {
        when(dishCache.get(1L)).thenReturn(null);
        when(dishRepository.findById(1)).thenReturn(Optional.of(dish));

        Dish result = dishService.getDishById(1);

        assertEquals("Салат", result.getName());
        verify(dishCache).put(1L, dish);
    }

    @Test
    void getDishById_notFound() {
        when(dishCache.get(1L)).thenReturn(null);
        when(dishRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> dishService.getDishById(1));
    }

    @Test
    void updateDish_success() {
        DishDto updateDto = new DishDto();
        updateDto.setName("Обновленный салат");
        updateDto.setProductNames(List.of("Яблоко"));

        when(dishCache.get(1L)).thenReturn(dish);
        when(productRepository.findByName("Яблоко")).thenReturn(Optional.of(product));
        when(dishRepository.save(any())).thenReturn(dish);

        Dish result = dishService.updateDish(1, updateDto);

        assertEquals("Обновленный салат", result.getName());
        verify(dishCache).put(1L, dish);
    }

    @Test
    void updateDish_noProducts_exception() {
        DishDto updateDto = new DishDto();
        updateDto.setProductNames(Collections.emptyList());

        when(dishCache.get(1L)).thenReturn(dish);

        assertThrows(IllegalArgumentException.class, () -> dishService.updateDish(1, updateDto));
    }

    @Test
    void deleteDish_success() {
        doNothing().when(dishRepository).deleteById(1);
        doNothing().when(dishCache).remove(1L);

        dishService.deleteDish(1);

        verify(dishRepository).deleteById(1);
        verify(dishCache).remove(1L);
    }

    @Test
    void updateDishNutrients_calculatesCorrectly() {
        Product product2 = new Product();
        product2.setProteins(1.0);
        product2.setFats(1.0);
        product2.setCarbohydrates(20.0);
        product2.setCalories(100.0);

        Dish testDish = new Dish();
        testDish.setProducts(Set.of(product, product2));

        dishService.updateDishNutrients(testDish, testDish.getProducts());

        assertEquals(0.7, testDish.getProteins(), 0.01);
        assertEquals(0.7, testDish.getFats(), 0.01);
        assertEquals(15.9, testDish.getCarbohydrates(), 0.01);
        assertEquals(76.0, testDish.getCalories(), 0.01);
    }
}
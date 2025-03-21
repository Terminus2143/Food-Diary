package com.example.fooddiary.service;

import com.example.fooddiary.dto.DishDto;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.Product;
import com.example.fooddiary.model.User;
import com.example.fooddiary.repository.DishRepository;
import com.example.fooddiary.repository.ProductRepository;
import com.example.fooddiary.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
            PRODUCT_NOT_FOUND_MESSAGE = "Продукт с именем %s не найден";
    private static final String
            DISHES_NOT_FOUND_MESSAGE = "Блюда не найдены";
    private static final String
            DISHES_CONTAINMENT_MESSAGE = "Нет продуктов в блюде";

    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public DishService(
            DishRepository dishRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE_ID, userId)));
    }

    private Product findProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE, productName)));
    }

    private void updateDishNutrients(Dish dish, Set<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException(DISHES_CONTAINMENT_MESSAGE);
        }

        double totalProteins = products.stream().mapToDouble(Product::getProteins).sum();
        double totalFats = products.stream().mapToDouble(Product::getFats).sum();
        double totalCarbohydrates = products.stream().mapToDouble(Product::getCarbohydrates).sum();
        double totalCalories = products.stream().mapToDouble(Product::getCalories).sum();

        int productCount = products.size();
        dish.setProteins(totalProteins / productCount);
        dish.setFats(totalFats / productCount);
        dish.setCarbohydrates(totalCarbohydrates / productCount);
        dish.setCalories(totalCalories / productCount);
    }

    private void updateDishFromDto(Dish dish, DishDto dishDto) {
        Optional.ofNullable(dishDto.getName()).ifPresent(dish::setName);
        Optional.ofNullable(dishDto.getProteins()).ifPresent(dish::setProteins);
        Optional.ofNullable(dishDto.getFats()).ifPresent(dish::setFats);
        Optional.ofNullable(dishDto.getCarbohydrates()).ifPresent(dish::setCarbohydrates);
        Optional.ofNullable(dishDto.getCalories()).ifPresent(dish::setCalories);
    }

    public ResponseEntity<Dish> addDish(Integer userId, DishDto dishDto) {
        User user = findUserById(userId);

        Dish dish = new Dish();
        dish.setName(dishDto.getName());
        dish.setUser(user);

        Set<Product> products = new HashSet<>();
        for (String productName : dishDto.getProductNames()) {
            products.add(findProductByName(productName));
        }

        updateDishNutrients(dish, products);
        dish.setProducts(products);
        dish = dishRepository.save(dish);

        return ResponseEntity.ok(dish);
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        if (dishes.isEmpty()) {
            throw new NotFoundException(DISHES_NOT_FOUND_MESSAGE);
        }
        return dishes;
    }

    public Dish getDishById(Integer id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(DISH_NOT_FOUND_MESSAGE_ID, id)));
    }

    public Dish updateDish(Integer id, @RequestBody DishDto dishDto) {
        Dish dish = getDishById(id);
        updateDishFromDto(dish, dishDto);

        if (dishDto.getProductNames() != null) {
            Set<Product> products = new HashSet<>();
            for (String productName : dishDto.getProductNames()) {
                products.add(findProductByName(productName));
            }
            updateDishNutrients(dish, products);
            dish.setProducts(products);
        }

        return dishRepository.save(dish);
    }

    public void deleteDish(Integer dishId) {
        dishRepository.deleteById(dishId);
    }

    public List<Dish> getAllDishesByUserIdNative(Integer userId) {
        List<Dish> dishes = dishRepository.findAllByUserIdNative(userId);
        if (dishes.isEmpty()) {
            throw new NotFoundException(DISHES_NOT_FOUND_MESSAGE);
        }
        return dishes;
    }
}

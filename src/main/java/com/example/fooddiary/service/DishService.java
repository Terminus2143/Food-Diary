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

    public ResponseEntity<Dish> addDish(Integer userId, DishDto dishDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(
                    String.format(USER_NOT_FOUND_MESSAGE_ID, userId)
            );
        }

        Dish dish = new Dish();
        dish.setName(dishDto.getName());
        dish.setUser(optionalUser.get());

        Set<Product> products = new HashSet<>();
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbohydrates = 0.0;
        double totalCalories = 0.0;

        for (String productName : dishDto.getProductNames()) {
            Optional<Product> optionalProduct = productRepository.findByName(productName);
            if (optionalProduct.isEmpty()) {
                throw new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE, productName)
                );
            }
            Product product = optionalProduct.get();
            products.add(product);
            totalProteins += product.getProteins();
            totalFats += product.getFats();
            totalCarbohydrates += product.getCarbohydrates();
            totalCalories += product.getCalories();
        }

        if (products.isEmpty()) {
            throw new IllegalArgumentException(DISHES_CONTAINMENT_MESSAGE);
        }

        int productCount = products.size();
        dish.setProteins(totalProteins / productCount);
        dish.setFats(totalFats / productCount);
        dish.setCarbohydrates(totalCarbohydrates / productCount);
        dish.setCalories(totalCalories / productCount);

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

        if (dishDto.getProductNames() != null) {
            Set<Product> products = new HashSet<>();
            double totalProteins = 0;
            double totalFats = 0;
            double totalCarbohydrates = 0;
            double totalCalories = 0;

            for (String productName : dishDto.getProductNames()) {
                Optional<Product> optionalProduct = productRepository.findByName(productName);
                if (optionalProduct.isEmpty()) {
                    throw new NotFoundException(
                            String.format(PRODUCT_NOT_FOUND_MESSAGE, productName));
                }
                Product product = optionalProduct.get();
                products.add(product);
                totalProteins += product.getProteins();
                totalFats += product.getFats();
                totalCarbohydrates += product.getCarbohydrates();
                totalCalories += product.getCalories();
            }
            int productCount = products.size();
            if (productCount > 0) {
                dish.setProteins(totalProteins / productCount);
                dish.setFats(totalFats / productCount);
                dish.setCarbohydrates(totalCarbohydrates / productCount);
                dish.setCalories(totalCalories / productCount);
            }
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

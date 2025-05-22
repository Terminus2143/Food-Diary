package com.example.fooddiary.repository;

import com.example.fooddiary.model.Dish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findByUserId(Integer userId);

    Optional<Dish> findByIdAndUserId(Integer id, Integer userId);

    @Query("SELECT DISTINCT d FROM Dish d JOIN d.products p WHERE LOWER(TRIM(p.name)) "
            + "= LOWER(TRIM(:productName))")
    List<Dish> findDishesByProductName(@Param("productName") String productName);

    @Query(value = "SELECT d.* FROM dishes d "
            + "JOIN dish_products dp ON d.id = dp.dish_id "
            + "JOIN products p ON dp.product_id = p.id "
            + "WHERE p.name ILIKE :productName", nativeQuery = true)
    List<Dish> findDishesByProductNameNative(@Param("productName") String productName);
}

package com.example.fooddiary.repository;

import com.example.fooddiary.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p JOIN p.dishes d WHERE d.id = :dishId")
    List<Product> findByDishId(@Param("dishId") Integer dishId);

    Optional<Product> findByName(String productName);

    boolean existsByName(String name);
}

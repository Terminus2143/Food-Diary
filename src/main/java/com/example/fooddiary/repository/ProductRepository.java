package com.example.fooddiary.repository;

import com.example.fooddiary.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String productName);

    boolean existsByName(String name);
}

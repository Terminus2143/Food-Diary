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

    @Query("SELECT d FROM Dish d WHERE d.user.id = :userId")
    List<Dish> findAllByUserIdJpql(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM dishes WHERE user_id = :userId", nativeQuery = true)
    List<Dish> findAllByUserIdNative(@Param("userId") Integer userId);
}

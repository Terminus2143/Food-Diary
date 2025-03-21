package com.example.fooddiary.cache;

import com.example.fooddiary.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishCache extends LfuCache<Dish> {
    public DishCache() {
        super(3);
    }
}

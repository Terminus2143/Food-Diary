package com.example.fooddiary.cache;

import com.example.fooddiary.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductCache extends LfuCache<Product> {
    public ProductCache() {
        super(5);
    }
}

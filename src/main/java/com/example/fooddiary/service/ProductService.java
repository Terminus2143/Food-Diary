package com.example.fooddiary.service;

import com.example.fooddiary.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        // Добавляем несколько продуктов
        products.add(new Product(1, "Яблоко", 0.3, 0.2, 14.0));
        products.add(new Product(2, "Банан", 1.1, 0.3, 23.0));
        products.add(new Product(3, "Куриное филе", 23.0, 1.5, 0.0));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Optional<Product> getProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getProductName().equalsIgnoreCase(productName))
                .findFirst();
    }

    public Optional<Product> getProductById(int id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}
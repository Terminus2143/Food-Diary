package com.example.fooddiary.controller;

import com.example.fooddiary.exception.ProductNotFoundException;
import com.example.fooddiary.model.Product;
import com.example.fooddiary.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/food", produces = "application/json")
    public Product getProductByName(@RequestParam(name = "product") String productName) {
        return productService.getProductByName(productName)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Продукт с названием '" + productName + "' не найден"));
    }

    @GetMapping(value = "/products/{id}", produces = "application/json")
    public Product getProductById(@PathVariable("id") int id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Продукт с ID " + id + " не найден"));
    }

    @GetMapping(value = "/products", produces = "application/json")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
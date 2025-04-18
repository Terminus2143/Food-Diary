package com.example.fooddiary.controller;

import com.example.fooddiary.dto.ProductDto;
import com.example.fooddiary.model.Product;
import com.example.fooddiary.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
@Tag(name = "Продукты", description = "Управление продуктами")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    @Operation(
            summary = "Добавить продукт",
            description = "Добавляет продукт с указанными параметрами")
    public Product addProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    @PostMapping("/products/bulk")
    @Operation(
            summary = "Добавить продукты",
            description = "Добавляет продукты с указанными параметрами")
    public List<Product> addProducts(@Valid @RequestBody List<ProductDto> productsDto) {
        return productService.addProducts(productsDto);
    }

    @GetMapping("/products")
    @Operation(
            summary = "Получить все продукты",
            description = "Извлекает список всех продуктов")
    public ResponseEntity<List<Product>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    @Operation(
            summary = "Получить продукт по ID",
            description = "Извлекает продукт с указанным ID")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @GetMapping("/product")
    @Operation(
            summary = "Получить продукт по имени",
            description = "Извлекает продукт с указанным именем")
    public ResponseEntity<Product> getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }

    @PutMapping("/products/{id}")
    @Operation(
            summary = "Обновить продукт по ID",
            description = "Обновляет продукт с указанным ID")
    public Product updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/products/{id}")
    @Operation(
            summary = "Удалить продукт по ID",
            description = "Удаляет продукт с указанным ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
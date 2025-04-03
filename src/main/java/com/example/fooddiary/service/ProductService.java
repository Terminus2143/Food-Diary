package com.example.fooddiary.service;

import com.example.fooddiary.cache.DishCache;
import com.example.fooddiary.cache.ProductCache;
import com.example.fooddiary.dto.ProductDto;
import com.example.fooddiary.exception.AlreadyExistsException;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.ProductMapper;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.Product;
import com.example.fooddiary.repository.DishRepository;
import com.example.fooddiary.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProductService {

    private static final String
            PRODUCT_EXISTENCE_MESSAGE = "Продукт с именем %s уже существует";
    private static final String
            PRODUCT_NOT_FOUND_MESSAGE_ID = "Продукт с ID %d не найден";
    private static final String
            PRODUCT_NOT_FOUND_MESSAGE_NAME = "Продукт с именем %s не найден";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final DishRepository dishRepository;
    private final ProductCache productCache;
    private final DishCache dishCache;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            DishRepository dishRepository,
            ProductCache productCache,
            DishCache dishCache
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.dishRepository = dishRepository;
        this.productCache = productCache;
        this.dishCache = dishCache;
    }

    public Product addProduct(@RequestBody ProductDto productDto) {
        try {
            Product product = productMapper.toEntity(productDto);
            product = productRepository.save(product);
            productCache.put((long) product.getId(), product);
            return product;
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(
                    String.format(PRODUCT_EXISTENCE_MESSAGE, productDto.getName())
            );
        }
    }

    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<Product> getProductById(Integer id) {
        Product product = productCache.get((long) id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE_ID, id)
                ));
        productCache.put((long) id, product);
        return ResponseEntity.ok(product);
    }

    public ResponseEntity<Product> getProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE_NAME, name)
                ));
        productCache.put((long) product.getId(), product);
        return ResponseEntity.ok(product);
    }

    public Product updateProduct(Integer id, @RequestBody ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE_ID, id)
                ));

        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getProteins() != null) {
            product.setProteins(productDto.getProteins());
        }
        if (productDto.getFats() != null) {
            product.setFats(productDto.getFats());
        }
        if (productDto.getCarbohydrates() != null) {
            product.setCarbohydrates(productDto.getCarbohydrates());
        }
        if (productDto.getCalories() != null) {
            product.setCalories(productDto.getCalories());
        }
        try {
            product = productRepository.save(product);
            productCache.put((long) id, product);
            return product;
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(
                    String.format(PRODUCT_EXISTENCE_MESSAGE, productDto.getName())
            );
        }
    }

    public void deleteProduct(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Dish> dishes = new ArrayList<>(product.getDishes());

            for (Dish dish : dishes) {
                dishCache.remove((long) dish.getId());
            }
            dishRepository.deleteAll(dishes);

            productCache.remove((long) productId);
            productRepository.delete(product);
        } else {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MESSAGE_ID + productId);
        }
    }
}

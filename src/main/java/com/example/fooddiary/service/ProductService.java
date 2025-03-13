package com.example.fooddiary.service;

import com.example.fooddiary.dto.ProductDto;
import com.example.fooddiary.exception.AlreadyExistsException;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.ProductMapper;
import com.example.fooddiary.model.Product;
import com.example.fooddiary.repository.ProductRepository;
import java.util.List;
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

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Product addProduct(@RequestBody ProductDto productDto) {
        try {
            Product product = productMapper.toEntity(productDto);
            return productRepository.save(product);
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
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE_ID, id)
                ));
        return ResponseEntity.ok(product);
    }

    public ResponseEntity<Product> getProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format(PRODUCT_NOT_FOUND_MESSAGE_NAME, name)
                ));
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
            return productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(
                    String.format(PRODUCT_EXISTENCE_MESSAGE, productDto.getName())
            );
        }
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException(
                    String.format(PRODUCT_NOT_FOUND_MESSAGE_ID, id)
            );
        }
        productRepository.deleteById(id);
    }
}

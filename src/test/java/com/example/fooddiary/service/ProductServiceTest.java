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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private ProductCache productCache;
    @Mock
    private DishCache dishCache;
    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private ProductDto updatedProductDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Apple");
        product.setProteins(0.3);
        product.setFats(0.2);
        product.setCarbohydrates(14.0);
        product.setCalories(52.0);

        productDto = new ProductDto();
        productDto.setName("Apple");
        productDto.setProteins(0.3);
        productDto.setFats(0.2);
        productDto.setCarbohydrates(14.0);
        productDto.setCalories(52.0);

        updatedProductDto = new ProductDto();
        updatedProductDto.setName("Green Apple");
        updatedProductDto.setProteins(0.4);
        updatedProductDto.setFats(0.1);
        updatedProductDto.setCarbohydrates(13.0);
        updatedProductDto.setCalories(50.0);
    }

    @Test
    void getAllProducts_success() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        ResponseEntity<List<Product>> response = productService.getAllProducts();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Apple", response.getBody().get(0).getName());
    }

    @Test
    void getAllProducts_emptyList() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Product>> response = productService.getAllProducts();

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void addProduct_success() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(productDto);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
        verify(productCache).put(1L, product);
    }

    @Test
    void addProduct_alreadyExists() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> productService.addProduct(productDto));
    }

    @Test
    void addProducts_success() {
        List<ProductDto> productDtos = List.of(productDto);
        when(productRepository.existsByName("Apple")).thenReturn(false);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        List<Product> result = productService.addProducts(productDtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
        verify(productCache).put(1L, product);
    }

    @Test
    void addProducts_withDuplicateNamesInRequest() {
        List<ProductDto> productDtos = List.of(productDto, productDto);

        assertThrows(IllegalArgumentException.class, () -> productService.addProducts(productDtos));
    }
    @Test
    void addProducts_withExistingNamesInDatabase() {
        when(productRepository.existsByName("Apple")).thenReturn(true);
        List<ProductDto> productsToAdd = List.of(productDto);
        String expectedMessage = "Продукты с именами [Apple] уже существуют";

        AlreadyExistsException exception = assertThrows(
                AlreadyExistsException.class,
                () -> productService.addProducts(productsToAdd)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, never()).saveAll(anyList());
    }

    @Test
    void addProducts_databaseError() {
        when(productRepository.existsByName("Apple")).thenReturn(false);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.saveAll(anyList())).thenThrow(DataIntegrityViolationException.class);

        List<ProductDto> input = List.of(productDto);

        assertThrows(AlreadyExistsException.class, () -> productService.addProducts(input));
    }

    @Test
    void getProductById_fromCache() {
        when(productCache.get(1L)).thenReturn(product);

        ResponseEntity<Product> response = productService.getProductById(1);

        assertNotNull(response.getBody());
        assertEquals("Apple", response.getBody().getName());
        verify(productRepository, never()).findById(any());
    }

    @Test
    void getProductById_fromDatabase() {
        when(productCache.get(1L)).thenReturn(null);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productService.getProductById(1);

        assertNotNull(response.getBody());
        assertEquals("Apple", response.getBody().getName());
        verify(productCache).put(1L, product);
    }

    @Test
    void getProductById_notFound() {
        when(productCache.get(1L)).thenReturn(null);
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    void getProductByName_success() {
        when(productRepository.findByName("Apple")).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productService.getProductByName("Apple");

        assertNotNull(response.getBody());
        assertEquals("Apple", response.getBody().getName());
        verify(productCache).put(1L, product);
    }

    @Test
    void getProductByName_notFound() {
        when(productRepository.findByName("Apple")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductByName("Apple"));
    }

    @Test
    void updateProduct_success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        Product result = productService.updateProduct(1, updatedProductDto);

        assertNotNull(result);
        assertEquals("Green Apple", result.getName());
        assertEquals(0.4, result.getProteins());
        assertEquals(0.1, result.getFats());
        assertEquals(13.0, result.getCarbohydrates());
        assertEquals(50.0, result.getCalories());
        verify(productCache).put(1L, product);
    }

    @Test
    void updateProduct_partialUpdate() {
        ProductDto partialUpdate = new ProductDto();
        partialUpdate.setName("New Apple");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        Product result = productService.updateProduct(1, partialUpdate);

        assertNotNull(result);
        assertEquals("New Apple", result.getName());
        // Остальные поля должны остаться без изменений
        assertEquals(0.3, result.getProteins());
        assertEquals(0.2, result.getFats());
        assertEquals(14.0, result.getCarbohydrates());
        assertEquals(52.0, result.getCalories());
    }

    @Test
    void updateProduct_notFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(1, updatedProductDto));
    }

    @Test
    void updateProduct_alreadyExists() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> productService.updateProduct(1, updatedProductDto));
    }

    @Test
    void deleteProduct_success() {
        Dish dish = new Dish();
        dish.setId(2);
        product.setDishes(List.of(dish));

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        verify(dishCache).remove(2L);
        verify(dishRepository).deleteAll(List.of(dish));
        verify(productCache).remove(1L);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1));

        verify(productCache, never()).remove(any());
        verify(productRepository, never()).delete(any());
    }
}
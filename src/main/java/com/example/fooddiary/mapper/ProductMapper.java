package com.example.fooddiary.mapper;

import com.example.fooddiary.dto.ProductDto;
import com.example.fooddiary.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setProteins(product.getProteins());
        productDto.setFats(product.getFats());
        productDto.setCarbohydrates(product.getCarbohydrates());
        productDto.setCalories(product.getCalories());
        return productDto;
    }

    public Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setProteins(productDto.getProteins());
        product.setFats(productDto.getFats());
        product.setCarbohydrates(productDto.getCarbohydrates());
        product.setCalories(productDto.getCalories());
        return product;
    }
}

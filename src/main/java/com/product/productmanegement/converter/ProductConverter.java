package com.product.productmanegement.converter;

import com.product.productmanegement.domain.Category;
import com.product.productmanegement.domain.Product;
import com.product.productmanegement.dtos.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategoryId());
        dto.setActive(product.isActive());
        return dto;
    }

    public Product mapToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategoryId(dto.getCategoryId());
        product.setActive(dto.isActive());
        return product;
    }
}

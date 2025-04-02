package com.product.productmanegement.service;

import com.product.productmanegement.dtos.ProductDTO;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    Page<ProductDTO> getAllProducts(int page, int size);

    List<ProductDTO> getProductsByCategory(Long categoryId);

    Page<ProductDTO> getProductsByCategory(Long categoryId, int page, int size);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    Page<ProductDTO> searchProducts(String keyword, int page, int size);
}

package com.product.productmanegement.service.impl;

import com.product.productmanegement.converter.ProductConverter;
import com.product.productmanegement.domain.Category;
import com.product.productmanegement.domain.Product;
import com.product.productmanegement.dtos.ProductDTO;
import com.product.productmanegement.repository.CategoryRepository;
import com.product.productmanegement.repository.ProductRepository;
import com.product.productmanegement.service.CategoryService;
import com.product.productmanegement.service.ProductService;
import com.product.productmanegement.utils.exceptionHandlers.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConverter productConverter;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Verify category exists
        Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = productConverter.mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);

        ProductDTO savedProductDTO = productConverter.mapToDTO(savedProduct);
        savedProductDTO.setCategoryName(category.getName());

        return savedProductDTO;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

        ProductDTO productDTO = productConverter.mapToDTO(product);

        // Add category name
        categoryRepository.findById(product.getCategoryId())
            .ifPresent(category -> productDTO.setCategoryName(category.getName()));

        return productDTO;
    }

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOs = productPage.getContent().stream()
            .map(product -> productConverter.mapToDTO(product))
            .map(this::enrichWithCategoryName)
            .collect(Collectors.toList());

        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
            .map(product -> productConverter.mapToDTO(product))
            .map(this::enrichWithCategoryName)
            .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, int page, int size) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

        List<ProductDTO> productDTOs = productPage.getContent().stream()
            .map(product -> productConverter.mapToDTO(product))
            .map(this::enrichWithCategoryName)
            .collect(Collectors.toList());

        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

        // Verify category exists if changed
        if (!existingProduct.getCategoryId().equals(productDTO.getCategoryId())) {
            categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCategoryId(productDTO.getCategoryId());
        existingProduct.setActive(productDTO.isActive());

        Product updatedProduct = productRepository.save(existingProduct);

        ProductDTO updatedProductDTO = productConverter.mapToDTO(updatedProduct);

        // Add category name
        categoryRepository.findById(updatedProduct.getCategoryId())
            .ifPresent(category -> updatedProductDTO.setCategoryName(category.getName()));

        return updatedProductDTO;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductDTO> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage =
            productRepository.findByNameContainingIgnoreCase(keyword, pageable);

        List<ProductDTO> productDTOs = productPage.getContent().stream()
            .map(product -> productConverter.mapToDTO(product))
            .map(this::enrichWithCategoryName)
            .collect(Collectors.toList());
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    private ProductDTO enrichWithCategoryName(ProductDTO productDTO) {
        categoryRepository.findById(productDTO.getCategoryId())
            .ifPresent(category -> productDTO.setCategoryName(category.getName()));
        return productDTO;
    }
}

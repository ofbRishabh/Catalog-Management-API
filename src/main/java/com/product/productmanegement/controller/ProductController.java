package com.product.productmanegement.controller;

import com.product.productmanegement.dtos.ProductDTO;
import com.product.productmanegement.service.ProductService;
import com.product.productmanegement.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
        @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Product created", createdProduct),
            HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> pagedProducts = productService.getAllProducts(page, size);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Products fetched", pagedProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Product found", product));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProductsByCategory(
        @PathVariable Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> pagedProducts =
            productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Products by category fetched",
                pagedProducts));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> searchProducts(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> pagedProducts = productService.searchProducts(keyword, page, size);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Products searched", pagedProducts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Product updated", updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Product deleted", null));
    }
}

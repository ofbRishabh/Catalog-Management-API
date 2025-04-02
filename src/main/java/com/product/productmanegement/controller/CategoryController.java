package com.product.productmanegement.controller;

import com.product.productmanegement.dtos.CategoryDTO;
import com.product.productmanegement.service.CategoryService;
import com.product.productmanegement.utils.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category Management", description = "API endpoints for category management")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
        @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Category created", createdCategory),
            HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Categories fetched", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Category found", category));
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getChildCategories(
        @PathVariable Long parentId) {
        List<CategoryDTO> childCategories = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Child categories fetched", childCategories));
    }

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getCategoryTree() {
        List<CategoryDTO> categoryTree = categoryService.getCategoryTree();
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Category tree fetched", categoryTree));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Category updated", updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Category deleted", null));
    }
}

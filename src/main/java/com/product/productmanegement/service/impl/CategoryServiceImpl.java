package com.product.productmanegement.service.impl;

import com.product.productmanegement.converter.CategoryConverter;
import com.product.productmanegement.domain.Category;
import com.product.productmanegement.dtos.CategoryDTO;
import com.product.productmanegement.utils.exceptionHandlers.exceptions.NotFoundException;
import com.product.productmanegement.repository.CategoryRepository;
import com.product.productmanegement.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired CategoryConverter categoryConverter;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryConverter.mapToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryConverter.mapToDTO(savedCategory);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        return categoryConverter.mapToDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(category -> categoryConverter.mapToDTO(category))
            .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getChildCategories(Long parentId) {  // Changed from Integer to Long
        List<Category> children = categoryRepository.findByParentId(parentId);
        return children.stream()
            .map(category -> categoryConverter.mapToDTO(category))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setActive(categoryDTO.isActive());
        existingCategory.setParentId(categoryDTO.getParentId());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryConverter.mapToDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category not found with id: " + id);
        }
        List<Category> children = categoryRepository.findByParentId(id);

        for (Category child : children) {
            deleteCategory(child.getId());
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, List<Category>> categoryMap = new HashMap<>();

        List<Category> rootCategories = new ArrayList<>();

        for (Category category : allCategories) {
            if (category.getParentId() == null) {
                rootCategories.add(category);
            } else {
                categoryMap.computeIfAbsent(category.getParentId(), k -> new ArrayList<>())
                    .add(category);
            }
        }

        return rootCategories.stream()
            .map(root -> categoryConverter.buildCategoryTree(root, categoryMap))
            .collect(Collectors.toList());
    }
}

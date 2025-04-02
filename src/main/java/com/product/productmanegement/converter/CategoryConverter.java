package com.product.productmanegement.converter;

import com.product.productmanegement.domain.Category;
import com.product.productmanegement.dtos.CategoryDTO;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryDTO buildCategoryTree(Category category, Map<Long, List<Category>> categoryMap) {
        CategoryDTO dto = mapToDTO(category);

        List<Category> children =
            categoryMap.getOrDefault(category.getId(), Collections.emptyList());
        if (!children.isEmpty()) {
            dto.setChildren(
                children.stream()
                    .map(child -> buildCategoryTree(child, categoryMap))
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public CategoryDTO mapToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName(dto.getName()));
        dto.setParentId(category.getParentId());
        dto.setDescription(category.getDescription());
        dto.setActive(category.isActive());
        return dto;
    }

    public Category mapToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setParentId(dto.getParentId());
        category.setDescription(dto.getDescription());
        category.setActive(dto.isActive());
        return category;
    }
}

package com.cooperation.ecom.service;

import com.cooperation.ecom.domain.request.SaveCategoryRequest;
import com.cooperation.ecom.domain.request.UpdateCategoryRequest;
import com.cooperation.ecom.domain.response.CategoryResponse;
import com.cooperation.ecom.entity.Category;
import com.cooperation.ecom.exception.ResourceNotFoundException;
import com.cooperation.ecom.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategory() {
        List<CategoryResponse> categoryResponseList;
        try {
            categoryResponseList = categoryRepository.findAll().stream().map(x -> new CategoryResponse(x.getId(), x.getName())).toList();
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        return categoryResponseList;
    }

    public Category saveCategory(SaveCategoryRequest request) {
        Category category;
        String createAuthor = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            category = new Category(request.getName(), createAuthor, new Date());
            category = categoryRepository.save(category);
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        return category;
    }

    public Category updateCategory(UpdateCategoryRequest request) {
        Category category;
        String updateAuthor = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            category = categoryRepository.findById(request.getId()).orElse(null);
            if (category != null) {
                category.setName(request.getName());
                category.setUpdateAuthor(updateAuthor);
                category.setUpdateDate(new Date());
                category = categoryRepository.save(category);
            } else {
                throw new ResourceNotFoundException("Category is null.");
            }
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        return category;
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.findById(id).ifPresent(category -> categoryRepository.delete(category));
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }

}

package com.schoolProject.ecommerceApplication.service.interf;


import com.schoolProject.ecommerceApplication.dto.CategoryDto;
import com.schoolProject.ecommerceApplication.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDto categoryRequest);

    Response updateCategory(Long categoryId, CategoryDto categoryRequest);

    Response getAllCategories();

    Response getCategoryId(Long categoryId);

    Response deleteCategory(Long categoryId);
}

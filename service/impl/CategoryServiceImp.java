package com.schoolProject.ecommerceApplication.service.impl;

import com.schoolProject.ecommerceApplication.dto.CategoryDto;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.entity.Category;
import com.schoolProject.ecommerceApplication.exception.NotFoundException;
import com.schoolProject.ecommerceApplication.mapper.EntityDtoMapper;
import com.schoolProject.ecommerceApplication.repository.CategoryRepo;
import com.schoolProject.ecommerceApplication.service.interf.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createCategory(CategoryDto categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepo.save(category);
        return Response.builder()
                .status(200)
                .message("Category created successfully")
                .build();
    }

    @Override
    public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
       Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
       category.setName(categoryRequest.getName());
       categoryRepo.save(category);

       return Response.builder()
               .status(200)
               .message("Category updated successfully")
               .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(entityDtoMapper::mapCategoryToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .categoryList(categoryDtos)
                .build();
    }

    @Override
    public Response getCategoryId(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToDtoBasic(category);


        return Response.builder()
                .status(200)
                .category(categoryDto)
                .build();
    }

    @Override
    public Response deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
        categoryRepo.delete(category);

        return Response.builder()
                .status(200)
                .message("category is successfully deleted")
                .build();
    }
}

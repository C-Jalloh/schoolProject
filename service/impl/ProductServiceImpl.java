package com.schoolProject.ecommerceApplication.service.impl;

import com.schoolProject.ecommerceApplication.dto.ProductDto;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.entity.Category;
import com.schoolProject.ecommerceApplication.entity.Product;
import com.schoolProject.ecommerceApplication.exception.NotFoundException;
import com.schoolProject.ecommerceApplication.mapper.EntityDtoMapper;
import com.schoolProject.ecommerceApplication.repository.CategoryRepo;
import com.schoolProject.ecommerceApplication.repository.ProductRepo;
//import com.schoolProject.ecommerceApplication.service.AwsS3Service;
import com.schoolProject.ecommerceApplication.service.interf.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;
//    private final AwsS3Service awsS3Service;

    @Override
    public Response createProduct(Long categoryId, String image, String name, String description, BigDecimal price) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new NotFoundException("category not found"));
//        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product  = new Product();
        product.setCategory(category); 
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
//        product.setImageUrl(productImageUrl);
        product.setImageUrl(image);

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("product created successfully")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, String image, String name, String description, BigDecimal price){

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product not found"));

       Category category = null;
        String productImageUrl = null;

         if (categoryId != null){
             category = categoryRepo.findById(categoryId)
                     .orElseThrow(()-> new NotFoundException("Category not found"));
         }
//         if (image != null && image.isEmpty()){
//             productImageUrl = awsS3Service.saveImageToS3(image);
//         }

         if (category != null) product.setCategory(category);
        if (name != null) product.setName(name);
        if (description != null) product.setDescription(description);
        if (price != null) product.setPrice(price);
        if (productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("product updated  successfully")
                .build();


    }

    @Override
    public Response deleteProduct(Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product not found"));

        productRepo.deleteById(product.getId());

        return Response.builder()
                .status(200)
                .message("product deleted  successfully")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product not found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);
        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC,"id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productList)
                .build();
    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if (products.isEmpty()){
            throw new NotFoundException("No product found by this category");
        }

        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());


        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProduct(String searchValue) {
        // I changed this to call the new method name in the productRepo
        List<Product> products = productRepo.findByNameContainingAndDescriptionContaining(searchValue, searchValue);

        if (products.isEmpty()){
            throw new NotFoundException("No product found");
        }

        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());


        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }
}

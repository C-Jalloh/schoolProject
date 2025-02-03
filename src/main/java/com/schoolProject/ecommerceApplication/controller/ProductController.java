package com.schoolProject.ecommerceApplication.controller;

import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.entity.Product;
import com.schoolProject.ecommerceApplication.exception.InvalidCredentialException;
import com.schoolProject.ecommerceApplication.service.interf.OrderItemService;
import com.schoolProject.ecommerceApplication.service.interf.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createProduct(@RequestBody Product product

            ){
        if (product.getCategory().getId() <= 0 || product.getImageUrl().isEmpty() || product.getName().isEmpty() || product.getDescription().isEmpty() || product.getPrice() == null){
            throw new InvalidCredentialException("All Fields are required");
        }
        return ResponseEntity.ok(productService.createProduct(product.getCategory().getId(),product.getImageUrl(),product.getName(),product.getDescription(), product.getPrice()));

    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(@RequestBody Product product

    ){

        return ResponseEntity.ok(productService.updateProduct(product.getId(),product.getCategory().getId(),product.getImageUrl(),product.getName(),product.getDescription(), product.getPrice()));

    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping("/get-all-product/{productId}")
    public ResponseEntity<Response> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //consider changing the url name below. I added an extra y to by because it had the same name with the
    // one below it which was causing error
    @GetMapping("/get-byy-category-id/{categoryId}")
    public ResponseEntity<Response> getProductByCategory(@PathVariable Long categoryId ) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/get-by-category-id/{categoryId}")
    public ResponseEntity<Response> searchProduct(@PathVariable String searchValue ) {
        return ResponseEntity.ok(productService.searchProduct(searchValue));
    }
}

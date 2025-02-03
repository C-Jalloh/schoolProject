package com.schoolProject.ecommerceApplication.repository;

import com.schoolProject.ecommerceApplication.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// You forgot to add this annotation
@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
}

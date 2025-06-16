package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventura.entities.ProductCategories;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategories, Integer> {

}

package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

}

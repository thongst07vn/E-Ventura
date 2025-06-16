package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

@Repository
public interface VendorRepository extends JpaRepository<Vendors, Integer> {

}

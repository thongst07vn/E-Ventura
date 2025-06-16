package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Orders;
import com.eventura.entities.Products;
import com.eventura.entities.Users;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

}

package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {

}

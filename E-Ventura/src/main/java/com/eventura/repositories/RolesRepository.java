package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Roles;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

}

package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Provinces;

@Repository
public interface ProvinceRepository extends JpaRepository<Provinces, String> {

}
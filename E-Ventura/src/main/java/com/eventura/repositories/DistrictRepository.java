package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;

@Repository
public interface DistrictRepository extends JpaRepository<Districts, String> {
	@Query("from Districts where provinces.code = :province_code")
	public List<Districts> findDistrictsByProvinceCode(@Param("province_code") String province_code);
}
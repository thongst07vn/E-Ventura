package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Commissions;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.Vendors;

@Repository
public interface CommissionsRepository extends JpaRepository<Commissions, Integer> {
	@Query("select count(c) from Commissions c")
	public int countCommission();
	@Query("select sum(c.amount) from Commissions c")
	public double sumCommissions();
}

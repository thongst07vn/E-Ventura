package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.ActivityLog;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Wards;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {
	@Query("select count(a) from ActivityLog a where a.deviceLog = 'PC'")
	public int countActivityLogPC();
	@Query("select count(a) from ActivityLog a where a.deviceLog = 'Phone'")
	public int countActivityLogPhone();
}
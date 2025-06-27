package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Coupons;
import com.eventura.entities.CouponsCampaigns;
import com.eventura.entities.CouponsCampaignsId;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Wards;

@Repository
public interface CouponCampaignRepository extends JpaRepository<CouponsCampaigns, CouponsCampaignsId> {
	
}
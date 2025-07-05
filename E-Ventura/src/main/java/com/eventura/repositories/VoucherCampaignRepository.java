package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eventura.entities.Coupons;
import com.eventura.entities.CouponsCampaigns;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.VouchersCampaigns;
import com.eventura.entities.Wards;

@Repository
public interface VoucherCampaignRepository extends JpaRepository<VouchersCampaigns, String> {
	@Query("from VouchersCampaigns where vouchers.id= :id and redeemUsedQty = 0")
	public List<VouchersCampaigns> findAllVouchersCampaignsByVouchersId(@Param("id") int id);
	
	@Query("from VouchersCampaigns where vouchers.id= :id and redeemUsedQty = 1")
	public List<VouchersCampaigns> findAllVouchersCampaignsByVouchersIdWithredeemUsedQty(@Param("id") int id);
}
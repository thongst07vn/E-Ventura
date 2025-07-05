package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Vouchers;
import com.eventura.entities.VouchersCampaigns;
import com.eventura.entities.CampaignRedemptions;
import com.eventura.entities.Coupons;
import com.eventura.entities.CouponsCampaigns;
import com.eventura.entities.OrdersCampaigns;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

public interface CampaignRedeemtionService{
	public boolean saveCampaignRedeemtion(CampaignRedemptions campaignRedemptions);
	public boolean saveVoucherCampaign(VouchersCampaigns vouchersCampaigns);
	public boolean saveCouponCampaign(CouponsCampaigns couponsCampaigns);
	
	public boolean saveOrderCampaign(OrdersCampaigns ordersCampaigns);
	public List<CouponsCampaigns> findAllCouponsCampaignsByCouponsId(int id);
	public List<CouponsCampaigns> findAllCouponsCampaignsByCouponsIdWithredeemUsedQty(int id);
	public List<VouchersCampaigns> findAllVouchersCampaignsByVouchersId(int id);
	public List<VouchersCampaigns> findAllVouchersCampaignsByVouchersIdWithredeemUsedQty(int id);

	public Page<OrdersCampaigns> findOrderByCampaignRedeem(int campaignRedeemId, Pageable pageable);
}

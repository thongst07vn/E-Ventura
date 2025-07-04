package com.eventura.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eventura.entities.CampaignRedemptions;
import com.eventura.entities.Coupons;
import com.eventura.entities.CouponsCampaigns;
import com.eventura.entities.OrdersCampaigns;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vouchers;
import com.eventura.entities.VouchersCampaigns;
import com.eventura.repositories.CampaignRedeemtionRepository;
import com.eventura.repositories.CouponCampaignRepository;
import com.eventura.repositories.CouponRepository;
import com.eventura.repositories.OrderCampaignRepository;
import com.eventura.repositories.UserAddressRepository;
import com.eventura.repositories.UserRepository;
import com.eventura.repositories.VoucherCampaignRepository;
import com.eventura.repositories.VoucherRepository;

@Service
public class CampaignRedeemtionServiceImpl implements CampaignRedeemtionService {
	
	@Autowired
	private CampaignRedeemtionRepository campaignRedeemtionRepository;
	@Autowired
	private VoucherCampaignRepository voucherCampaignRepository;
	@Autowired
	private CouponCampaignRepository couponCampaignRepository;
	@Autowired
	private OrderCampaignRepository orderCampaignRepository;

	@Override
	public boolean saveCampaignRedeemtion(CampaignRedemptions campaignRedemptions) {
		// TODO Auto-generated method stub
		try {
			campaignRedeemtionRepository.save(campaignRedemptions);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveVoucherCampaign(VouchersCampaigns vouchersCampaigns) {
		// TODO Auto-generated method stub
		try {
			voucherCampaignRepository.save(vouchersCampaigns);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveCouponCampaign(CouponsCampaigns couponsCampaigns) {
		// TODO Auto-generated method stub
		try {
			couponCampaignRepository.save(couponsCampaigns);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<CouponsCampaigns> findAllCouponsCampaignsByCouponsId(int id) {
		
		return couponCampaignRepository.findAllCouponsCampaignsByCouponsId(id);
	}

	@Override
	public boolean saveOrderCampaign(OrdersCampaigns ordersCampaigns) {
		try {
			orderCampaignRepository.save(ordersCampaigns);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<VouchersCampaigns> findAllVouchersCampaignsByVouchersId(int id) {
		// TODO Auto-generated method stub
		return voucherCampaignRepository.findAllVouchersCampaignsByVouchersId(id);
	}
	
}

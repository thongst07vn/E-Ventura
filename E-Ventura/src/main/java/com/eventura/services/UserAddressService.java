package com.eventura.services;

import java.util.List;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Wards;

public interface UserAddressService {
	
	public List<UserAddress> findUserAddressesByOrderId(int orderId);
	
	
}

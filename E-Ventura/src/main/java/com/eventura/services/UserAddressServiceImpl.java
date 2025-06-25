package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Wards;
import com.eventura.repositories.UserAddressRepository;

@Service
public class UserAddressServiceImpl implements UserAddressService{
	
	@Autowired
	private UserAddressRepository userAddressRepository;
	
	@Override
	public List<UserAddress> findUserAddressesByOrderId(int orderId) {
		// TODO Auto-generated method stub
		return userAddressRepository.findUserAddressByOrderId(orderId);
	}
	
}

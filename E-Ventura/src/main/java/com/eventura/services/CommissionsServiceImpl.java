package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Wards;
import com.eventura.repositories.CommissionsRepository;
import com.eventura.repositories.DistrictRepository;
import com.eventura.repositories.ProvinceRepository;
import com.eventura.repositories.UserAddressRepository;
import com.eventura.repositories.WardRepository;

@Service
public class CommissionsServiceImpl implements CommissionsService {
	@Autowired
	private CommissionsRepository commissionsRepository;
	@Override
	public int countCommission() {
		// TODO Auto-generated method stub
		return commissionsRepository.countCommission();
	}

	@Override
	public double sumCommission() {
		// TODO Auto-generated method stub
		return commissionsRepository.sumCommissions();
	}
	
	
}

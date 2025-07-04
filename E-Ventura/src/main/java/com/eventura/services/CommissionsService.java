package com.eventura.services;

import java.util.List;

import com.eventura.entities.Commissions;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Wards;

public interface CommissionsService {
	public int countCommission();
	public double sumCommission();
	
	public boolean saveCommission(Commissions commission);
}

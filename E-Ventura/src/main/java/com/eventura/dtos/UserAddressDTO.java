package com.eventura.dtos;

import java.util.Date;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Users;
import com.eventura.entities.Wards;

public class UserAddressDTO {
	
	private Integer id;
	private String districts;
	private String districtsUnit;
	private int districtsCode;
	private String provinces;
	private String provincesUnit;
	private int provincesCode;
	private int usersId;
	private String wards;
	private String wardsUnit;
	private int wardsCode;
	private String address;
	private String name;
	public UserAddressDTO() {
		super();
	}
	
	
	public UserAddressDTO(Integer id, String districts, String districtsUnit, int districtsCode, String provinces,
			String provincesUnit, int provincesCode, int usersId, String wards, String wardsUnit, int wardsCode,
			String address, String name) {
		super();
		this.id = id;
		this.districts = districts;
		this.districtsUnit = districtsUnit;
		this.districtsCode = districtsCode;
		this.provinces = provinces;
		this.provincesUnit = provincesUnit;
		this.provincesCode = provincesCode;
		this.usersId = usersId;
		this.wards = wards;
		this.wardsUnit = wardsUnit;
		this.wardsCode = wardsCode;
		this.address = address;
		this.name = name;
	}


	// Constructor để chuyển từ entity sang DTO
    public UserAddressDTO(com.eventura.entities.UserAddress userAddress) {
    	this.id = userAddress.getId();
    	this.districts = userAddress.getDistricts().getName();
    	this.districtsUnit = userAddress.getDistricts().getAdministrativeUnits().getShortName();
    	this.districtsCode = userAddress.getDistricts().getAdministrativeUnits().getId();
		this.provinces = userAddress.getProvinces().getName();;
		this.provincesUnit = userAddress.getProvinces().getAdministrativeUnits().getShortName();
		this.provincesCode = userAddress.getProvinces().getAdministrativeUnits().getId();
		this.usersId = userAddress.getUsers().getId();
		this.wards = userAddress.getWards().getName();
		this.wardsUnit = userAddress.getWards().getAdministrativeUnits().getShortName();
		this.wardsCode = userAddress.getWards().getAdministrativeUnits().getId();
		this.address = userAddress.getAddress();
		this.name = userAddress.getName();

    }
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDistricts() {
		return districts;
	}
	public void setDistricts(String districts) {
		this.districts = districts;
	}
	public String getDistrictsUnit() {
		return districtsUnit;
	}
	public void setDistrictsUnit(String districtsUnit) {
		this.districtsUnit = districtsUnit;
	}
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public String getProvincesUnit() {
		return provincesUnit;
	}
	public void setProvincesUnit(String provincesUnit) {
		this.provincesUnit = provincesUnit;
	}
	public int getUsersId() {
		return usersId;
	}
	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}
	public String getWards() {
		return wards;
	}
	public void setWards(String wards) {
		this.wards = wards;
	}
	public String getWardsUnit() {
		return wardsUnit;
	}
	public void setWardsUnit(String wardsUnit) {
		this.wardsUnit = wardsUnit;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}

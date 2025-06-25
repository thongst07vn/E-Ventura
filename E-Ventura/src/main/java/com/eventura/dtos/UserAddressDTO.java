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
	private String districtsCode;
	private String provinces;
	private String provincesUnit;
	private String provincesCode;
	private int usersId;
	private String wards;
	private String wardsUnit;
	private String wardsCode;
	private String address;
	private String name;
	public UserAddressDTO() {
		super();
	}
	
	
	public UserAddressDTO(Integer id, String districts, String districtsUnit, String districtsCode, String provinces,
			String provincesUnit, String provincesCode, int usersId, String wards, String wardsUnit, String wardsCode,
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
    	this.districtsCode = userAddress.getDistricts().getCode();
		this.provinces = userAddress.getProvinces().getName();;
		this.provincesUnit = userAddress.getProvinces().getAdministrativeUnits().getShortName();
		this.provincesCode = userAddress.getProvinces().getCode();
		this.usersId = userAddress.getUsers().getId();
		this.wards = userAddress.getWards().getName();
		this.wardsUnit = userAddress.getWards().getAdministrativeUnits().getShortName();
		this.wardsCode = userAddress.getWards().getCode();
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


	public String getDistrictsCode() {
		return districtsCode;
	}


	public void setDistrictsCode(String districtsCode) {
		this.districtsCode = districtsCode;
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


	public String getProvincesCode() {
		return provincesCode;
	}


	public void setProvincesCode(String provincesCode) {
		this.provincesCode = provincesCode;
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


	public String getWardsCode() {
		return wardsCode;
	}


	public void setWardsCode(String wardsCode) {
		this.wardsCode = wardsCode;
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

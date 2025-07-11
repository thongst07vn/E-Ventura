package com.eventura.entities;
// Generated Jul 2, 2025, 11:54:26 PM by Hibernate Tools 4.3.6.Final

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Provinces generated by hbm2java
 */
@Entity
@Table(name = "provinces")
public class Provinces implements java.io.Serializable {

	private String code;
	private AdministrativeRegions administrativeRegions;
	private AdministrativeUnits administrativeUnits;
	private String name;
	private String nameEn;
	private String fullName;
	private String fullNameEn;
	private String codeName;
	private Set<Districts> districtses = new HashSet<Districts>(0);
	private Set<UserAddress> userAddresses = new HashSet<UserAddress>(0);

	public Provinces() {
	}

	public Provinces(String code, String name, String fullName) {
		this.code = code;
		this.name = name;
		this.fullName = fullName;
	}

	public Provinces(String code, AdministrativeRegions administrativeRegions, AdministrativeUnits administrativeUnits,
			String name, String nameEn, String fullName, String fullNameEn, String codeName, Set<Districts> districtses,
			Set<UserAddress> userAddresses) {
		this.code = code;
		this.administrativeRegions = administrativeRegions;
		this.administrativeUnits = administrativeUnits;
		this.name = name;
		this.nameEn = nameEn;
		this.fullName = fullName;
		this.fullNameEn = fullNameEn;
		this.codeName = codeName;
		this.districtses = districtses;
		this.userAddresses = userAddresses;
	}

	@Id

	@Column(name = "code", unique = true, nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "administrative_region_id")
	public AdministrativeRegions getAdministrativeRegions() {
		return this.administrativeRegions;
	}

	public void setAdministrativeRegions(AdministrativeRegions administrativeRegions) {
		this.administrativeRegions = administrativeRegions;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "administrative_unit_id")
	public AdministrativeUnits getAdministrativeUnits() {
		return this.administrativeUnits;
	}

	public void setAdministrativeUnits(AdministrativeUnits administrativeUnits) {
		this.administrativeUnits = administrativeUnits;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "name_en")
	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "full_name", nullable = false)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "full_name_en")
	public String getFullNameEn() {
		return this.fullNameEn;
	}

	public void setFullNameEn(String fullNameEn) {
		this.fullNameEn = fullNameEn;
	}

	@Column(name = "code_name")
	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provinces")
	public Set<Districts> getDistrictses() {
		return this.districtses;
	}

	public void setDistrictses(Set<Districts> districtses) {
		this.districtses = districtses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provinces")
	public Set<UserAddress> getUserAddresses() {
		return this.userAddresses;
	}

	public void setUserAddresses(Set<UserAddress> userAddresses) {
		this.userAddresses = userAddresses;
	}

}

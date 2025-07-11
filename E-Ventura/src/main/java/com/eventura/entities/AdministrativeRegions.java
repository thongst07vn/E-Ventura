package com.eventura.entities;
// Generated Jul 2, 2025, 11:54:26 PM by Hibernate Tools 4.3.6.Final

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * AdministrativeRegions generated by hbm2java
 */
@Entity
@Table(name = "administrative_regions")
public class AdministrativeRegions implements java.io.Serializable {

	private int id;
	private String name;
	private String nameEn;
	private String codeName;
	private String codeNameEn;
	private Set<Provinces> provinceses = new HashSet<Provinces>(0);

	public AdministrativeRegions() {
	}

	public AdministrativeRegions(int id, String name, String nameEn) {
		this.id = id;
		this.name = name;
		this.nameEn = nameEn;
	}

	public AdministrativeRegions(int id, String name, String nameEn, String codeName, String codeNameEn,
			Set<Provinces> provinceses) {
		this.id = id;
		this.name = name;
		this.nameEn = nameEn;
		this.codeName = codeName;
		this.codeNameEn = codeNameEn;
		this.provinceses = provinceses;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "name_en", nullable = false)
	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "code_name")
	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Column(name = "code_name_en")
	public String getCodeNameEn() {
		return this.codeNameEn;
	}

	public void setCodeNameEn(String codeNameEn) {
		this.codeNameEn = codeNameEn;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "administrativeRegions")
	public Set<Provinces> getProvinceses() {
		return this.provinceses;
	}

	public void setProvinceses(Set<Provinces> provinceses) {
		this.provinceses = provinceses;
	}

}

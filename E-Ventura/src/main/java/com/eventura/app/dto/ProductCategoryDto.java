package com.eventura.app.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductCategoryDto {
	private int id;
	private String Name;
	private String Photo;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdAt;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date updatedAt;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date deletedAt;
	public ProductCategoryDto() {
		super();
	}
	public ProductCategoryDto(int id, String name, String photo, Date createdAt, Date updatedAt, Date deletedAt) {
		super();
		this.id = id;
		Name = name;
		Photo = photo;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	
}

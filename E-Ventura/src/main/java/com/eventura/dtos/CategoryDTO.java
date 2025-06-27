package com.eventura.dtos;

import com.eventura.entities.ProductCategories;

public class CategoryDTO {
	private int id;
	private String Name;
	private String Photo;
	
	public CategoryDTO() {
	}
	
	public CategoryDTO(int id, String name, String photo) {
		this.id = id;
		Name = name;
		Photo = photo;
	}
	public CategoryDTO(ProductCategories category) {
		this.id = category.getId();
		Name = category.getName();
		Photo = category.getPhoto();
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
	
}

package com.eventura.app.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.eventura.entities.Coupons;
import com.eventura.entities.Medias;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.VendorSettings;
import com.eventura.entities.Vendors;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductDto {
	private int id;
	//product category
	private int categoryId;
	//product vendor
	private int vendorId;
	private double vendorCommission;
	private String name;
	private String description;
	private double price;
	private int quantity;
	private boolean deleted;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdAt;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date updatedAt;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date deletedAt;
	public ProductDto() {
		super();
	}
	public ProductDto(int id, int categoryId, int vendorId, double vendorCommission, String name, String description,
			double price, int quantity, boolean deleted, Date createdAt, Date updatedAt, Date deletedAt) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.vendorId = vendorId;
		this.vendorCommission = vendorCommission;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.deleted = deleted;
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
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public double getVendorCommission() {
		return vendorCommission;
	}
	public void setVendorCommission(double vendorCommission) {
		this.vendorCommission = vendorCommission;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

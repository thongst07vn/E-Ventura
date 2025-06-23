package com.eventura.dtos;

import com.eventura.entities.Districts;
import com.eventura.entities.Products;

public class ProductDTO {
    private Products products;
    private double rating;

    public ProductDTO() {
    }

	public ProductDTO(Products products, double rating) {
		this.products = products;
		this.rating = rating;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
    

}

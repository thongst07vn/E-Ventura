package com.eventura.dtos;

import com.eventura.entities.Districts;
import com.eventura.entities.Products;

public class ProductDTO {
    private Products products;
    private double rating;
    private int countRating;

    public ProductDTO() {
    }

	public ProductDTO(Products products, double rating) {
		this.products = products;
		this.rating = rating;
	}
	
	public ProductDTO(Products products, double rating, int countRating) {
		this.products = products;
		this.rating = rating;
		this.countRating = countRating;
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

	public int getCountRating() {
		return countRating;
	}

	public void setCountRating(int countRating) {
		this.countRating = countRating;
	}
	
    

}

package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.Medias;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Vendors;

public interface MediaService {

	public boolean save(Medias media);
	
	public boolean delete(int id);
}

package com.eventura.app.services;

import java.util.List;
import java.util.SortedMap;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.app.configuration.ModelMapperConfiguration;
import com.eventura.app.dto.ProductCategoryDto;
import com.eventura.app.dto.ProductDto;
import com.eventura.repositories.CategoryRepository;
import com.eventura.repositories.ProductRepository;

@Service
public class ProductCategoryServiceImplApp implements ProductCategoryServiceApp{

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<ProductCategoryDto> findAll() {
		return modelMapper.map(categoryRepository.findAllCategory(),  new TypeToken<List<ProductCategoryDto>>(){}.getType());
	}

	@Override
	public ProductCategoryDto findById(int id) {
		if(productRepository.existsById(id)) {
			return modelMapper.map(categoryRepository.findById(id).get(),ProductCategoryDto.class);
		}
		return null;
	}
	
}

package com.eventura.app.services;

import java.util.List;
import java.util.SortedMap;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.app.configuration.ModelMapperConfiguration;
import com.eventura.app.dto.ProductDto;
import com.eventura.repositories.ProductRepository;

@Service
public class ProductServiceImplApp implements ProductServiceApp{

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductRepository productRepository;
	@Override
	public List<ProductDto> findAll() {
		// TODO Auto-generated method stub
		return modelMapper.map(productRepository.findAll(),  new TypeToken<List<ProductDto>>(){}.getType());
	}

	@Override
	public List<ProductDto> findByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return modelMapper.map(productRepository.findByKeyword(keyword,Sort.by("createdAt").descending()),  new TypeToken<List<ProductDto>>(){}.getType());
	}

	@Override
	public ProductDto findById(int id) {
		if(productRepository.existsById(id)) {
			return modelMapper.map(productRepository.findById(id).get(),ProductDto.class);
		}
		return null;
	}
	
}

package com.eventura.app.configuration;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.app.dto.ProductCategoryDto;
import com.eventura.app.dto.ProductDto;

@Configuration
public class ModelMapperConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();

		mapper.addMappings(new PropertyMap<Products, ProductDto>() {

			@Override
			protected void configure() {
				map().setId(source.getId());
				map().setName(source.getName());
				map().setCategoryId(source.getProductCategories().getId());
				map().setVendorId(source.getVendors().getId());
				map().setVendorCommission(source.getVendors().getVendorSettings().getCommissionValue());
				map().setDescription(source.getDescription());
				map().setPrice(source.getPrice());
				map().setQuantity(source.getQuantity());
				map().setDeleted(source.isDeleted());
				map().setDeletedAt(source.getDeletedAt());
				map().setUpdatedAt(source.getUpdatedAt());
				map().setCreatedAt(source.getCreatedAt());
			}
		});
		Converter<String, String> converterPhotoToPhotourl = new AbstractConverter<String, String>() {

			@Override
			protected String convert(String source) {
				return environment.getProperty("image_url") + source;
			}
		};
		mapper.typeMap(ProductCategories.class, ProductCategoryDto.class).addMappings(m -> {
			m.using(converterPhotoToPhotourl).map(ProductCategories::getPhoto, ProductCategoryDto::setPhoto);
		});
		mapper.addMappings(new PropertyMap<ProductCategories, ProductCategoryDto>() {
			@Override
			protected void configure() {
				map().setId(source.getId());
				map().setName(source.getName());
				map().setDeletedAt(source.getDeletedAt());
				map().setUpdatedAt(source.getUpdatedAt());
				map().setCreatedAt(source.getCreatedAt());
			}
		});

		return mapper;

	}

}

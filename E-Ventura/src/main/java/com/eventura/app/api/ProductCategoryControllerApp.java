package com.eventura.app.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.app.dto.ProductCategoryDto;
import com.eventura.app.dto.ProductDto;
import com.eventura.app.services.ProductCategoryServiceApp;
import com.eventura.app.services.ProductServiceApp;



@RestController
@RequestMapping({"api/category"})
public class ProductCategoryControllerApp {
	@Autowired
	private ProductServiceApp productService;
	@Autowired
	private ProductCategoryServiceApp productCategoryServiceApp;

	@GetMapping(value = "findall", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductCategoryDto>> findAll() {
		try {
			return new ResponseEntity<List<ProductCategoryDto>>(productCategoryServiceApp.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<ProductCategoryDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping(value = "findbyid/{id}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductCategoryDto> findById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<ProductCategoryDto>(productCategoryServiceApp.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ProductCategoryDto>(HttpStatus.BAD_REQUEST);
		}
	}

}

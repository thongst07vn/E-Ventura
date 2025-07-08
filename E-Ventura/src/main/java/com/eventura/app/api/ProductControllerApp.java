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

import com.eventura.app.dto.ProductDto;
import com.eventura.app.services.ProductServiceApp;



@RestController
@RequestMapping({"api/product"})
public class ProductControllerApp {
	@Autowired
	private ProductServiceApp productService;

	@GetMapping(value = "findall", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDto>> findAll() {
		try {
			return new ResponseEntity<List<ProductDto>>(productService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<ProductDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping(value = "findbykeyword", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDto>> findByKeyword(@RequestParam("keyword") String keyword) {
		try {
			return new ResponseEntity<List<ProductDto>>(productService.findByKeyword(keyword), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<ProductDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping(value = "findbyid/{id}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDto> findById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<ProductDto>(productService.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ProductDto>(HttpStatus.BAD_REQUEST);
		}
	}

}

package com.eventura.app.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.app.dto.ProductDto;
import com.eventura.app.dto.UserDto;
import com.eventura.app.services.ProductServiceApp;
import com.eventura.app.services.UserServiceApp;



@RestController
@RequestMapping({"api/user"})
public class UserControllerApp {
	@Autowired
	private UserServiceApp userServiceApp;

	@GetMapping(value = "findall", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDto>> findAll() {
		try {
			return new ResponseEntity<List<UserDto>>(userServiceApp.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<UserDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "create",
			 consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
			 produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> create(@RequestBody UserDto userDto){
		try {
			if(userServiceApp.save(userDto)) {
				return new ResponseEntity<Object>(HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST );
			}
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

}

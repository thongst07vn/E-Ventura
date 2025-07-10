package com.eventura.app.services;

import java.util.List;

import com.eventura.app.dto.ProductDto;
import com.eventura.app.dto.UserDto;

public interface UserServiceApp {
	public List<UserDto> findAll();
	
	public boolean save(UserDto userDto);
	public boolean delete(int id);

}

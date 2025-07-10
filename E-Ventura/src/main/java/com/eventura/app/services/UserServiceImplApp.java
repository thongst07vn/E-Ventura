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
import com.eventura.app.dto.UserDto;
import com.eventura.entities.Roles;
import com.eventura.entities.Users;
import com.eventura.repositories.ProductRepository;
import com.eventura.repositories.RoleRepository;
import com.eventura.repositories.UserRepository;

@Service
public class UserServiceImplApp implements UserServiceApp{

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository; // repo để truy vấn role theo id
	@Override
	public List<UserDto> findAll() {
		// TODO Auto-generated method stub
		return modelMapper.map(userRepository.findAll(),  new TypeToken<List<UserDto>>(){}.getType());
	}
	@Override
	public boolean save(UserDto userDto) {
		try {
			Users user = modelMapper.map(userDto, Users.class);
			if(userDto.getId() == 0) {
				user.setId(null);
			}

			// Tạo đối tượng Role từ roleId
			Roles role = roleRepository.findById(userDto.getRoleId()).orElse(null);
			if (role == null) return false;

			user.setRoles(role);
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}

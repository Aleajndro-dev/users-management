package com.example.test.service;

import java.util.List;

import com.example.test.dto.GetUserDTO;
import com.example.test.dto.ImageResponseDTO;
import com.example.test.dto.SaveUserDTO;

public interface UsersService {
	
	GetUserDTO createUser(SaveUserDTO user);
	List<GetUserDTO> getUserList(Integer page);
	GetUserDTO getUserById(Long id);
	GetUserDTO updateUser(SaveUserDTO user, Long id);
	void deleteUser(Long id);
	ImageResponseDTO getPhoto(Long id);

}

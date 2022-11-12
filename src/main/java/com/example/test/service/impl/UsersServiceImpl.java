package com.example.test.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.test.dto.GetUserDTO;
import com.example.test.dto.ImageResponseDTO;
import com.example.test.dto.SaveUserDTO;
import com.example.test.exception.BadRequestDataException;
import com.example.test.exception.ResourceNotFoundException;
import com.example.test.file.property.FileStorageProperties;
import com.example.test.model.Users;
import com.example.test.repository.UsersRepository;
import com.example.test.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService{
	private UsersRepository userRepository;
	private Path dirLocation;

	@Autowired
	public UsersServiceImpl(UsersRepository userRepository, FileStorageProperties fileProperties) {
		super();
		this.userRepository = userRepository;
		this.dirLocation = Paths.get(fileProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
	}
	
	@PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.dirLocation);
        } 
        catch (Exception ex) {
        	throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, "Could not create upload dir!");
        }
    }

	@Override
	public GetUserDTO createUser(SaveUserDTO user) {
		this.validateStatus(user.getStatus());
		String photoName = this.savePhoto(user.getPhoto());
		return new GetUserDTO(userRepository.save(
				user.convertToRepository(photoName,user.getPhoto().getContentType())));
	}
	
	@Override
	public List<GetUserDTO> getUserList(Integer page) {
		Pageable paging = PageRequest.of(page, 10);
        Page<Users> pagedResult = userRepository.findAll(paging);
        List<GetUserDTO> list = new ArrayList<GetUserDTO>();
        pagedResult.toList().forEach((user)->{
        	list.add(new GetUserDTO(user));
        });

        return list;
	}

	@Override
	public GetUserDTO getUserById(Long id) {
		Optional<Users> user = userRepository.findById(id);
		if(user.isPresent()) {
			return new GetUserDTO(user.get());
		}else {
			throw new ResourceNotFoundException("Users", "Id", id);
		}
	}
	
	@Override
	public GetUserDTO updateUser(SaveUserDTO user, Long id) {
		Optional<Users> existingUser = userRepository.findById(id);
		if(existingUser.isPresent()) {
			this.validateStatus(user.getStatus());
			
			Users newUser = existingUser.get();
			if(user.getName()!= null) newUser.setName(user.getName());
			if(user.getPhoto()!= null) {
				String photoName = this.savePhoto(user.getPhoto());
				newUser.setPhoto(photoName);
			}
			if(user.getGender()!= null) newUser.setGender(user.getGender());
			if(user.getStatus()!= null) newUser.setStatus(user.getStatus());
			return new GetUserDTO(userRepository.save(newUser));
			
		}else {
			throw new ResourceNotFoundException("Users", "Id", id);
		}
	}

	@Override
	public void deleteUser(Long id) {
		Optional<Users> user = userRepository.findById(id);
		if(user.isPresent()) {
			userRepository.deleteById(id);
		}else {
			throw new ResourceNotFoundException("Users", "Id", id);
		}	
	}
	
	@Override
	public ImageResponseDTO getPhoto(Long id) {
		Optional<Users> user = userRepository.findById(id);
		ImageResponseDTO photo = new ImageResponseDTO();
		if(user.isPresent()) {
			photo.setFileName(user.get().getPhoto());
			photo.setFileType(user.get().getImageType());
			photo.setImage(this.readPhoto(user.get().getPhoto()));
			return photo;
		}else {
			throw new ResourceNotFoundException("Users", "Id", id);
		}
	}
	
	private String savePhoto(MultipartFile photo) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (!photo.isEmpty()) {
			if (!photo.getContentType().equals("image/png") && !photo.getContentType().equals("image/jpeg"))
				throw new ResponseStatusException( HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
						"The photo must be of a valid image type (.jpg, .jpeg o .png )");
			try {
				String[] words = photo.getOriginalFilename().split("\\.");
				String name = timestamp.getTime() + "." + words[words.length - 1];
				Path imageDir = this.dirLocation.resolve(name);
				byte[] bytesImg = photo.getBytes();
				Files.write(imageDir, bytesImg);
				return name;

			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The image could not be saved");
			}
		} else {
			throw new BadRequestDataException("User", "photo", "Empty");
		}
	}

	private byte[] readPhoto(String namePhoto){
		byte[] content = null;
		try {
			Path file = this.dirLocation.resolve(namePhoto).normalize();
		    content = Files.readAllBytes(file);
		} catch (final IOException e) {
			throw new ResourceNotFoundException("Photo", "name", namePhoto);
		}
		return content;
	}
	
	private void validateStatus(Integer status) {
		if(!(status== 200 || status== 500))
			throw new BadRequestDataException("User", "status", status);
	}
	
}

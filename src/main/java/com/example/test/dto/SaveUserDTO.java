package com.example.test.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.test.model.UserEmail;
import com.example.test.model.Users;

public class SaveUserDTO {
	
	private String name;
	private MultipartFile photo;
	private List<String> emails = new ArrayList<>();
	private String gender;
	private Integer status;

	public SaveUserDTO() {
		//
	}
	
	
	public SaveUserDTO(String name, MultipartFile photo, List<String> emails, String gender, Integer status) {
		this.name = name;
		this.photo = photo;
		this.emails = emails;
		this.gender = gender;
		this.status = status;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MultipartFile getPhoto() {
		return photo;
	}
	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	

	public Users convertToRepository (String namePhoto, String imageType) {
		List<UserEmail> userEmails = new ArrayList<UserEmail>();
		this.emails.forEach((email) -> userEmails.add(new UserEmail(email)));
		return new Users(this.name, namePhoto, userEmails, this.gender, this.status, imageType);		
	}
	
	
}



package com.example.test.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.test.model.UserEmail;
import com.example.test.model.Users;

public class GetUserDTO {

	private Long id;
	private String name;
	private String photo;
	private List<String> emails = new ArrayList<>();
	private String gender;
	private Integer status;
	
	public GetUserDTO() {
		//
	}
	
	public GetUserDTO(Long id, String name, String photo, List<String> emails, String gender, Integer status) {
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.emails = emails;
		this.gender = gender;
		this.status = status;
	}
	
	public GetUserDTO(Users user) {
		this.id = user.getId();
		this.name = user.getName();
		this.gender = user.getGender();
		this.status = user.getStatus();
		this.photo = user.getPhoto();
		for(UserEmail email : user.getEmails()){
			this.emails.add(email.getEmail());
		}
	}
	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
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

}


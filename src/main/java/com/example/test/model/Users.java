package com.example.test.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="user")
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;
	@Column(name="photo")
	private String photo;
	@Column(name="image_type")
	private String imageType;


	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<UserEmail> emails = new ArrayList<UserEmail>();
	
	public Users() {
		super();
	}
	
	public Users(String name, String photo, List<UserEmail> emails, String gender, Integer status,
			String imageType) {
		super();
		this.name = name;
		this.photo = photo;
		this.imageType = imageType;
		this.emails = emails;
		this.gender = gender;
		this.status = status;
	}
	
	@Column(name="gender")
	private String gender;
	@Column(name="status")
	private Integer status;
	
	public Long getId() {
		return id;
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
	
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public List<UserEmail> getEmails() {
		return emails;
	}
	public void setEmails(List<UserEmail> emails) {
		this.emails = emails;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
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

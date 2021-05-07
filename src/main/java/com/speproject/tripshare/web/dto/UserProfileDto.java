package com.speproject.tripshare.web.dto;

import com.speproject.tripshare.model.User;

import java.sql.Date;

public class UserProfileDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Integer gender;
	private Date dob;
	private String description;
	private String photoPath;

	public UserProfileDto(){

	}

	public UserProfileDto(String firstName, String lastName, String email, String password, Integer gender, Date dob, String description) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.dob = dob;
		this.description = description;
		this.photoPath = "";
	}

	public UserProfileDto(User user){
		super();
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = "";
		this.gender = user.getGender();
		this.dob = user.getDob();
		this.description = user.getDescription();
		this.photoPath = user.getPhotoPath();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	@Override
	public boolean equals(Object other){
		if (other == this) {
			return true;
		}

		if (!(other instanceof UserProfileDto)) {
			return false;
		}
		return this.id.equals(((UserProfileDto) other).id);
	}

	@Override
	public int hashCode(){
		return this.email.hashCode();
	}
}

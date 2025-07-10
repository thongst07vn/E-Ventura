package com.eventura.app.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDto {
	private int id;
	private int roleId;
	private String avatar;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String rememberToken;
	private String password;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date birthOfDate;
	private String phoneNumber;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdAt;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date deletedAt;
	public UserDto() {
		super();
	}
	public UserDto(int id, int roleId, String avatar, String firstName, String lastName, String username, String email,
			String rememberToken, String password, Date birthOfDate, String phoneNumber, Date createdAt,
			Date deletedAt) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.avatar = avatar;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.rememberToken = rememberToken;
		this.password = password;
		this.birthOfDate = birthOfDate;
		this.phoneNumber = phoneNumber;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRememberToken() {
		return rememberToken;
	}
	public void setRememberToken(String rememberToken) {
		this.rememberToken = rememberToken;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthOfDate() {
		return birthOfDate;
	}
	public void setBirthOfDate(Date birthOfDate) {
		this.birthOfDate = birthOfDate;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	
	
	
}

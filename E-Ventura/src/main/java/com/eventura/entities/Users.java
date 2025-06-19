package com.eventura.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(columnNames = "email"),
	@UniqueConstraint(columnNames = "username")
})
public class Users implements java.io.Serializable {

	private int id;
	private Roles roles;
	private Vendors vendors;
	private String avatar;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String rememberToken;
	private String password;
	private Date birthOfDate;
	private String phoneNumber;
	private Date createdAt;
	private Date deletedAt;

	private Set<VendorReviews> vendorReviewses = new HashSet<>(0);
	private Set<Carts> cartses = new HashSet<>(0);
	private Set<ProductReviews> productReviewses = new HashSet<>(0);
	private Set<Orders> orderses = new HashSet<>(0);
	private Set<ActivityLog> activityLogs = new HashSet<>(0);
	private Set<UserAddress> userAddresses = new HashSet<>(0);

	public Users() {}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "vendors"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Vendors getVendors() {
		return vendors;
	}

	public void setVendors(Vendors vendors) {
		this.vendors = vendors;
	}

	@Column(name = "avatar")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "username", unique = true, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "email", unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "remember_token")
	public String getRememberToken() {
		return rememberToken;
	}

	public void setRememberToken(String rememberToken) {
		this.rememberToken = rememberToken;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "birth_of_date", length = 10)
	public Date getBirthOfDate() {
		return birthOfDate;
	}

	public void setBirthOfDate(Date birthOfDate) {
		this.birthOfDate = birthOfDate;
	}

	@Column(name = "phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, length = 19)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", nullable = false, length = 19)
	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<VendorReviews> getVendorReviewses() {
		return vendorReviewses;
	}

	public void setVendorReviewses(Set<VendorReviews> vendorReviewses) {
		this.vendorReviewses = vendorReviewses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Carts> getCartses() {
		return cartses;
	}

	public void setCartses(Set<Carts> cartses) {
		this.cartses = cartses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<ProductReviews> getProductReviewses() {
		return productReviewses;
	}

	public void setProductReviewses(Set<ProductReviews> productReviewses) {
		this.productReviewses = productReviewses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Orders> getOrderses() {
		return orderses;
	}

	public void setOrderses(Set<Orders> orderses) {
		this.orderses = orderses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<ActivityLog> getActivityLogs() {
		return activityLogs;
	}

	public void setActivityLogs(Set<ActivityLog> activityLogs) {
		this.activityLogs = activityLogs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<UserAddress> getUserAddresses() {
		return userAddresses;
	}

	public void setUserAddresses(Set<UserAddress> userAddresses) {
		this.userAddresses = userAddresses;
	}
}

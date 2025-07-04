package com.eventura.entities;
// Generated Jul 2, 2025, 11:54:26 PM by Hibernate Tools 4.3.6.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Products generated by hbm2java
 */
@Entity
@Table(name = "products")
public class Products implements java.io.Serializable {

	private Integer id;
	private ProductCategories productCategories;
	private Vendors vendors;
	private String name;
	private String description;
	private double price;
	private int quantity;
	private boolean deleted;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private Set<ProductVariants> productVariantses = new HashSet<ProductVariants>(0);
	private Set<CartItems> cartItemses = new HashSet<CartItems>(0);
	private Set<OrderReturns> orderReturnses = new HashSet<OrderReturns>(0);
	private Set<ProductReviews> productReviewses = new HashSet<ProductReviews>(0);
	private Set<OrderItems> orderItemses = new HashSet<OrderItems>(0);
	private Set<Medias> mediases = new HashSet<Medias>(0);
	private Set<Coupons> couponses = new HashSet<Coupons>(0);

	public Products() {
	}

	public Products(ProductCategories productCategories, Vendors vendors, String name, String description, double price,
			int quantity, Date createdAt, Date updatedAt) {
		this.productCategories = productCategories;
		this.vendors = vendors;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Products(ProductCategories productCategories, Vendors vendors, String name, String description, double price,
			int quantity, Date createdAt, Date updatedAt, Date deletedAt, Set<ProductVariants> productVariantses,
			Set<CartItems> cartItemses, Set<OrderReturns> orderReturnses, Set<ProductReviews> productReviewses,
			Set<OrderItems> orderItemses, Set<Medias> mediases, Set<Coupons> couponses) {
		this.productCategories = productCategories;
		this.vendors = vendors;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.productVariantses = productVariantses;
		this.cartItemses = cartItemses;
		this.orderReturnses = orderReturnses;
		this.productReviewses = productReviewses;
		this.orderItemses = orderItemses;
		this.mediases = mediases;
		this.couponses = couponses;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	public ProductCategories getProductCategories() {
		return this.productCategories;
	}

	public void setProductCategories(ProductCategories productCategories) {
		this.productCategories = productCategories;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	public Vendors getVendors() {
		return this.vendors;
	}

	public void setVendors(Vendors vendors) {
		this.vendors = vendors;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = false, length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "price", nullable = false, precision = 22, scale = 0)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "quantity", nullable = false)
	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "deleted", nullable = false)
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false, length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	public Date getDeletedAt() {
		return this.deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<ProductVariants> getProductVariantses() {
		return this.productVariantses;
	}

	public void setProductVariantses(Set<ProductVariants> productVariantses) {
		this.productVariantses = productVariantses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<CartItems> getCartItemses() {
		return this.cartItemses;
	}

	public void setCartItemses(Set<CartItems> cartItemses) {
		this.cartItemses = cartItemses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<OrderReturns> getOrderReturnses() {
		return this.orderReturnses;
	}

	public void setOrderReturnses(Set<OrderReturns> orderReturnses) {
		this.orderReturnses = orderReturnses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<ProductReviews> getProductReviewses() {
		return this.productReviewses;
	}

	public void setProductReviewses(Set<ProductReviews> productReviewses) {
		this.productReviewses = productReviewses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<OrderItems> getOrderItemses() {
		return this.orderItemses;
	}

	public void setOrderItemses(Set<OrderItems> orderItemses) {
		this.orderItemses = orderItemses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<Medias> getMediases() {
		return this.mediases;
	}

	public void setMediases(Set<Medias> mediases) {
		this.mediases = mediases;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
	public Set<Coupons> getCouponses() {
		return this.couponses;
	}

	public void setCouponses(Set<Coupons> couponses) {
		this.couponses = couponses;
	}



	
}

package com.eventura.entities;
// Generated Jun 12, 2025, 2:02:47 PM by Hibernate Tools 4.3.6.Final

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * CartItems generated by hbm2java
 */
@Entity
@Table(name = "cart_items")
public class CartItems implements java.io.Serializable {

	private int id;
	private Carts carts;
	private Integer productId;
	private Integer productVariantId;
	private Integer quantity;
	private Date createdAt;
	private Date updatedAt;

	public CartItems() {
	}

	public CartItems(int id, Date createdAt, Date updatedAt) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public CartItems(int id, Carts carts, Integer productId, Integer productVariantId, Integer quantity, Date createdAt,
			Date updatedAt) {
		this.id = id;
		this.carts = carts;
		this.productId = productId;
		this.productVariantId = productVariantId;
		this.quantity = quantity;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	public Carts getCarts() {
		return this.carts;
	}

	public void setCarts(Carts carts) {
		this.carts = carts;
	}

	@Column(name = "product_id")
	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Column(name = "product_variant_id")
	public Integer getProductVariantId() {
		return this.productVariantId;
	}

	public void setProductVariantId(Integer productVariantId) {
		this.productVariantId = productVariantId;
	}

	@Column(name = "quantity")
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

}

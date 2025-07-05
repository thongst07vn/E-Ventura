package com.eventura.dtos;

public class OrderStatusUpdateRequest {
	private int orderItemId;
	private String newStatus;

	// Getters and Setters
	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
}

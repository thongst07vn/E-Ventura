package com.eventura.dtos;

import java.util.Date;
import java.util.List;

import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderStatus;

public class OrderVendorDTO {
    private Integer orderId;
    private String orderName;
    private String paymentMethod;
    private Date createdAt;
    private Double totalAmount;
    private OrderStatus orderStatus; 

    // Constructor ánh xạ dữ liệu
    public OrderVendorDTO(Integer orderId, String orderName, String paymentMethod, Date createdAt, 
                          Double totalAmount, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;  // Ánh xạ trạng thái vào DTO
    }

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	public String getOrderName() {
		return orderName;
	}

	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}



    
    
}


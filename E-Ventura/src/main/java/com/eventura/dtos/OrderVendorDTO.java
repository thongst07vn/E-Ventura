package com.eventura.dtos;

import java.util.Date;
import java.util.List;

import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderStatus;

public class OrderVendorDTO {
    private Integer orderId;
    private String orderName;
    private Date createdAt;
    private Double totalAmount;
    private OrderStatus orderStatus; 

    // Constructor ánh xạ dữ liệu
    public OrderVendorDTO(Integer orderId, String orderName, Date createdAt, 
                          Double totalAmount, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderName = orderName;
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


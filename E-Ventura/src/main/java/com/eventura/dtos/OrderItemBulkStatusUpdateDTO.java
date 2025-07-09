// src/main/java/com/yourpackage/dto/OrderItemBulkStatusUpdateDTO.java
package com.eventura.dtos; // Adjust package as necessary

import java.util.List;

public class OrderItemBulkStatusUpdateDTO {
    private List<Integer> orderItemIds;
    private String newStatusName;

    // Constructors, Getters, and Setters
    public OrderItemBulkStatusUpdateDTO() {
    }

    public OrderItemBulkStatusUpdateDTO(List<Integer> orderItemIds, String newStatusName) {
        this.orderItemIds = orderItemIds;
        this.newStatusName = newStatusName;
    }

    public List<Integer> getOrderItemIds() {
        return orderItemIds;
    }

    public void setOrderItemIds(List<Integer> orderItemIds) {
        this.orderItemIds = orderItemIds;
    }

    public String getNewStatusName() {
        return newStatusName;
    }

    public void setNewStatusName(String newStatusName) {
        this.newStatusName = newStatusName;
    }
}
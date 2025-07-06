package com.eventura.dtos;

public class UpdateVendorStatusRequest {
	 private Integer vendorId;
     private String newStatus;

     // Getters and Setters
     public Integer getVendorId() {
         return vendorId;
     }

     public void setVendorId(Integer vendorId) {
         this.vendorId = vendorId;
     }

     public String getNewStatus() {
         return newStatus;
     }

     public void setNewStatus(String newStatus) {
         this.newStatus = newStatus;
     }
}

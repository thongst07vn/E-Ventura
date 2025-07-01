// src/main/java/com/yourpackage/dto/VendorSettingUpdateDto.java
package com.eventura.dtos;

public class VendorSettingDTO {
    private int id;
    private String fieldName;
    private String value; // Used for vendorType and commissionValue
    private String deletedAt; // Add this field for the switch status

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) { // Changed to String for flexibility
        this.deletedAt = deletedAt;
    }
}
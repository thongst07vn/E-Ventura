package com.eventura.entities;

public class WardsDTO {
    private String code;
    private String name;

    public WardsDTO() {
    }

    public WardsDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Constructor chuyển từ entity sang DTO
    public WardsDTO(com.eventura.entities.Wards ward) {
        this.code = ward.getCode();
        this.name = ward.getName();
    }

    // Getter và Setter
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


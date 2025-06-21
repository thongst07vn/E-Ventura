package com.eventura.entities;

public class DistrictsDTO {
    private String code;
    private String name;

    public DistrictsDTO() {
    }

    public DistrictsDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Constructor để chuyển từ entity sang DTO
    public DistrictsDTO(com.eventura.entities.Districts district) {
        this.code = district.getCode();
        this.name = district.getName();
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

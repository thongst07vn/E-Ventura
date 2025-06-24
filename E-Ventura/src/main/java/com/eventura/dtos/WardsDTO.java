package com.eventura.dtos;

import com.eventura.entities.Wards;

public class WardsDTO {
    private String code;
    private String name;
    private String shortName;
    
    public WardsDTO() {
    }

    public WardsDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public WardsDTO(String code, String name, String shortName) {
		super();
		this.code = code;
		this.name = name;
		this.shortName = shortName;
	}
    // Constructor chuyển từ entity sang DTO
    public WardsDTO(com.eventura.entities.Wards ward) {
        this.code = ward.getCode();
        this.name = ward.getName();
        if (ward.getAdministrativeUnits() != null) {
            this.shortName = ward.getAdministrativeUnits().getShortName();
        } else {
            this.shortName = null; // Or handle as appropriate, e.g., an empty string
        }
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
    
}


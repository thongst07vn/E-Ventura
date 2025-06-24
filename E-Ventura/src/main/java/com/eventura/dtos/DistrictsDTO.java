package com.eventura.dtos;

import com.eventura.entities.Districts;

public class DistrictsDTO {
    private String code;
    private String name;
    private String shortName;
    
    public DistrictsDTO() {
    }

    public DistrictsDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public DistrictsDTO(String code, String name, String shortName) {
		super();
		this.code = code;
		this.name = name;
		this.shortName = shortName;
	}

	// Constructor để chuyển từ entity sang DTO
    public DistrictsDTO(com.eventura.entities.Districts district) {
        this.code = district.getCode();
        this.name = district.getName();
        if (district.getAdministrativeUnits() != null) {
            this.shortName = district.getAdministrativeUnits().getShortName();
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

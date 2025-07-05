package com.eventura.dtos;

public class StatusUpdateResponse {
	private boolean success;
    private String message;

    public StatusUpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters (no setters needed for response)
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

package com.springboot.rest.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Data
@NoArgsConstructor
public class PasswordChangeDTO {

	private Long id;
    private String currentPassword;
    private String newPassword;

 

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
    
    public PasswordChangeDTO(Long id, String currentPassword, String newPassword) {
    	this.id = id;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

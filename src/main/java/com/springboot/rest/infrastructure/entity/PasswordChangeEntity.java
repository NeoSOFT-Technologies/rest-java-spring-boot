package com.springboot.rest.infrastructure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An representing a password change required data - current and new password.
 */

//@Entity
//@Table(name = "password_change")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PasswordChangeEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(name = "current_password")
    private String currentPassword;
    
    @Column(name = "new_password")
    private String newPassword;

    public PasswordChangeEntity() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChangeEntity(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
    
    public PasswordChangeEntity(Long id, String currentPassword, String newPassword) {
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

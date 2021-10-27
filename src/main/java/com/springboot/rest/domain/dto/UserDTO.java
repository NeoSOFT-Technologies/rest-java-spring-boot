package com.springboot.rest.domain.dto;

import javax.persistence.Entity;

import com.springboot.rest.infrastructure.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * A DTO representing a user, with only the public attributes.
 */

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String login;

    
    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }

}
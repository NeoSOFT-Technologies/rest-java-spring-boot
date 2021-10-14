package com.springboot.rest.domain.port.api;

import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.UserDTO;
import com.springboot.rest.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserServicePort {

    Optional<User> activateRegistration(String key);
    Optional<User> completePasswordReset(String newPassword, String key);
    Optional<User> requestPasswordReset(String mail);
    User registerUser(AdminUserDTO userDTO, String password);
    boolean removeNonActivatedUser(User existingUser);
    User createUser(AdminUserDTO userDTO);
    Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO);
    void deleteUser(String login);
    void saveAccount(AdminUserDTO userDTO, String userLogin);
    void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl);
    void changePassword(String currentClearTextPassword, String newPassword);
    Page<AdminUserDTO> getAllManagedUsers(Pageable pageable);
    Page<UserDTO> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
    Optional<User> getUserWithAuthoritiesByLogin(String login);
    Optional<User> getUserWithAuthorities();
    void removeNotActivatedUsers();
    Page<UserDTO> getAllPublicUsers(Pageable pageable);
    void clearUserCaches(User user);

}

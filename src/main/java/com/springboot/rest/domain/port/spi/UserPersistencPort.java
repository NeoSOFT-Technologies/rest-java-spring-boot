package com.springboot.rest.domain.port.spi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.UserDTO;
import com.springboot.rest.infrastructure.entity.User;

public interface UserPersistencPort {


    Optional<User> findOneByActivationKey(String key);

    Optional<User> completePasswordReset(String newPassword, String key);

    Optional<User> requestPasswordReset(String mail);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findById(Long id);

    User save(AdminUserDTO userDTO, String password);

    boolean delete(User existingUser);

    Page<AdminUserDTO> findAll(Pageable pageable);

    public Page<UserDTO> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable) ;
    Page<UserDTO> getAllUsers(Pageable pageable);

    Optional<User> getUserWithAuthoritiesByLogin(String login);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore();

     Optional<User> findOneByResetKey(String key);
     User createUser(AdminUserDTO userDTO);

}

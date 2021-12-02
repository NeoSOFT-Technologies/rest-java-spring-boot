package com.springboot.rest.domain.service;

import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.UserDTO;
import com.springboot.rest.domain.port.api.UserServicePort;
import com.springboot.rest.domain.port.spi.UserPersistencPort;
import com.springboot.rest.infrastructure.entity.User;
import com.springboot.rest.infrastructure.repository.UserRepository;
import com.springboot.rest.mapper.UserMapper;
import com.springboot.rest.security.SecurityUtils;
import com.springboot.rest.rest.errors.AccountResourceException;
import com.springboot.rest.rest.errors.BadRequestAlertException;
import com.springboot.rest.rest.errors.LoginAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService implements UserServicePort {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserPersistencPort userPersistencePort;
    
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final CacheManager cacheManager;

	/*
	 * public UserService(UserPersistencPort userRepository, PasswordEncoder
	 * passwordEncoder, CacheManager cacheManager) { this.userPersistencePort =
	 * userRepository; this.passwordEncoder = passwordEncoder; this.cacheManager =
	 * cacheManager; }
	 */
    
    public UserService(UserPersistencPort userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, CacheManager cacheManager) {
        this.userPersistencePort = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
    }

    @Override
    public Optional<User> activateRegistration(String key) {
        //log.debug("Activating user for activation key {}", key);
        return userPersistencePort.findOneByActivationKey(key).map(user -> {
            // activate given user for the registration key.
            user.setActivated(true);
            user.setActivationKey(null);
            this.clearUserCaches(user);
            log.debug("Activated user: {}", user);
            return user;
        });
    }

    @Override
    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userPersistencePort.findOneByResetKey(key).filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400))).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetKey(null);
            user.setResetDate(null);
            this.clearUserCaches(user);
            return user;
        });
    }

    @Override
    public Optional<User> requestPasswordReset(String mail) {
        return userPersistencePort.findOneByEmailIgnoreCase(mail).filter(User::isActivated).map(user -> {
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(Instant.now());
            this.clearUserCaches(user);
            return user;
        });
    }

    @Override
    public User registerUser(AdminUserDTO userDTO, String password) {
        userPersistencePort.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userPersistencePort.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });

        return userPersistencePort.save(userDTO, password);

       
    }

    @Override
    public boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userPersistencePort.delete(existingUser);
        this.clearUserCaches(existingUser);
        return true;
    }

    @Override
    public User createUser(AdminUserDTO userDTO) {        
    	
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userPersistencePort.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userPersistencePort.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            return userPersistencePort.createUser(userDTO);

        }
       
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userEntity
     *            user to update.
     * @return updated user.
     */
    @Override
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {

        // UserDTO to User conversion
        User userEntity = userMapper.adminUserDtoToUserEntity(userDTO);
    	
        Optional<User> existingUser = userPersistencePort.findOneByEmailIgnoreCase(userEntity.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userEntity.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userPersistencePort.findOneByLogin(userEntity.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userEntity.getId()))) {
            throw new LoginAlreadyUsedException();
        }

        return Optional.of(userPersistencePort.findById(userEntity.getId())).filter(Optional::isPresent).map(Optional::get).map(user -> {
            this.clearUserCaches(user);
            
            // UserDTO to User conversion
            AdminUserDTO userEntityDTO = userMapper.userEntityToAdminUserDto(userEntity);
            
            userPersistencePort.update(userEntityDTO, user);
            this.clearUserCaches(user);
            log.debug("Changed Information for User: {}", user);
            return user;
        }).map(AdminUserDTO::new);
    }

    @Override
    public void deleteUser(String login) {
        userPersistencePort.findOneByLogin(login).ifPresent(user -> {
            userPersistencePort.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    @Override
    public void saveAccount(AdminUserDTO userDTO, String userLogin) {

        // UserDTO to User conversion
        User userEntity = userMapper.adminUserDtoToUserEntity(userDTO);
    	
        Optional<User> existingUser = userPersistencePort.findOneByEmailIgnoreCase(userEntity.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userPersistencePort.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        
        updateUser(userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getLangKey(), userEntity.getImageUrl());
    }

    /**
     * Update basic information (first name, last name, email, language) for the
     * current user.
     *
     * @param firstName
     *            first name of user.
     * @param lastName
     *            last name of user.
     * @param email
     *            email id of user.
     * @param langKey
     *            language key.
     * @param imageUrl
     *            image URL of user.
     */
    @Override
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin().flatMap(userPersistencePort::findOneByLogin).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            if (email != null) {
                user.setEmail(email.toLowerCase());
            }
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            this.clearUserCaches(user);
            log.debug("Changed Information for User: {}", user);
        });
    }

    @Transactional
    @Override
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin().flatMap(userPersistencePort::findOneByLogin).ifPresent(user -> {
            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new InvalidPasswordException();
            }
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            this.clearUserCaches(user);
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userPersistencePort.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable) {
        return userPersistencePort.findAllByIdNotNullAndActivatedIsTrue(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userPersistencePort.getUserWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userPersistencePort::getUserWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Override
    public void removeNotActivatedUsers() {
        userPersistencePort.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore().forEach(user -> {
            log.debug("Deleting not activated user {}", user.getLogin());
            userPersistencePort.delete(user);
            this.clearUserCaches(user);
        });
    }

    /**
     * Gets a list of all the authorities.
     * 
     * @return a list of all the authorities.
     */
   

    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userPersistencePort.getAllPublicUsers(pageable);
    }

    @Override
    public void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

}

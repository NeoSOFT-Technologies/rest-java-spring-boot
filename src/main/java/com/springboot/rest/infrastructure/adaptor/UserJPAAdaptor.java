package com.springboot.rest.infrastructure.adaptor;

import com.springboot.rest.config.Constants;
import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.UserDTO;
import com.springboot.rest.domain.port.spi.UserPersistencPort;
import com.springboot.rest.infrastructure.entity.Authority;
import com.springboot.rest.infrastructure.entity.User;
import com.springboot.rest.infrastructure.repository.AuthorityRepository;
import com.springboot.rest.infrastructure.repository.UserRepository;
import com.springboot.rest.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserJPAAdaptor implements UserPersistencPort {

    private final Logger log = LoggerFactory.getLogger(UserJPAAdaptor.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final CacheManager cacheManager;

    public UserJPAAdaptor(PasswordEncoder passwordEncoder, CacheManager cacheManager) {
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
    }

    public Optional<User> findOneByActivationKey(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key).map(user -> {
            // activate given user for the registration key.
            user.setActivated(true);
            user.setActivationKey(null);
            this.clearUserCaches(user);
            log.debug("Activated user: {}", user);
            return user;
        });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key).filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400))).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetKey(null);
            user.setResetDate(null);
            this.clearUserCaches(user);
            return user;
        });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail).filter(User::isActivated).map(user -> {
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(Instant.now());
            this.clearUserCaches(user);
            return user;
        });
    }

    public Optional<User> findOneByLogin(String login) {
        return userRepository.findOneByLogin(login);

    }

    public Optional<User> findOneByEmailIgnoreCase(String email) {
        return userRepository.findOneByEmailIgnoreCase(email);

    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);

    }

    @Value("${user-registration.setActivationKey}")
    Boolean setActivationKey;

    public User save(AdminUserDTO userDTO, String password) {

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());

        if(setActivationKey){
            // new user is not active
            newUser.setActivated(false);
            // new user gets registration key
            newUser.setActivationKey(RandomUtil.generateActivationKey());
        }else{
            //USER AUTOMATICALLY IS ACTIVATED WHEN REGISTERS
            newUser.setActivated(true);
            newUser.setActivationKey(null);
        }

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    public User update(AdminUserDTO userDTO,User user) {
               
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (user.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        user.setActivated(userDTO.isActivated());
        user.setLangKey(userDTO.getLangKey());
        Set<com.springboot.rest.infrastructure.entity.Authority> managedAuthorities = user.getAuthorities();
        managedAuthorities.clear();
        userDTO
            .getAuthorities()
            .stream()
            .map(authorityRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(managedAuthorities::add);
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Changed Information for User: {}", user);
        return user;
    
     
    }
    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream().map(authorityRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public boolean delete(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    public Page<UserDTO> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore() {
        return userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));

    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public Optional<User> findOneByResetKey(String key) {
        return userRepository.findOneByResetKey(key);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }
    
  public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
      return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
  }
    
    @Override
    public Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String login) {
      return  userRepository
        .findOneWithAuthoritiesByEmailIgnoreCase(login)       ;
    }
    
    @Override
    public Optional<User> findOneWithAuthoritiesByLogin(String login) {
      return  userRepository
        .findOneWithAuthoritiesByLogin(login)       ;
    }
 
}

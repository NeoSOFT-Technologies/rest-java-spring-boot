package com.springboot.rest.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.dto.UserDTO;
import com.springboot.rest.infrastructure.entity.Authority;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.entity.User;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO} and {@link AdminUserDTO}.
 *
 *With the hard-coded mappers, it could get very tedious in the future 
 *when we have lots of entities with many fields each
 *
 *So, here we are making use of ModelMapper to generate the DTO mappings automatically
 *
 */

@Service
public class UserMapper {

	// inject ModelMapper
	private ModelMapper modelMapper;
	
	////// DTO Mapping strategy for UserMapper //////////

	////////////////////////// 1. Using ModelMapper library /////////////////////
	// Entity to DTO Mapping
	private UserDTO entityToDto(User user) {
		return modelMapper.map(user, UserDTO.class);
	}
	
    public List<UserDTO> entitiesToDtos(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::entityToDto).collect(Collectors.toList());
    }
    
    // User to AdminUserDTO Mapping
	private AdminUserDTO userEntityToAdminUserDto(User user) {
		return modelMapper.map(user, AdminUserDTO.class);
	}
	
    public List<AdminUserDTO> userEntitiesToAdminUserDtos(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userEntityToAdminUserDto).collect(Collectors.toList());
    }
	
	// DTO to entity Mapping
	private User dtoToEntity(UserDTO userDTO) {
		return modelMapper.map(userDTO, User.class);
	}
	
    public List<User> dtosToEntities(List<UserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::dtoToEntity).collect(Collectors.toList());
    }
	
    //////////////////////////2. Hard-coded way /////////////////////
    
	/*
	 * public List<UserDTO> usersToUserDTOs(List<User> users) { return
	 * users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(
	 * Collectors.toList()); }
	 * 
	 * public UserDTO userToUserDTO(User user) { return new UserDTO(user); }
	 * 
	 * public List<AdminUserDTO> usersToAdminUserDTOs(List<User> users) { return
	 * users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).collect
	 * (Collectors.toList()); }
	 * 
	 * public AdminUserDTO userToAdminUserDTO(User user) { return new
	 * AdminUserDTO(user); }
	 * 
	 * public List<User> userDTOsToUsers(List<AdminUserDTO> userDTOs) { return
	 * userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(
	 * Collectors.toList()); }
	 * 
	 * public User userDTOToUser(AdminUserDTO userDTO) { if (userDTO == null) {
	 * return null; } else { User user = new User(); user.setId(userDTO.getId());
	 * user.setLogin(userDTO.getLogin()); user.setFirstName(userDTO.getFirstName());
	 * user.setLastName(userDTO.getLastName()); user.setEmail(userDTO.getEmail());
	 * user.setImageUrl(userDTO.getImageUrl());
	 * user.setActivated(userDTO.isActivated());
	 * user.setLangKey(userDTO.getLangKey()); Set<Authority> authorities =
	 * this.authoritiesFromStrings(userDTO.getAuthorities());
	 * user.setAuthorities(authorities); return user; } }
	 */

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                authoritiesAsString
                    .stream()
                    .map(
                        string -> {
                            Authority auth = new Authority();
                            auth.setName(string);
                            return auth;
                        }
                    )
                    .collect(Collectors.toSet());
        }

        return authorities;
    }

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public UserDTO toDtoId(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        return userDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<UserDTO> toDtoIdSet(Set<User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserDTO> userSet = new HashSet<>();
        for (User userEntity : users) {
            userSet.add(this.toDtoId(userEntity));
        }

        return userSet;
    }

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public UserDTO toDtoLogin(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        return userDto;
    }

    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public Set<UserDTO> toDtoLoginSet(Set<User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserDTO> userSet = new HashSet<>();
        for (User userEntity : users) {
            userSet.add(this.toDtoLogin(userEntity));
        }

        return userSet;
    }
}

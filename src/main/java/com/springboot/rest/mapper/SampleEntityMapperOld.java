package com.springboot.rest.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.dto.UserDTO;
import com.springboot.rest.infrastructure.entity.Authority;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.entity.User;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
@Mapper		// use MapStruct functionality
public class SampleEntityMapperOld {
	
	
    public List<SampleEntityDTO> sampleEntitiesToSampleEntityDTOs(List<SampleEntity> sampleEntities) {
        return sampleEntities.stream().filter(Objects::nonNull).map(this::sampleEntityToSampleEntityDTO).collect(Collectors.toList());
    }

    public SampleEntityDTO sampleEntityToSampleEntityDTO(SampleEntity sampleEntity) {
        return new SampleEntityDTO(sampleEntity);
    }

    public List<User> sampleEntityDTOsToSampleEntities(List<AdminUserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::sampleEntityDTOToSampleEntity).collect(Collectors.toList());
    }

    public User sampleEntityDTOToSampleEntity(AdminUserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

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

    public SampleEntity userFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(id);
        return sampleEntity;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public SampleEntityDTO toDtoId(SampleEntity user) {
        if (user == null) {
            return null;
        }
        SampleEntityDTO userDto = new SampleEntityDTO();
        userDto.setId(user.getId());
        return userDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<SampleEntityDTO> toDtoIdSet(Set<SampleEntity> sampleEntities) {
        if (sampleEntities == null) {
            return Collections.emptySet();
        }

        Set<SampleEntityDTO> sampleEntitySet = new HashSet<>();
        for (SampleEntity sampleEntityEntity : sampleEntities) {
            sampleEntitySet.add(this.toDtoId(sampleEntityEntity));
        }

        return sampleEntitySet;
    }

}

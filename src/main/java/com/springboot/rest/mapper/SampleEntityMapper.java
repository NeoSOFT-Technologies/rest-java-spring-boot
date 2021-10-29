package com.springboot.rest.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
//import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.infrastructure.entity.Authority;
import com.springboot.rest.infrastructure.entity.SampleEntity;

/**
 * Mapper for the entity {@link SampleEntity} and its DTO called {@link SampleEntityDTO}.
 *
 *With the hard-coded mappers, it could get very tedious in the future 
 *when we have lots of entities with many fields each
 *
 *So, here we are making use of ModelMapper to generate the DTO mappings automatically
 *
 */

@Service
public class SampleEntityMapper {
	
	// inject ModelMapper
	private ModelMapper modelMapper;
	
	////// DTO Mapping strategy //////////

	////////////////////////// 1. Using ModelMapper library /////////////////////
	// Entity to DTO Mapping
	private SampleEntityDTO entityToDto(SampleEntity sampleEntity) {
		return modelMapper.map(sampleEntity, SampleEntityDTO.class);
	}
	
    public List<SampleEntityDTO> entitiesToDTOs(List<SampleEntity> sampleEntities) {
        return sampleEntities.stream().filter(Objects::nonNull).map(this::entityToDto).collect(Collectors.toList());
    }
	
	// DTO to entity Mapping
	private SampleEntity dtoToEntity(SampleEntityDTO sampleEntityDTO) {
		return modelMapper.map(sampleEntityDTO, SampleEntity.class);
	}
	
    public List<SampleEntity> dtosToEntities(List<SampleEntityDTO> sampleEntityDTOs) {
        return sampleEntityDTOs.stream().filter(Objects::nonNull).map(this::dtoToEntity).collect(Collectors.toList());
    }
	
	
	//////////////////////////2. Hard-coded way /////////////////////
    
	/*
	 * public List<SampleEntityDTO>
	 * sampleEntitiesToSampleEntityDTOs(List<SampleEntity> sampleEntities) { return
	 * sampleEntities.stream().filter(Objects::nonNull).map(this::
	 * sampleEntityToSampleEntityDTO).collect(Collectors.toList()); }
	 * 
	 * public SampleEntityDTO sampleEntityToSampleEntityDTO(SampleEntity
	 * sampleEntity) { return new SampleEntityDTO(sampleEntity); }
	 * 
	 * public List<User> sampleEntityDTOsToSampleEntities(List<AdminUserDTO>
	 * userDTOs) { return userDTOs.stream().filter(Objects::nonNull).map(this::
	 * sampleEntityDTOToSampleEntity).collect(Collectors.toList()); }
	 * 
	 * public User sampleEntityDTOToSampleEntity(AdminUserDTO userDTO) { if (userDTO
	 * == null) { return null; } else { User user = new User();
	 * user.setId(userDTO.getId()); user.setLogin(userDTO.getLogin());
	 * user.setFirstName(userDTO.getFirstName());
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

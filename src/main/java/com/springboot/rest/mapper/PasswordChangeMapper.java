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

import com.springboot.rest.domain.dto.PasswordChangeDTO;
import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.infrastructure.entity.Authority;
import com.springboot.rest.infrastructure.entity.PasswordChangeEntity;
import com.springboot.rest.infrastructure.entity.SampleEntity;

/**
 * Mapper for the entity {@link PasswordChangeEntity} and its DTO called {@link PasswordChangeDTO}.
 *
 *With the hard-coded mappers, it could get very tedious in the future 
 *when we have lots of entities with many fields each
 *
 *So, here we are making use of ModelMapper to generate the DTO mappings automatically
 *
 */

@Service
public class PasswordChangeMapper {
	
	// inject ModelMapper
	private ModelMapper modelMapper;
	
	////// DTO Mapping strategy //////////

	////////////////////////// Using ModelMapper library /////////////////////
	// Entity to DTO Mapping
	private PasswordChangeDTO entityToDto(PasswordChangeEntity pwdChangeEntity) {
		return modelMapper.map(pwdChangeEntity, PasswordChangeDTO.class);
	}
	
    public List<PasswordChangeDTO> entitiesToDTOs(List<PasswordChangeEntity> pwdChangeEntities) {
        return pwdChangeEntities.stream().filter(Objects::nonNull).map(this::entityToDto).collect(Collectors.toList());
    }
	
	// DTO to entity Mapping
	private PasswordChangeEntity dtoToEntity(PasswordChangeDTO pwdChangeEntityDTO) {
		return modelMapper.map(pwdChangeEntityDTO, PasswordChangeEntity.class);
	}
	
    public List<PasswordChangeEntity> dtosToEntities(List<PasswordChangeDTO> pwdChangeDTOs) {
        return pwdChangeDTOs.stream().filter(Objects::nonNull).map(this::dtoToEntity).collect(Collectors.toList());
    }

    
    
    public PasswordChangeEntity passwordChangeEntityFromId(Long id) {
        if (id == null) {
            return null;
        }
        PasswordChangeEntity pwdChangeEntity = new PasswordChangeEntity();
        pwdChangeEntity.setId(id);
        return pwdChangeEntity;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public PasswordChangeDTO toDtoId(PasswordChangeEntity pwdChangeEntity) {
        if (pwdChangeEntity == null) {
            return null;
        }
        PasswordChangeDTO pwdChangeDto = new PasswordChangeDTO();
        pwdChangeDto.setId(pwdChangeDto.getId());
        return pwdChangeDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<PasswordChangeDTO> toDtoIdSet(Set<PasswordChangeEntity> passwordChangeEntities) {
        if (passwordChangeEntities == null) {
            return Collections.emptySet();
        }

        Set<PasswordChangeDTO> passwordChangeEntitySet = new HashSet<>();
        for (PasswordChangeEntity passwordChangeEntityEntity : passwordChangeEntities) {
        	passwordChangeEntitySet.add(this.toDtoId(passwordChangeEntityEntity));
        }

        return passwordChangeEntitySet;
    }

}

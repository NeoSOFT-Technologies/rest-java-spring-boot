package com.springboot.rest.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
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


//@Component
// @Mapper(componentModel = "spring", imports = UUID.class)	// going to create the spring component
@Mapper		// use MapStruct functionality
public interface SampleEntityMapper {
	
//	SampleEntityMapper INSTANCE = Mappers.getMapper(SampleEntityMapper.class);
	
	
	//	@Mapping(source="sampleEntity.password", target="pwd", defaultValue="password")
	// @Mapping(source="seAux", target="seAuxList")
//	@BeanMapping(ignoreByDefault = true)
	SampleEntityDTO sampleEntityToDTO(SampleEntity sampleEntity);

	@BeanMapping(ignoreByDefault = true)
	List<SampleEntityDTO> sampleEntitiesToDTOs(List<SampleEntity> sampleEntity);
	
	////	@Mapping(source="sampleEntityDto.pwd", target="password", defaultValue="password")
	@InheritInverseConfiguration
	@BeanMapping(ignoreByDefault = true)
	SampleEntity dtoToSampleEntity(SampleEntityDTO sampleEntityDTO);
	


}

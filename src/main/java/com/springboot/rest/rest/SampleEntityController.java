package com.springboot.rest.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.port.api.SampleEntityServicePort;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.repository.SampleEntityRepository;

@RestController
@RequestMapping("/myapi")
public class SampleEntityController {
	
	/*
	@Autowired
	private SampleEntityMapper sampleEntityMapper;
	
	@Autowired
	private SampleEntityRepository sampleEntityRepository;
	*/
	
    private final Logger log = LoggerFactory.getLogger(SampleEntityResource.class);

    private static final String ENTITY_NAME = "a";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

//    private final SampleEntityServicePort sampleEntityServicePort;
	
    /*
	@PostMapping("/samples")
	public ResponseEntity<SampleEntity> save(@RequestBody SampleEntityDTO sampleEntDto) {
		return new ResponseEntity<>(sampleEntityRepository.save(
				sampleEntityMapper.dtoToSampleEntity(sampleEntDto)), HttpStatus.CREATED);
	}
	
	@GetMapping("/samples")
	public ResponseEntity<List<SampleEntityDTO>> getAllSamples() {
		return new ResponseEntity<>(sampleEntityMapper.sampleEntitiesToDTOs(sampleEntityRepository.findAll()), HttpStatus.OK);
	}
	
	@GetMapping("/samples/{id}")
	public ResponseEntity<SampleEntityDTO> getSampleById(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(sampleEntityMapper.sampleEntityToDTO(sampleEntityRepository.findById(id).get()), HttpStatus.OK);
	}
	*/
	
}

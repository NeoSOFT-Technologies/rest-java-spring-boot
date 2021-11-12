package com.springboot.rest.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springboot.rest.domain.dto.AdminUserDTO;
import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.port.api.HttpGatewayServicePort;
import com.springboot.rest.domain.port.api.UserServicePort;
import com.springboot.rest.domain.port.spi.HttpGatewayPersistencePort;
import com.springboot.rest.domain.port.spi.UserPersistencPort;
import com.springboot.rest.domain.service.UserService;
import com.springboot.rest.infrastructure.entity.User;
import com.springboot.rest.mapper.UserMapper;
import com.springboot.rest.security.AuthoritiesConstants;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class HttpGatewayGetRequestTest {
    
    private String testAccept = "";
    private String testAuth = "";
    private String testUrl = "";
    Map<String, String> testHttpHeader = new HashMap<>();
    
    @Autowired
    @MockBean
    private HttpGatewayServicePort httpGatewayServicePort;
    
    @MockBean
    private HttpGatewayPersistencePort httpGatewayPersistencePort;
    
    @InjectMocks
    private HttpGateway httpGateway;

	@BeforeEach
    public void init() {
//		sampleEntity = new SampleEntity();
//		sampleEntity.setId(99l);
//		sampleEntity.setAge(20);
//		sampleEntity.setName("Test Sample");
//		sampleEntity.setPhone(2848);
//		sampleEntity.setPassword("Test@123");
//
//        sampleEntityDto = new SampleEntityDTO(sampleEntity);
		
		testAccept = "application/json";
		testAuth = "Bearer jshdakjfkjegdiufedbkckjds64bh87432bkjcbds";
		testUrl = "http://httpbin.org/get";
		testHttpHeader.put("Accept", testAccept);
		testHttpHeader.put("Auth", testAuth);
		testHttpHeader.put("ExternalURL", testUrl);
		
		
        httpGateway = new HttpGateway();
    }
    
	@Test
	void contextLoads() {
		assertThat(httpGatewayServicePort).isNotNull();
	}
	
	@Test
	void performGHRTest() throws IOException {
		Mockito.when(httpGatewayPersistencePort
				.performGHR(testHttpHeader))
				.thenReturn(null);
		String fetchedGetResponse = httpGateway.performGETHttpReq(testAccept, testAuth, testUrl);
		
		assertNotNull(fetchedGetResponse);
	}
	
	@Test
	void performPHRTest() throws IOException {
		Mockito.when(httpGatewayPersistencePort
				.performPHR(testHttpHeader))
				.thenReturn(null);
		String fetchedPostResponse = httpGateway.performPOSTHttpReq(testAccept, testAuth, testUrl);
		
		assertNotNull(fetchedPostResponse);
	}
	
//    @Test
//    void findSampleEntitiesTest() {
//		List<SampleEntity> entities = new ArrayList<SampleEntity>();
//		
//		Mockito.when(httpGatewayServicePort.findAll().size() > 0)
//				.thenReturn(null);
//		
//		entities = httpGateway.findAll();
//		// testing
//		// System.out.println("Auths: "+authorities);
//		
//		assertNull(entities);
//    }
    
//    @Test
//    void findSampleEntityByIdTest() {
//    	Mockito.when(httpGatewayPersistencePort
//    			.findById(sampleEntityDto.getId())
//    			.isPresent())
//    			.thenReturn(null);    	
//    	Optional<SampleEntity> fetchedSampleEntity = httpGateway.findById(sampleEntity.getId());
//    	
//    	assertNull(fetchedSampleEntity);
//    }
 
}
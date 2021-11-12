package com.springboot.rest.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.domain.port.api.HttpGatewayServicePort;
import com.springboot.rest.domain.port.api.SampleEntityServicePort;

@RestController
@RequestMapping("/httpreq")
public class HttpGatewayResource {

	private final Logger log = LoggerFactory.getLogger(HttpGatewayResource.class);

	private static final String accept = "Accept";
	
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HttpGatewayServicePort httpGatewayServicePort;   
    public HttpGatewayResource(HttpGatewayServicePort httpGatewayServicePort) {
        this.httpGatewayServicePort = httpGatewayServicePort;
    }
	
	
    // API calls' methods
    
	@GetMapping("/message")
	public String message() {
		return "Message from XXXXXXXXXXX HttpGatewayResource......";
	}
	
	@PostMapping("/testHR")
	public ResponseEntity<Map<String, String>> testHttpReq(
			@RequestHeader(value=accept) String acceptHeader,
			@RequestHeader(value="Auth") String authHeader
			) {
		Map<String, String> returnValue = new HashMap<>();
		returnValue.put(accept, acceptHeader);
		returnValue.put("Auth", authHeader);
		
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@PostMapping("/getHR")
	public ResponseEntity<String> createGETHttpReq(
			@RequestHeader(value=accept) String acceptHeader,
			@RequestHeader(value="Auth") String authHeader,
			@RequestHeader(value="ExternalURL") String urlHeader
			) throws IOException {
		
		Map<String, String> returnValue = new HashMap<>();
		returnValue.put(accept, acceptHeader);
		returnValue.put("Auth", authHeader);
		returnValue.put("ExternalURL", urlHeader);
		
		String httpHeader = httpGatewayServicePort.performGHR(returnValue);
	
		return ResponseEntity.status(HttpStatus.OK).body(httpHeader);
	}
	
	@PostMapping("/postHR")
	public ResponseEntity<String> createPOSTHttpReq(
			@RequestHeader(value=accept) String acceptHeader,
			@RequestHeader(value="Auth") String authHeader,
			@RequestHeader(value="ExternalURL") String urlHeader
			) throws IOException {
		
		Map<String, String> returnValue = new HashMap<>();
		returnValue.put(accept, acceptHeader);
		returnValue.put("Auth", authHeader);
		returnValue.put("ExternalURL", urlHeader);
		
		String httpHeader = httpGatewayServicePort.performPHR(returnValue);
	
		return ResponseEntity.status(HttpStatus.OK).body(httpHeader);
	}
}

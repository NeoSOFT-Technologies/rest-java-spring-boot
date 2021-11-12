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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/httpreq")
public class HttpGatewayResource {

	private final Logger log = LoggerFactory.getLogger(HttpGatewayResource.class);

	private static final String accept = "Accept";
	private static final String auth = "Auth";
	private static final String url = "ExternalURL";
	private static final String params = "Params";
	
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HttpGatewayServicePort httpGatewayServicePort;   
    public HttpGatewayResource(HttpGatewayServicePort httpGatewayServicePort) {
        this.httpGatewayServicePort = httpGatewayServicePort;
    }
	
	
    // API calls' methods
    
    // Test API call
	@GetMapping("/message")
	@Operation(summary = "/message", security = @SecurityRequirement(name = "bearerAuth"))
	public String message() {
		return "Message from HttpGatewayResource......";
	}
	
	// GET Request API call to external URL
	@PostMapping("/get-request")
	@Operation(summary = "/get-request", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> createGETRequest(
			@RequestHeader(value=accept) String acceptHeader,
			@RequestHeader(value=auth) String authHeader,
			@RequestHeader(value=url) String urlHeader,
			@RequestHeader(value=params) String paramsHeader
			) throws IOException {
		Map<String, String> returnValue = new HashMap<>();
		returnValue.put(accept, acceptHeader);
		returnValue.put(auth, authHeader);
		returnValue.put(url, urlHeader);
		returnValue.put(params, paramsHeader);
		
		String httpHeader = httpGatewayServicePort.makeGetRequest(returnValue);
		
		return ResponseEntity.status(HttpStatus.OK).body(httpHeader);
	}
	
	// POST Request API call to external URL
	@PostMapping("/post-request")
	@Operation(summary = "/post-request", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> createPOSTRequest(
			@RequestHeader(value=accept) String acceptHeader,
			@RequestHeader(value=auth) String authHeader,
			@RequestHeader(value=url) String urlHeader,
			@RequestHeader(value=params) String paramsHeader
			) throws IOException {
		Map<String, String> returnValue = new HashMap<>();
		returnValue.put(accept, acceptHeader);
		returnValue.put(auth, authHeader);
		returnValue.put(url, urlHeader);
		returnValue.put(params, paramsHeader);
		
		String httpHeader = httpGatewayServicePort.makePostRequest(returnValue);
		
		return ResponseEntity.status(HttpStatus.OK).body(httpHeader);
	}
	
}

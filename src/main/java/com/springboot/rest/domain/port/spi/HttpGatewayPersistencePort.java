package com.springboot.rest.domain.port.spi;


import java.io.IOException;
import java.util.Map;


public interface HttpGatewayPersistencePort {
	
	String makeGetRequest(Map<String, String> httpHeader) throws IOException;
	
	String makePostRequest(Map<String, String> httpHeader) throws IOException;
	
}
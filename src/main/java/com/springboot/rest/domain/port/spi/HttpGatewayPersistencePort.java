package com.springboot.rest.domain.port.spi;


import java.io.IOException;
import java.util.Map;


public interface HttpGatewayPersistencePort {
	
	String performGHR(Map<String, String> httpHeader) throws IOException;
	
	String performPHR(Map<String, String> httpHeader) throws IOException;
	
}
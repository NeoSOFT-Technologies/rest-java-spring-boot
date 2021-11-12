package com.springboot.rest.domain.port.api;


import java.io.IOException;
import java.util.Map;


public interface HttpGatewayServicePort {
	
	String performGHR(Map<String, String> httpHeader) throws IOException;
	
	String performPHR(Map<String, String> httpHeader) throws IOException;


}

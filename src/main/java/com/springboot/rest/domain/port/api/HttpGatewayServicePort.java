package com.springboot.rest.domain.port.api;


import java.io.IOException;
import java.util.Map;


public interface HttpGatewayServicePort {
	
	String makeGetRequest(Map<String, String> httpHeader) throws IOException;
	
	String makePostRequest(Map<String, String> httpHeader) throws IOException;


}

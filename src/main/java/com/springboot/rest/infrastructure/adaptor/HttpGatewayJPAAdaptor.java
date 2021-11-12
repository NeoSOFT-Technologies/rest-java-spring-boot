package com.springboot.rest.infrastructure.adaptor;

import com.springboot.rest.domain.port.spi.HttpGatewayPersistencePort;
import com.springboot.rest.gateway.HttpGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class HttpGatewayJPAAdaptor implements HttpGatewayPersistencePort {

    private final HttpGateway httpGateway;
    
    public HttpGatewayJPAAdaptor(HttpGateway httpGateway) {
        this.httpGateway = httpGateway;
    }


    // Implementations
    
	@Override
	public String performGHR(Map<String, String> httpHeader) throws IOException {
		
		return httpGateway.performGETHttpReq(httpHeader.get("Accept")
										, httpHeader.get("Auth")
										, httpHeader.get("ExternalURL"));
	}
	
	@Override
	public String performPHR(Map<String, String> httpHeader) throws IOException {
		
		return httpGateway.performPOSTHttpReq(httpHeader.get("Accept")
										, httpHeader.get("Auth")
										, httpHeader.get("ExternalURL"));
	}


}
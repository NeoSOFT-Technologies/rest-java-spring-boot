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
	public String makeGetRequest(Map<String, String> httpHeader) throws IOException {
		return httpGateway.performGETRequest(httpHeader);
	}


	@Override
	public String makePostRequest(Map<String, String> httpHeader) throws IOException {
		return httpGateway.performPOSTRequest(httpHeader);
	}

}
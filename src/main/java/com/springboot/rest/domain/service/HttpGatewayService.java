package com.springboot.rest.domain.service;

import com.springboot.rest.domain.port.api.HttpGatewayServicePort;
import com.springboot.rest.domain.port.spi.HttpGatewayPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;


@Service
@Transactional
public class HttpGatewayService implements HttpGatewayServicePort {


    private final HttpGatewayPersistencePort httpGatewayPersistencePort;

    public HttpGatewayService(HttpGatewayPersistencePort httpGatewayPersistencePort) {
        this.httpGatewayPersistencePort = httpGatewayPersistencePort;
    }

	@Override
	public String makeGetRequest(Map<String, String> httpHeader) throws IOException {
		return httpGatewayPersistencePort.makeGetRequest(httpHeader);
	}

	@Override
	public String makePostRequest(Map<String, String> httpHeader) throws IOException {
		return httpGatewayPersistencePort.makePostRequest(httpHeader);
	}

}
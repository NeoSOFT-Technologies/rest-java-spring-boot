package com.springboot.rest.gateway;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class HttpGateway {
	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";

	private static final String params = "hero=IronMan&power=BeingRich";
	
	public String performGETHttpReq(
							String acceptHeader, String authHeader, String urlHeader) throws IOException {
		Map<String, String> httpHeader = new HashMap<>();
		httpHeader.put("Accept", acceptHeader);
		httpHeader.put("Auth", authHeader);
		httpHeader.put("ExternalURL", urlHeader);
		
		// Set up the connection: creating request :---
		URL url = new URL(urlHeader + "?" + params);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		
		// Adding request parameters :---
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
	
		// Setting Request Headers :---
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		
		// Setting the timeouts :---
		con.setConnectTimeout(5000);
		con.setReadTimeout(6000);
		
		
		// Reading the response (handling the failed requests) :---
		int status = con.getResponseCode();
		System.out.println("'GET' request to URL : " + url);
		System.out.println("Response Code : " + status);
		System.out.println("Response Body : ");

		Reader streamReader = null;
		if(status > 299) {
			streamReader = new InputStreamReader(con.getErrorStream());
		} else {
			streamReader = new InputStreamReader(con.getInputStream());
		}
		
		// read response content
		BufferedReader in = new BufferedReader(streamReader);
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		
		// print content on console
		System.out.println(content.toString());
		
		// Retrieve "full response"
		StringBuilder fullResponseContent;
		fullResponseContent = FullResponseBuilder.getFullResponse(con);
		
		// Add response content
		fullResponseContent.append("\n"+content);
		
		// Perform connection disconnection
		con.disconnect();
		
		System.out.println("GET done.\n");
		
		return fullResponseContent.toString();
	}
	
	
	public String performPOSTHttpReq(
			String acceptHeader, String authHeader, String urlHeader) throws IOException {	
		
		Map<String, String> httpHeader = new HashMap<>();
		httpHeader.put("Accept", acceptHeader);
		httpHeader.put("Auth", authHeader);
		httpHeader.put("ExternalURL", urlHeader);
		
		// Set up the connection: creating request :---
		URL url = new URL(urlHeader);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		
		// Adding request parameters :---
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "value1");
		
		// Setting Request Headers :---
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
//		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.writeBytes(params);
		out.flush();
		out.close();
		
		// Setting the timeouts :---
		con.setConnectTimeout(5000);
		con.setReadTimeout(6000);
		
		
		// Reading the response (handling the failed requests) :---
		int status = con.getResponseCode();
		System.out.println("'POST' request to URL : " + url);
		System.out.println("Response Code : " + status);
		System.out.println("Response Body : ");
		Reader streamReader = null;
		
		if(status > 299) {
			streamReader = new InputStreamReader(con.getErrorStream());
		} else {
			streamReader = new InputStreamReader(con.getInputStream());
		}
		
		// read response content
		BufferedReader in = new BufferedReader(streamReader);
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		
		// print content on console
		System.out.println(content.toString());
		
		// Retrieve "full response"
		StringBuilder fullResponseContent;
		fullResponseContent = FullResponseBuilder.getFullResponse(con);
		
		// Add response content
		fullResponseContent.append("\n"+content);
		
		// Perform connection disconnection
		con.disconnect();
		
		System.out.println("POST done.\n");
		
		return fullResponseContent.toString();

	}
}

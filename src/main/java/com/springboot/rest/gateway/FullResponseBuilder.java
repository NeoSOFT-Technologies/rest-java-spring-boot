package com.springboot.rest.gateway;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

public class FullResponseBuilder {
    public static StringBuilder getFullResponse(HttpURLConnection con) throws IOException {
        StringBuilder fullResponseBuilder = new StringBuilder();

        // read status and message
        fullResponseBuilder.append(con.getResponseCode())
        .append(" ")
        .append(con.getResponseMessage())
        .append("\n");
        
        // read headers
		con.getHeaderFields().entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> {
			fullResponseBuilder.append(entry.getKey()).append(": ");
			List<String> headerValues = entry.getValue();
			Iterator<String> it = headerValues.iterator();
			if (it.hasNext()) {
				fullResponseBuilder.append(it.next());
				while (it.hasNext()) {
					fullResponseBuilder.append(", ").append(it.next());
				}
			}
			fullResponseBuilder.append("\n");
		});
        
        // read response content
		
		
        return fullResponseBuilder;
    }
}

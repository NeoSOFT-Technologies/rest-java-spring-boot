package com.springboot.rest.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestResource {

	@GetMapping("/message")
	public String message() {
		return "Testing..........###\nTest2..........\ntest3...........#####";
	}
	

}

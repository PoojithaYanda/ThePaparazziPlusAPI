package com.txst.restapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.txst.restapi.model.Greeting;

@RestController
public class GreetingController {

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "id", defaultValue = "1") String id) {
		return new Greeting(Integer.parseInt(id));
	}
}
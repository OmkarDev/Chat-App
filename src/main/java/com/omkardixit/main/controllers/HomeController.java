package com.omkardixit.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.omkardixit.main.model.Greeting;

@Controller
public class HomeController {

	@Autowired
	private SimpMessagingTemplate template;
	
	@GetMapping("/home")
	public String home() {
		Greeting greeting = new Greeting("SO SO SO BAD");
		template.convertAndSend("/topic/greetings",greeting);
		return "home";
	}
	
}

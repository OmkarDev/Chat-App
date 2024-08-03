package com.omkardixit.main.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.omkardixit.main.model.Greeting;
import com.omkardixit.main.model.Message;

@Controller
public class WebSocketController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(Message message) {
		System.out.println(message);
		return new Greeting("Hello, " + message.getSender() + "! with content " + message.getContent());
	}

}

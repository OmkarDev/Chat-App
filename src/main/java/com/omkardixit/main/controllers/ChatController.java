package com.omkardixit.main.controllers;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.util.HtmlUtils;

import com.omkardixit.main.model.ChatMessage;
import com.omkardixit.main.model.ChatUser;
import com.omkardixit.main.model.User;
import com.omkardixit.main.services.UserService;

import jakarta.annotation.PostConstruct;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private UserService userService;

	List<ChatUser> usersDTO = new ArrayList<>();

	@PostConstruct
	public void init() {
		List<User> usersReal = userService.getAllUsers();
		for (User user : usersReal) {
			usersDTO.add(new ChatUser(user.getUsername(), false));
		}
	}

	@MessageMapping("/public/message")
	public void sendPublicMessage(ChatMessage chatMessage, Principal principal) {
		String message = HtmlUtils.htmlEscape(chatMessage.getContent());
		String username = principal.getName();
		chatMessage = new ChatMessage(chatMessage.getUid(), username, message, null, Instant.now());
		template.convertAndSend("/topic/public-messages", chatMessage);
	}

	@MessageMapping("/private/message")
	public void sendPrivateMessage(ChatMessage chatMessage, Principal principal) {
		String message = HtmlUtils.htmlEscape(chatMessage.getContent());
		String username = principal.getName();
		String receiver = chatMessage.getReceiver();
		String receiverId = null;
		String userId = null;
		for (User user : userService.getAllUsers()) {
			if (user.getUsername().equals(receiver)) {
				receiverId = user.getId();
			}
			if(user.getUsername().equals(principal.getName())) {
				userId = user.getId();
			}
		}
		chatMessage = new ChatMessage(null, username, message, null, Instant.now());
		template.convertAndSendToUser(receiverId, "/topic/private-messages", chatMessage);
		template.convertAndSendToUser(userId, "/topic/private-messages", chatMessage);
	}

	@MessageMapping("/public/status")
	public void setStatus(String status, Principal principal) {
		if (principal == null) {
			return;
		}
		boolean online = false;
		if (status.equals("online")) {
			online = true;
		}
		for (ChatUser user : usersDTO) {
			if (user.getUsername().equals(principal.getName())) {
				user.setOnline(online);
			}
		}
		template.convertAndSend("/topic/users", usersDTO);
	}

	@EventListener
	public void handleSessionConnectEvent(SessionConnectEvent event) {
		System.out.println("Session Connect Event");
//		template.convertAndSendToUser(null, null, event);
	}

	@EventListener
	public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
		System.out.println("Session has disconnected or so it seems");
//		var users = userService.getAllUsers();
//		for (User user : users) {
//
//		}
	}
}

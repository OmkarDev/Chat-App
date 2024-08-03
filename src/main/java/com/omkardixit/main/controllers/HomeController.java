package com.omkardixit.main.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.services.UserService;


@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	UserService userService;

	@GetMapping("")
	public String home(Principal pricipal,Model model) {
		String username = pricipal.getName();
		model.addAttribute("username", username);
		model.addAttribute("userId", userService.getUserByUsername(username).get().getId());
		return "chat";
	}
	
	
}

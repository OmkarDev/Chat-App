package com.omkardixit.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omkardixit.main.model.User;
import com.omkardixit.main.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<User> getUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAllUsers();
	}

}

package com.omkardixit.main.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.omkardixit.main.model.User;

@Repository
public class UserRepository {

	List<User> users = new ArrayList<>();
	
	public UserRepository() {
		User[] users = {
				new User("Alice","1234"),
				new User("Bob","1234"),
				new User("Candice","1234"),
				new User("Dave","1234"),
				new User("Eve","1234"),
		};
		this.users = Arrays.stream(users).collect(Collectors.toList());
	}
	
	public Optional<User> findUserByUsername(String username){
		for(User user:users) {
			if(user.getUsername().equals(username)) {
				return Optional.of(user);
			}
		}
		return Optional.ofNullable(null);
	}
	
	public List<User> findAllUsers(){
		return users;
	}
	
	
}

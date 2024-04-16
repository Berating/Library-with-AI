package com.bookStore.Service;

import java.io.Console;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bookStore.Entity.User;
import com.bookStore.Repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
		
	}
	
	public User registerUser(String username, String password, String repeatPassword, String email) {
		
		if(username.isBlank()  || 
			email.isBlank() 	||
			password.isBlank()  || 
			!password.equals(repeatPassword)) {
			return null;
		}
		User user = new User(username, password, email);
		
		return userRepository.save(user);
	}
	
	public  User testMethoda(String username) {
		return null;
	}
	
	public List<User> getAll(){
		return userRepository.findAll();
	}	
	
}

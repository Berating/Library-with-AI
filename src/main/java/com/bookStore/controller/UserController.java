package com.bookStore.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookStore.Entity.User;
import com.bookStore.Service.UserService;

@RestController
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}	
	
	@PostMapping(path = "/register")
	public User register( @RequestParam(value = "email") String email, 
								@RequestParam(value = "username") String username, 
								@RequestParam(value = "password") String password, 
								@RequestParam(value = "repeatPassword") String repeatPassword) {
		System.out.println("gsdfgdsfdsfsdfdsfds");
		return userService.registerUser(username, password, repeatPassword, email);	
		
	}
}

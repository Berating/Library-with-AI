package com.bookStore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class StoreController {
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/shop")
	public String shop() {
		return "shop";
	}
	
	@GetMapping("/testimonial")
	public String testimonial() {
		return "testimonial";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	@GetMapping("/why")
	public String why() {
		return "why";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
}

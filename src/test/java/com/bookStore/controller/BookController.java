package com.bookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookStore.Entity.Book;
import com.bookStore.Service.BookService;

@RestController
public class BookController {
	
	private BookService bookService;
	
	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@PostMapping("/registerBook")
	public Book registerBook( 	@RequestParam(value = "bookName") String bookName, 
								@RequestParam(value = "author") String author, 
								@RequestParam(value = "genre") List<String> genre, 
								@RequestParam(value = "publisher") String publisher,
								@RequestParam(value = "countryOrigin") String countryOrigin,
								@RequestParam(value = "language") String language,
								@RequestParam(value = "bookType") String bookType) {
		System.out.println("gsdfgdsfdsfsdfdsfds");
		return bookService.addBook(bookName, author, genre, publisher, countryOrigin, language, bookType);
		
	}

}

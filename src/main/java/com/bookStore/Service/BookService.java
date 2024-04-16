package com.bookStore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.Entity.Book;
import com.bookStore.Repository.BookRepository;

@Service
public class BookService {
	
	private BookRepository bookRepository;
	
	@Autowired
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	public Book addBook(String bookName, String author, List<String> genre, String publisher, String countryOrigin, String language, String bookType) {
		
		if(	bookName.isBlank()  || 
			author.isBlank() 	||
			genre.isEmpty()  || 
			publisher.isBlank()  || 
			countryOrigin.isBlank()  || 
			language.isBlank()  || 
			bookType.isBlank()
			) {
			return null;
		}
		Book book = new Book();
		
		return bookRepository.save(book);
	}
	
	public  Book testMethoda(String username) {
		return null;
	}
	
	
	public List<Book> getAll(){
		return bookRepository.findAll();
	}
	
}

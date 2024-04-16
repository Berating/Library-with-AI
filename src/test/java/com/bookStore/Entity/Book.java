package com.bookStore.Entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "bookname", nullable = false)
	private String bookName;
	
	@Column(name = "author", nullable = false)
	private String author;
	
	@Column(name = "genre", nullable = false)
	private List<String> genre;
	
	@Column(name = "publisher", nullable = false)
	private String publisher;
	
	@Column(name = "countryorigin", nullable = false)
	private String countryOrigin;
	
	@Column(name = "language", nullable = false)
	private String language;
	
	@Column(name = "booktype", nullable = false)
	private String bookType;
	
	@Transient
	private String IdAsString;
	
	@Autowired
	public Book(String bookName, String author, List<String> genre, String publisher, String countryOrigin, String language, String bookType) {
		this.bookName = bookName;
		this.author = author;
		this.genre = genre;
		this.publisher = publisher;
		this.countryOrigin = countryOrigin;
		this.language = language;
		this.bookType = bookType;
	}

	public Book() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getIdAsString() {
	    return String.valueOf(id);
	}
	
	public void setIdAsString(String IdAsString) {
        this.IdAsString = IdAsString;
    }

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getGenre() {
		return genre;
	}

	public void setGenre(List<String> genre) {
		this.genre = genre;
	}
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCountryOrigin() {
		return countryOrigin;
	}

	public void setCountryOrigin(String countryOrigin) {
		this.countryOrigin = countryOrigin;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	
	@Override
    public String toString() {
        return "Book { " +
                " Book Name = '" + bookName + '\'' +
                " Author = '" + author + '\'' +
                " Genre = '" + genre + '\'' +
                " Publisher = '" + publisher + '\'' +
                " Country of Origin = '" + countryOrigin + '\'' +
                " Language = '" + language + '\'' +
                ", Book Type = " + bookType +
                '}';
                
    }
	
	
}

package com.bookStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookStore.Entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer>{
	Book findByBookName(String bookName);
	Book findByAuthor(String author);
	Book findById(int id);
	@Query("SELECT b FROM Book b WHERE b.genre IN :genres")
    List<Book> findByGenres(@Param("genres") List<String> genres);
	Book findByPublisher(String publisher);
	Book findByCountryOrigin(String countryOrigin);
	Book findByLanguage(String language);
	Book findByBookType(String bookType);
    List<Book> findAll();
}

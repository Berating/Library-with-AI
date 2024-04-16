package com.bookStore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bookStore.Agent.BookAgent;
import com.bookStore.Agent.ClientAgent;
import com.bookStore.Entity.Book;
import com.bookStore.Ontology.LibraryOntology;

@Controller // Use @Controller for handling HTML views
public class AgentController {
	
	@Autowired
    private ClientAgent clientAgent;
    private final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @GetMapping("/books")
    public String searchBooks(@RequestParam("genre") String genre, Model model) {
        logger.info("Received genre: {}", genre);

        // Send the genre query to the ClientAgent
        clientAgent.setSearchedGenre(genre);

        // Assuming you have a method in ClientAgent to get search results directly
        List<Book> searchResults = clientAgent.getSearchResults();

        logger.info("Search results size: {}", searchResults.size());
        model.addAttribute("books", searchResults);
        logger.info("Added {} books to the model", searchResults.size());

        // Return the HTML template for displaying the results
        return "shop";
    }
}


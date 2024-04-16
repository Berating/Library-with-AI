package com.bookStore.Entity;

import java.util.ArrayList;
import java.util.List;

import com.bookStore.Agent.BookAgent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookAgentEntity {
	
	BookAgent myBookAgent = new BookAgent();
	
	public List<String> genre;
	
	public BookAgentEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Book> getBookByGenre(String genre){
		return getBookByGenre(genre);
	}

}

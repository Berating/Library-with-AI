package com.bookStore.Agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.stereotype.Component;

import com.bookStore.Entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@Component
public class ClientAgent extends Agent{

	private ClientAgentGUI gui;
	private String searchedGenre = null;
	private ArrayList<AID> readers;
	
	private List<Book> searchResults = new ArrayList<>();
	
	@Override
	protected void setup() {
		gui = new ClientAgentGUI(this);
		
		addBehaviour(new TickerBehaviour(this, 2000) {
			
			@Override
			protected void onTick() {
				
				if(searchedGenre != null) {
					System.out.println("Търся книга с жанр" + 
							searchedGenre);
					
					DFAgentDescription dfd = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					
					sd.setType("library");
					
					dfd.addServices(sd);
					
					try {
						
						DFAgentDescription[] descrptions = 
								DFService.search(myAgent, dfd);
						
						readers = new ArrayList<>();
						
						for(int i = 0; i < descrptions.length; i++) {
							readers.add(descrptions[i].getName());
						}
						
					} catch (FIPAException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
					
					if(readers.size() > 0) {
						myAgent.addBehaviour(new SearchingBehaviour());
					}else {
						System.out.println("Няма книжарници");
					}
						
				}
				
			}
		});
		
	}

	public String getSearchedGenre() {
		return searchedGenre;
	}

	public void setSearchedGenre(String searchedGenre) {
		this.searchedGenre = searchedGenre;
	}
	
	public List<Book> getSearchResults() {
	    return searchResults;
	}
	
	private class SearchingBehaviour extends Behaviour{

		int step = 0;
		MessageTemplate mt;
		int repliesCount = 0;
		
		Book[] bookArray;
		
		@Override
		public void action() {
			switch(step) {
			case 0:
				
				System.out.println("Започвам запитване за книга с жанр" + searchedGenre);
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				
				for(int i=0; i < readers.size(); i++) {
					cfp.addReceiver(readers.get(i));
				}
				
				cfp.setContent(searchedGenre);
				cfp.setConversationId("book stuff");
				cfp.setReplyWith("cfp" 
						+ System.currentTimeMillis());
				
				mt = MessageTemplate.and(
						MessageTemplate.MatchConversationId("book stuff"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith())
						);
				
				send(cfp);
				
				step++;
				
				break;
				
			case 1:
				
				ACLMessage reply = receive(mt);
				
				if(reply != null) {
					
					if(reply.getPerformative() == ACLMessage.PROPOSE) {
						ObjectMapper mapper = new ObjectMapper();
						
						try {
							bookArray = mapper.readValue(
									reply.getContent(), Book[].class);
							
							 // Add the received books to search results
                            searchResults.addAll(Arrays.asList(bookArray));

							
							for(int i = 0; i < bookArray.length; i++) {
								System.out.println(bookArray[i]);
							}
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					repliesCount++;
					
					System.out.println("Нов отоговор");
					if(repliesCount >= readers.size()) {
						System.out.println("Преминаваме към края");
						step++;
					}
					
				}
				
				break;
				
			}
			
		}
		
		

		@Override
		public boolean done() {
			if(step == 2) {		
								
				if(bookArray == null || bookArray.length == 0)
				{
					System.out.println("Нема книга.... нема книга..");
				}
				
				searchedGenre = null;
				
				removeBehaviour(this);
				
				return true;
			}
			
			return false;
		}
		
	}
}

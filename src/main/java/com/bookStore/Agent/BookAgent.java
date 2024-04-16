package com.bookStore.Agent;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.bookStore.Entity.Book;
import com.bookStore.Ontology.LibraryOntology;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookAgent extends Agent{
	
	private LibraryOntology bookOntology;
	
	@Override
	protected void setup() {
		
		bookOntology = new LibraryOntology();
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("library");
		sd.setName("cool book");
		
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addBehaviour(new BookReqestBehaviour());
		
	}
	
	public void createNewBookType(String bookType) throws OWLOntologyStorageException {
		bookOntology.addBookType(bookType);
	}
	
	public void addGenreToBook(String bookType, String bookGenre) throws OWLOntologyStorageException {
		bookOntology.addGenreToBook(bookType, bookGenre);
	}

	private class BookReqestBehaviour extends CyclicBehaviour{

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.
					MatchPerformative(ACLMessage.CFP);
			
			ACLMessage msg = receive(mt);
			
			if(msg != null) {
				
				
				String bookGenre = msg.getContent();
				System.out.println("Някой търси книга с " + bookGenre);
				
				ACLMessage reply = msg.createReply();
				
				ArrayList<Book> result = 
						bookOntology.getBookByGenre(bookGenre);
				
				if(result.size() > 0) {
					
					System.out.println("Имам такива книга");
					ObjectMapper mapper = new ObjectMapper();
					
					reply.setPerformative(ACLMessage.PROPOSE);
					
					try {
						reply.setContent(
								mapper.writeValueAsString(result));
					
						reply.setLanguage("JSON");
						
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}					
				}else {
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("немааааа");
					System.out.println("Няма такава книга");
				}
				
				send(reply);				
			}
			
			
		}		
	}

	public void changeGenreName(String oldNameGenre, String newNameGenre) {
		bookOntology.renameGenre(oldNameGenre, newNameGenre);		
	}

	public void deleteBook(String nameBookToDelete) {
		bookOntology.removeBook(nameBookToDelete);		
	}
	
}

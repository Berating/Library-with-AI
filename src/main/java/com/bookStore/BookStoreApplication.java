package com.bookStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


@SpringBootApplication
public class BookStoreApplication {

	public static void main(String[] args) {
		
		Runtime rt = Runtime.instance();
		
		Profile profile = new ProfileImpl();
		
		profile.setParameter(Profile.MAIN_HOST, "localhost");
		profile.setParameter(Profile.MAIN_PORT, "1099");
		profile.setParameter(Profile.GUI, "true");
		
		AgentContainer mainContainer =  rt.createMainContainer(profile); //Start jade container gui
		
		try {
			//add agents to container gui
			AgentController ag1 = mainContainer.createNewAgent("Book Agent", "com.bookStore.Agent.BookAgent", null);
			AgentController ag2 = mainContainer.createNewAgent("Reader", "com.bookStore.Agent.ClientAgent", null);

			
			ag1.start();
			ag2.start();
			
			System.out.println("Agents started!");
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SpringApplication.run(BookStoreApplication.class, args);
	}
}
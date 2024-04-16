package com.bookStore.Agent;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class ClientAgentGUI extends JFrame {

	
	public ClientAgentGUI(ClientAgent agent) {
		
		Container cp = getContentPane();
		
		JTextField genreNameTF = new JTextField();
		
		JButton searchButton = new JButton("Search");
		
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String genreName = genreNameTF.getText();
				
				if(genreName != null && 
						genreName.length() > 0) {
					agent.setSearchedGenre(genreName);
				}
				
			}
		});
		
		Box content = Box.createHorizontalBox();
		
		content.add(Box.createRigidArea(new Dimension(5, 1)));
		content.add(genreNameTF);
		content.add(Box.createRigidArea(new Dimension(5, 1)));
		content.add(searchButton);
		content.add(Box.createRigidArea(new Dimension(5, 1)));
		
		cp.add(content);
		
		this.setSize(300, 100);
		setVisible(true);
	}
	
}

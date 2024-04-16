package com.bookStore.Entity;
import java.util.Set;

//import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	    @Id
		@Column(name = "id", nullable = false)
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    
	    @Column(name = "username", nullable = false)
	    private String username;
	    
	    @Column(name = "pass", nullable = false)
	    private String pass;
	    
	    @Column(name = "email", nullable = false)
	    private String email;
	    
	    public User() {
	    }
	    
	    @Autowired
	    public User(String username, String pass, String emal) {
	        this.username = username;
	        this.pass = pass;
	        this.email = emal;
	    }
	    
	    public int getId() {
	        return id;
	    }
	    
	    public void setId(int id) {
	        this.id = id;
	    }
	    
	    public String getUsername() {
	        return username;
	    }
	    
	    public void setUsername(String username) {
	        this.username = username;
	    }
	    
	    public String getPass() {
	        return pass;
	    }
	    
	    public void setPass(String pass) {
	        this.pass = pass;
	    }
	    
	    public String getEmail() {
	        return email;
	    }
	    
	    public void setEmail(String email) {
	        this.email = email;
	    }
	    
	    @Override
	    public String toString() {
	        return "User { " +
	                " Username = '" + username + '\'' +
	                ", Email = " + email +
	                '}';
	    }
	

}

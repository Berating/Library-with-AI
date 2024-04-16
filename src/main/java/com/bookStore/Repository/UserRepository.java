/*package holidayplanner.HolidayPlanner.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
		
		UserEntity findUserByUsernameAndPassword
						(String username, String password);
		
		UserEntity findByUsername(String username);

	}*/

package com.bookStore.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.Entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(int id);
    User findUserByUsernameAndPass(String username, String password);
    List<User> findAll();
    
}




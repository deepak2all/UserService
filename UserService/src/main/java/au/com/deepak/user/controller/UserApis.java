package au.com.deepak.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import au.com.deepak.user.model.User;

public interface UserApis {

	public ResponseEntity<List<User>> getAllUsers() throws InterruptedException;
	
	//public ResponseEntity<User> getUserById(long userid);
	
	//public ResponseEntity<User> getUserById(String userid);
	
	//public ResponseEntity<User> createUser(User user);
		
	public ResponseEntity<User> updateUser(User userDetails);
	
	//public void deleteUser(String userid);
}

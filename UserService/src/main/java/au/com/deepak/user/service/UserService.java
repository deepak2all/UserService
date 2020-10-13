/**
 * 
 */
package au.com.deepak.user.service;

import java.util.List;

import au.com.deepak.user.model.User;

/**
 * Interface with CRUD methods
 */
public interface UserService {

	// Create / Save the record
	User createUser(User user);
	
	// Update a record
	void updateUser(long newUserId, User newUserDetails);
	
	// Get all the records
	List<User> getAllUsers();
	
	// Get a single record using Id
	User getUserById(long userId);
	
	// Delete a record
	void deleteUser(long userId);
	
	// Save All / Multiple records
	List<User> saveAll(List<User> users);
	
	// Delete All records
	void deleteAllUsers();
	
}
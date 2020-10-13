package au.com.deepak.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.deepak.user.exception.ResourceNotFoundException;
import au.com.deepak.user.model.User;
import au.com.deepak.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for defining the business logic
 * In general, it makes a call to DAO which then makes DB calls
 */
@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Creating / saving a specific record by using the method save() of CrudRepository
	 */
	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * Updating a specific record by using the method save() of CrudRepository
	 */
	@Override
	public void updateUser(long newUserId, User newUserDetails) throws ResourceNotFoundException{
		// Fetch the corresponding record from the DB
		// Need to pass in newUserId as parameter to findById
		// Need to make use of "Optional" to avoid NPE as the record might / might not exist in DB
		System.out.println("Inside updateUser method of Service class");
		User userDB = userRepository.findById(newUserId)
				.orElseThrow(() -> new ResourceNotFoundException ("Record not found with id :: " + newUserId));
		
		userDB.setTitle(newUserDetails.getTitle());
		userDB.setFirstName(newUserDetails.getFirstName());
		userDB.setLastName(newUserDetails.getLastName());
		userDB.setGender(newUserDetails.getGender());
		//userDB.setAddress(newUserDetails.getAddress());
		logger.info("Updating the record with id " + newUserDetails.getId());
		
		// Save the user in DB
		// Send back the user with updated info
		userRepository.save(newUserDetails);
	}

	/**
	 * Getting all user records by using the method findaAll() of CrudRepository 
	 */
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Getting a specific record by using the method findById() of CrudRepository 
	 */
	@Override
	public User getUserById(long userId) {
		// Fetch the corresponding record from the DB
		// Need to pass in userId as parameter to findById
		// Need to make use of "Optional" to avoid NPE as the record might / might not exist in DB
		Optional<User> userDB = this.userRepository.findById(userId);
		
		// Check if the user is present in the DB
		if(userDB.isPresent()) {
			// Retrieve the object from the DB and return
			logger.info("Record with id " + userId + " fetched successfully");
			return userDB.get();
		} else {
			logger.error("FAILED to fetch record with id " + userId);
			// Throw exception if user isn't present in the DB
			throw new ResourceNotFoundException ("Record not found with id :: " + userId);
		}
	}

	/**
	 * Deleting a specific record by using the method deleteById() of CrudRepository
	 */
	@Override
	public void deleteUser(long userId) {
		// Fetch the corresponding record from the DB
		// Need to pass in userId as parameter to findById
		// Need to make use of "Optional" to avoid NPE as the record might / might not exist in DB
		Optional<User> userDB = this.userRepository.findById(userId);
		
		// Check if the user is present in the DB
		if(userDB.isPresent()) {
			// Delete the object from the DB
			userRepository.deleteById(userId); 
		} else {
			// Throw exception if user isn't present in the DB
			throw new ResourceNotFoundException ("Record not found with id :: " + userId);
		}		
	}

	/**
	 * Saving all records simultaneously by using the method saveAll() of CrudRepository 
	 */
	@Override
	public List<User> saveAll(List<User> users) {
		return userRepository.saveAll(users);
	}

	/**
	 * Deleting all records simultaneously by using the method saveAll() of CrudRepository 
	 */
	@Override
	public void deleteAllUsers() {
		userRepository.deleteAll();
	}
}
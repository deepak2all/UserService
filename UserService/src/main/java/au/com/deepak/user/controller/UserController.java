package au.com.deepak.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import au.com.deepak.user.cache.UserDataConstants;
import au.com.deepak.user.exception.IncorrectInputException;
import au.com.deepak.user.model.Address;
import au.com.deepak.user.model.User;
import au.com.deepak.user.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.springframework.hateoas.Resource;
//import org.springframework.hateoas.mvc.ControllerLinkBuilder;


@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController implements UserApis{

	private static final Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * Get Home Page
	 * Page can be accessed without logging in
	 * @return String
	 */
	@GetMapping("/")
	public String home() {
		logger.debug("Debugging log in our greeting method");
        return ("<h1>Welcome to home page</h1>");
    }

	/**
	 * Get All Users
	 * Creating a get mapping that retrieves all the books detail from the database 
	 * @return List<User>
	 */
	@GetMapping("/users")
    @HystrixCommand(groupKey = "getAllUsers", commandKey = "cmdGetAllUsersTripExecute",
    fallbackMethod = "fallbackGetAllUsers", commandProperties = {
    		@HystrixProperty(name = "circuitBreaker.forceOpen", value = "false")
    })
	public ResponseEntity<List<User>> getAllUsers() throws InterruptedException {
		logger.info("::: FETCHING ALL RECORDS ::: ");
		long randomVal = (ThreadLocalRandom.current().nextLong(1, 5))*1000;
		Thread.sleep(randomVal);
		return ResponseEntity.ok().body(userService.getAllUsers());
	}
	
	/**
	 * Fallback method to Get All Users
	 * Gets the data from cache if DB is not reachable for a long time
	 * If data couldn't be obtained from DB or cache, returns empty JSON
	 * @return List<User>
	 */
	public ResponseEntity<List<User>> fallbackGetAllUsers() {
		logger.info("::: FETCHING DATA from Cache - Fallback Mechanism Triggered ::: ");
		if(UserDataConstants.userInfoCache!=null && UserDataConstants.getUserInfoCache().size()>0) {
			logger.info("Found records in cache !!");
			// If data is present in cache, return it when DB fetch takes longer time
			return ResponseEntity.ok().body(UserDataConstants.getUserInfoCache());
		} else {
			// Return an empty list when do data is present in the cache
			List<User> dummyUsers = new ArrayList<>();
			Address address = new Address("NA", "NA", "NA", "NA");
			User dummyUser = new User(0L, "unkown", "none", "none", "unkown", address);
			dummyUsers.add(dummyUser);
			logger.info("No records found in cache " + dummyUser.toString());
			return ResponseEntity.ok().body(dummyUsers);
		}
	}
	
	/**
	 * Get User by ID
	 * Creating a GET mapping that retrieves the detail of a specific user
	 * @param userId
	 * @return User
	 */
	@GetMapping("/users/{userid}")
	public ResponseEntity<User> getUserById(@PathVariable("userid") 
		@Min(value = 1, message = "id must be greater than or equal to 1") 
		@Max(value = 1000, message = "id must be lower than or equal to 1000") String userid ) {
		try {
		       long id = Long.parseLong(userid);
		       return ResponseEntity.ok().body(userService.getUserById(id));
		    }
		    catch(Exception e) {
		    	throw new IncorrectInputException ("Incorrect input provided :: " + userid);
		    }
	}
	
	/**
	 * Creating post mapping that post the user detail in the database 
	 * @param user
	 * @return user
	 */
	/*@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.createUser(user);
		return ResponseEntity.ok().body(user);
	}*/
	
	/**
	 * Creating put mapping that updates the user detail
	 * @param userDetails
	 * @return userDetails
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/users")
	public ResponseEntity<User> updateUser(@RequestBody User userDetails) {
		logger.info("Record number to be updated :: " + userDetails.getId());
		userService.updateUser(userDetails.getId(), userDetails);
		UserDataConstants.userInfoCache.set(userDetails.getId().intValue(), userDetails);
		return ResponseEntity.ok().body(userDetails);
	}
	
	/**
	 * Creating a delete mapping that deletes a specified user
	 * @param strId
	 * @return
	 */
	/*@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable("id") 
		@Min(value = 1, message = "id must be greater than or equal to 1") 
		@Max(value = 1000, message = "id must be lower than or equal to 1000") String strId) {
		try {
		       long id = Long.parseLong(strId);
		       userService.deleteUser(id);
		    }
		    catch(Exception e) {
		    	throw new IncorrectInputException ("Incorrect input provided :: " + strId);
		    }
	}*/

}
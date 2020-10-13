package au.com.deepak.user;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.deepak.user.service.UserService;
import au.com.deepak.user.cache.UserDataConstants;
import au.com.deepak.user.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableHystrix
public class UserServiceApplication {

	private static final Logger logger = LogManager.getLogger(UserServiceApplication.class);
	
	public static void main(String[] args) {		
		SpringApplication.run(UserServiceApplication.class, args);		
	}

	@PostConstruct
    public void startupApplication() {
        // log startup
		logger.info("Application starting successfully");
    }

    @PreDestroy
    public void shutdownApplication() {
        // log shutdown
    	logger.info("Application exited successfully");
    }
    
	/**
	 * To insert 5 to 10  users in h2 db during the start-up 
	 * from /json/user.json file
	 */
	@Bean
	CommandLineRunner runner(UserService userService) {
		return args -> {
			// read records from user.json file and write to db
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<User>> typeReference = new TypeReference<List<User>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream(UserDataConstants.JSON_FILE_PATH);
			try {
				List<User> users = mapper.readValue(inputStream,typeReference);
				logger.info("Caching the users.json file records");
				UserDataConstants.setUserInfoCache(users);
				if(userService.getAllUsers().size()>0) {
					userService.deleteAllUsers();
				}
				userService.saveAll(users);				
				logger.info("The below users are saved in DB, from users.json file!");
				userService.getAllUsers().forEach((user) -> {
		            logger.info("{}", user);
		        });
			} catch (IOException e){
				logger.error("Unable to save users: " + e.getMessage());
			} catch (Exception e) {
				logger.error("Failed to execute CommandLineRunner " + e.getMessage());
			}
		};
	}
	
	@Bean
	public HystrixCommandAspect hystrixAspect() {
	     return new HystrixCommandAspect();
	}
}
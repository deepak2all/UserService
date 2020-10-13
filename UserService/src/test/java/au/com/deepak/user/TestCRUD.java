package au.com.deepak.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import au.com.deepak.user.model.Address;
import au.com.deepak.user.model.User;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
 
/**
 * This test class has test methods for CRUD actions on REST API 
 * REST Service http://dummy.restapiexample.com/api
 * 
 * It has test methods for Create , Edit , Get and Delete Employee items
 * https://docs.spring.io/spring-framework/docs/current/javadoc-
api/org/springframework/web/client/RestTemplate.html
 * https://docs.spring.io/autorepo/docs/spring/3.2.3.RELEASE/javadoc-
api/org/springframework/web/client/RestTemplate.html
 * @author 
 *
 */
public class TestCRUD {

	private String URL = "http://localhost:8080/api/v1/users/";
	
    private static final Logger logger = LogManager.getLogger(TestCRUD.class);
    //RESTTemplate Object
    private RestTemplate restTemplate;
    
    private List<User> userList;
    
    @BeforeEach
    void setUp() {
        this.userList = new ArrayList<>();
        Address address1 = new Address("1A", "1B", "1C", "1D");
        User dummyUser1 = new User(1L, "Unkown1", "none1", "none1", "unkown1", address1);
        Address address2 = new Address("2A", "2B", "2C", "2D");
        User dummyUser2 = new User(2L, "Unkown2", "none2", "none2", "unkown2", address2);
        Address address3 = new Address("3A", "3B", "3C", "3D");
        User dummyUser3 = new User(3L, "Unkown3", "none3", "none3", "unkown3", address3);
        this.userList.add(dummyUser1);
        this.userList.add(dummyUser2);
        this.userList.add(dummyUser3);
    }

    @Before
    public void beforeTest() throws IOException, ParseException {
        logger.info("Setting up prerequisite for test execution");
        logger.info("Creating RestTemplate object before tests");
        this.restTemplate = new RestTemplate(); 
    }
     
    
    /**
     * Test Method to add employee using HTTP POST request 
     * 
     * Verifies GET action Status Code 
     *  
     * @throws IOException
     * @throws ParseException
     */
    //@Test
    public void testGetEmployee() throws ParseException, Exception {

    	// prepare
    	// Not required as setUp()
    	// be retrieved here

    	// execute
    	ResponseEntity<User> responseEntity = restTemplate.getForEntity(URL + "{id}", 
    															User.class, 
    															new Long(1));

    	// collect response
    	int status = responseEntity.getStatusCodeValue();
    	User resultEmployee = responseEntity.getBody();

    	// verify
    	assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

    	assertNotNull(resultEmployee);
    	assertEquals(1l, resultEmployee.getId().longValue());

    }
     
    //@Test
    public void testUpdateEmployee() throws Exception {
    	// prepare
    	// here the create the employee object with ID equal to ID of
    	// employee need to be updated with updated properties
    	Address address3upd = new Address("3ANew", "3B", "3C", "3D");
        User dummyUser3 = new User(3L, "NewVal", "NewVal", "NewVal", "unkown3", address3upd);
    	HttpEntity<User> requestEntity = new HttpEntity<User>(dummyUser3);

    	// execute
    	ResponseEntity<Void> responseEntity = restTemplate.exchange(URL, 
    														HttpMethod.PUT, 
    														requestEntity, 
    														Void.class);

    	// verify
    	int status = responseEntity.getStatusCodeValue();
    	assertEquals("Correct Response Status", HttpStatus.OK.value(), status);
    	logger.info("Employee Name is Updated successfully " + status);
    }
         
    @After
    public void afterTest() {
        logger.info("Clean up after test execution");
        logger.info("Creating RestTemplate object as Null");
        this.restTemplate = new RestTemplate(); 
    }
}

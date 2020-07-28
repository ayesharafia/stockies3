package com.citi.stockies;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.juli.logging.Log;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.junit.runner.Runner;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.citi.stockies.GetAndPost;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.stockies.model.KeyStats;
import com.stockies.model.UserCred;


//@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class GetAndPostTest {
	
	
	@Autowired
	private GetAndPost service;

	GetAndPost obj= new GetAndPost();
	
	
	
	
	@Test
	void testMyGETRequest() {
		//assertNotNull(obj.MyGETRequest("POWER"));
		assertNotNull(obj.MyGETRequest("SERVICES"));
	    
	}
	

	
	@Test
	void getUserDetailsTest() {
	
		String var="";
		var=service.getUserDetails("qwerty", "12345");
		assertEquals(var,"1");
	 
		assertEquals("-1",service.getUserDetails("hi", "1234"));
	
	}
	
	@Test
	void saveSelectedRecommTest() {
	
		String var=service.saveSelectedRecomm(new KeyStats("test-User","ntpc","","","","","","","",""));
		assertEquals("1", var);
		
	}
	
	
	
	@Test
	void showNiftyTest()
	{
		assertNotNull(obj.showNifty());
	}
	
	
	@Test
	void createUser()
	{
		
		String var=service.createUser(new UserCred("testUser","1234"));
		assertEquals("-1", var);
		
	
		
		
		
	}

	
	
}

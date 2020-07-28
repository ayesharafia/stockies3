package com.citi.stockies;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseInitializer {

	@PostConstruct
	public void initialize() {
		
		
		try {
		FileInputStream serviceAccount =
		  new FileInputStream("/Users/Allu/Desktop/stockies3/ServiceAccount.json");
		
		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .setDatabaseUrl("https://stockies-8a035.firebaseio.com")
		  .build();
		
		FirebaseApp.initializeApp(options);
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		
		
	}
}

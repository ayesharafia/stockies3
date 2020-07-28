

package com.citi.stockies;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockies.model.KeyStats;
import com.stockies.model.UserCred;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class StockiesController {
	public String username1;
	public String selectedDrop;
	@Autowired
	private GetAndPost service;

		
	@RequestMapping(method = RequestMethod.GET,value = "/getRecommendation")
	public JSONArray MyGETRequest(@RequestParam String sectorname) throws Exception{
		//selectedDrop=parameter.getSectorname();
		System.out.println(sectorname);
		JSONArray var=null;
		var=service.MyGETRequest(sectorname);
		return var;
		
		
				
}

	
	@RequestMapping(method = RequestMethod.GET, value = "/showNifty")
	public String showNifty() throws Exception{
		String res=null;

		res=service.showNifty();
		
		return res;

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createUser")

	public String createUser(@RequestParam String username,@RequestParam String password) throws InterruptedException, ExecutionException {
        UserCred user = new UserCred();
        user.setPsswd(password);
        user.setUsername(username);
		return service.createUser(user);

	}
	
	@RequestMapping(method = RequestMethod.GET,value = "/loginfire")
	public String getExample(@RequestParam String username,@RequestParam String password) throws InterruptedException, ExecutionException {

		return service.getUserDetails(username,password);
	}
	
	
	@GetMapping("/getSavedRecomm")
	public List<KeyStats> getSaveRecomm(@RequestParam String username,@RequestParam String timespanSelected) throws InterruptedException, ExecutionException {
		System.out.println(username);
		return service.getSavedRecomm(username,timespanSelected);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveRecomm")
	public String saveRecomm(@RequestBody List<KeyStats> dataToStore) throws InterruptedException, ExecutionException {
	
		return service.saveRecomm(dataToStore);
		//return dataToStore;
	}
	
	
	//Body in other method (private)
	@RequestMapping(method = RequestMethod.GET, value = "/saveSelectedRecomm")
	public String saveSelectedRecomm(@RequestParam String username,@RequestParam String shortname,@RequestParam String mktcap,@RequestParam String lastvalue,@RequestParam String chg,@RequestParam String percentchange,@RequestParam String yearlyhigh,@RequestParam String yearlylow,@RequestParam String volume,@RequestParam String fdayavgvol,@RequestParam String todaysopen,@RequestParam String timestamp1,@RequestParam String timestamp_to_display) throws InterruptedException, ExecutionException {


		
		KeyStats dataToStore=new KeyStats();
		dataToStore.setUsername(username);
		dataToStore.setShortname(shortname);
		dataToStore.setMktcap(mktcap);
		dataToStore.setLastvalue(lastvalue);
		dataToStore.setChg(chg);
		dataToStore.setPercentchange(percentchange);
		dataToStore.setYearlyhigh(yearlyhigh);
		dataToStore.setYearlylow(yearlylow);
		dataToStore.setVolume(volume);
		dataToStore.setFdayavgvol(fdayavgvol);		
		dataToStore.setTodaysopen(todaysopen);
		
		return service.saveSelectedRecomm(dataToStore);
		//return dataToStore;
	}
	
	
	@GetMapping("/deleteSavedRecomm")

	public Integer deleteSavedRecomm(@RequestParam() String username,@RequestParam() String timestamp1,@RequestParam() String shortname) throws InterruptedException, ExecutionException {
		
		return service.deleteSavedRecomm(username,timestamp1,shortname);

	}
	
	
	@GetMapping("/deleteAllSavedRecomm")
	public String deleteSavedRecomm(@RequestParam() String username) throws InterruptedException, ExecutionException {
		return service.deleteAllSavedRecomm(username);
	}

}



	/*
	@RequestMapping(method = RequestMethod.GET, value = "/showGraph")
	public JSONObject showGraph(@RequestParam String stockName) throws IOException, ParseException {
		return service.showGraph(stockName);
	}*/
	
	


	
	
	
	
	
	



package com.citi.stockies;

import java.util.regex.*;  

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Logging;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.stockies.model.Constants;
import com.stockies.model.KeyStats;
import com.stockies.model.Nifty50;
import com.stockies.model.UserCred;



import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




@Service
public class GetAndPost {
	
	private static final Logger LOG = LogManager.getLogger(GetAndPost.class.getName());
	
	
//Get recommendation sector wise	
public JSONArray MyGETRequest(String sector) {
	    
	    //Creating hashmap where key is sector and value is list of companies in that sector
		HashMap<String, ArrayList<String>> sectors = new HashMap<String, ArrayList<String>>();

		ArrayList<String> s1 = new ArrayList<String>(Arrays.asList(Constants.ADANIPORTS));///
		sectors.put("SERVICES", s1);

		ArrayList<String> s2 = new ArrayList<String>(Arrays.asList(Constants.GRASIM, Constants.SHREECEM, Constants.ULTRACEMCO));
		sectors.put("CEMENT_CEMENTPRODUCTS", s2);

		ArrayList<String> s3 = new ArrayList<String>(Arrays.asList(Constants.BHARTIARTL, Constants.INFRATEL));///
		sectors.put("TELECOM", s3);

		ArrayList<String> s4 = new ArrayList<String>(Arrays.asList(Constants.LTNS));//
		sectors.put("CONSTRUCTION", s4);

		ArrayList<String> s5 = new ArrayList<String>(Arrays.asList(Constants.BPCL, Constants.GAIL, Constants.ONGC, Constants.RELIANCE));///
		sectors.put("OIL_GAS", s5);

		ArrayList<String> s6 = new ArrayList<String>(Arrays.asList(Constants.NTPC, Constants.POWERGRID));///
		sectors.put("POWER", s6);

		ArrayList<String> s7 = new ArrayList<String>(Arrays.asList(Constants.ASIANPAINT,Constants.BRITANNIA, Constants.HINDUNILVR, Constants.ITCNS, Constants.NESTLEIND, Constants.TITAN));
		sectors.put("CONSUMER_GOODS", s7);

		ArrayList<String> s8 = new ArrayList<String>(Arrays.asList(Constants.UPL));//
		sectors.put("FERTILISERS_PESTICIDES", s8);

		ArrayList<String> s9 = new ArrayList<String>(Arrays.asList(Constants.AXISBANK, Constants.BAJFINANCE, Constants.BAJAJFINSV, Constants.HDFCBANK,Constants.HDFC, Constants.ICICIBANK, Constants.INDUSINDBK, Constants.KOTAKBANK, Constants.SBIN));
		sectors.put("FINANCIAL_SERVICES", s9);

		ArrayList<String> s10 = new ArrayList<String>(Arrays.asList(Constants.ZEEL));//
		sectors.put("MEDIA_ENTERTAINMENT", s10);

		ArrayList<String> s11 = new ArrayList<String>(Arrays.asList(Constants.HCLTECH, Constants.INFY,Constants.TCS, Constants.TECHM, Constants.WIPRO));
		sectors.put("IT", s11);

		ArrayList<String> s12 = new ArrayList<String>(Arrays.asList(Constants.CIPLA, Constants.DRREDDY, Constants.SUNPHARMA));
		sectors.put("PHARMA", s12);

		ArrayList<String> s13 = new ArrayList<String>(Arrays.asList(Constants.BAJAJAUTO, Constants.EICHERMOT, Constants.HEROMOTOCO, Constants.M,Constants.MARUTI, Constants.TATAMOTORS));
		sectors.put("AUTOMOBILE", s13);

		ArrayList<String> s14 = new ArrayList<String>(Arrays.asList(Constants.COALINDIA, Constants.HINDALCO, Constants.JSWSTEEL, Constants.TATASTEEL, Constants.VEDL));
		sectors.put("METALS", s14);
		
		
		
		
		//Getting epoch time for present
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli()/1000L-24*60*60;
		String timeStampMillisStr=Long.toString(timeStampMillis);
		
		//Getting epoch time for 2 weeks before
		long timeStampTwoWeeksBefore=timeStampMillis-14*24*60*60;	
		String timeStampTwoWeeksBeforeStr=Long.toString(timeStampTwoWeeksBefore);

		//Creating hashmap having key as stock and value as score to determine the recommendation
	    HashMap<String,Float> companyRanking= new HashMap<String,Float>();		
	    
	    //Creating List of stocks for requested sector
		ArrayList<String> sectorCompanies=new ArrayList<>();
		sectorCompanies=sectors.get(sector);	
		
		//Calculating score for each company in selected sector
		for (String string : sectorCompanies) {
			
			Float companyScore=0.0f;
			companyScore=getScoreUsingRapid(string,timeStampMillisStr,timeStampTwoWeeksBeforeStr);		
			companyRanking.put(string,companyScore);
			
		}
		
		//Sorting the map by its value(score)
		Map<String, Float> result = companyRanking.entrySet()
				  .stream()
				  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				  .collect(Collectors.toMap(
				    Map.Entry::getKey, 
				    Map.Entry::getValue, 
				    (oldValue, newValue) -> oldValue, LinkedHashMap::new));	
		System.out.print(result);
		
		JSONArray rankedCompanies=null;
		rankedCompanies=showRecom(result);
			
		return rankedCompanies;
	
}







//To get the recommendation score of the stock
private Float getScoreUsingRapid(String company,String timeStampMillisStr,String timeStampTwoWeeksBeforeStr){

	float sum=0;
	float i=10;

	//Calling yahoo finance rapid api to get last 2 week's data of the stock
	String url=Constants.rapid_url+timeStampTwoWeeksBeforeStr+"&period2="+timeStampMillisStr+"&symbol="+company;
	HttpResponse<String> response = null;
	try {
		response = Unirest.get(url)

				.header("x-rapidapi-host", Constants.rapid_host)

				.header("x-rapidapi-key",Constants.api_key )

				.asString();
		//0f42b6a562msh5356194d5be346dp197d5ejsnbcb55efd44ce
	} catch (UnirestException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	String stringResponse=response.getBody();

	//Parsing string response to json format
	JSONParser parse = new JSONParser();
	JSONObject jobj = null;
	try {
		jobj = (JSONObject) parse.parse(stringResponse);
	} catch (ParseException e) {
		LOG.error("Exception - " , e);
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Getting array corresponding to 'prices'
	JSONArray jsonarr_1 = (JSONArray) jobj.get(Constants.prices);

	//Iterating through jsoonarr_1 to calculate score 
	//Each instance iterated, represents data for one day
	for (Object object : jsonarr_1) {

		float open2=0;
		float close2=0;

		//Get the open value of stock
		JSONObject jsonObj_open = (JSONObject) object;
		Object open=jsonObj_open.get(Constants.open);

		//type casting open value on the basis of instance type to avoid errors 
		if(open instanceof Long) {
			Long open1 = (Long) jsonObj_open.get(Constants.open);
			open2=open1;

		}

		else if(open instanceof Double){
			double	open1 = (double) jsonObj_open.get(Constants.open);
			open2=(float)open1;

		}
				
		//Get the close value of stock
		JSONObject jsonObj_close = (JSONObject) object;
		Object close=jsonObj_close.get(Constants.close);

		//type casting close value on the basis of instance type to avoid errors 
		if(close instanceof Long) {
			Long close1 = (Long) jsonObj_close.get(Constants.close);
			close2=close1;
		}
		else if(close instanceof Double){
			double	close1 = (double) jsonObj_close.get(Constants.close);
			close2=(float)close1;

		}

		//Calculating score for stock
		if(open2!=0) {
		sum=i*(close2/open2)+sum;
		}

		i=i-1;

		if(i==0) {
			break;
		}
	}
	

return sum;


}









//Show details of recommended stocks on the order of passed map
public JSONArray showRecom(Map result) {
	
		String urlString = null;

		HashMap<String, String> ids = new HashMap<String, String>();
		
		//"ASIANPAINT.NS", "BRITANNIA.NS", "HINDUNILVR.NS", "ITC.NS", "NESTLEIND.NS", "TITAN.NS"
		//AXISBANK.NS", "BAJFINANCE.NS", "BAJAJFINSV.NS", "HDFCBANK.NS",
		//"HDFC.NS", "ICICIBANK.NS", "INDUSINDBK.NS", "KOTAKBANK.NS", "SBIN.NS
		//BAJAJ-AUTO.NS", "EICHERMOT.NS", "HEROMOTOCO.NS", "M&M.NS", "MARUTI.NS", "TATAMOTORS.NS

		
		//To display details, moneycontrol api is used.Changing the symbols for yahoo finance api to money control api
		//Used hashmap for reusability
		ids.put(Constants.ADANIPORTS, Constants.MPS);
		ids.put(Constants.ASIANPAINT, Constants.API);
		ids.put(Constants.AXISBANK, Constants.UTI10);
		ids.put(Constants.BAJAJAUTO, Constants.BA06);
		ids.put(Constants.BAJAJFINSV, Constants.BF04);
		ids.put(Constants.BAJFINANCE, Constants.BAF);
		ids.put(Constants.BHARTIARTL, Constants.BTV);
		ids.put(Constants.INFRATEL, Constants.BI26);
		ids.put(Constants.BPCL, Constants.BPC);
		ids.put(Constants.BRITANNIA, Constants.BI);
		ids.put(Constants.CIPLA,Constants.C);
		ids.put(Constants.COALINDIA, Constants.CI29);
		ids.put(Constants.DRREDDY, Constants.DRL);
		ids.put(Constants.EICHERMOT, Constants.EM);
		ids.put(Constants.GAIL, Constants.GAI);
		ids.put(Constants.GRASIM, Constants.GI01);
		ids.put(Constants.HCLTECH, Constants.HCL02);
		ids.put(Constants.HDFC, Constants.HDF);
		ids.put(Constants.HDFCBANK, Constants.HDF01);
		ids.put(Constants.HEROMOTOCO, Constants.HHM);
		ids.put(Constants.HINDALCO, Constants.H);
		ids.put(Constants.HINDUNILVR, Constants.HL);
		ids.put(Constants.ICICIBANK, Constants.ICI02);
		ids.put(Constants.INDUSINDBK, Constants.IIB);
		ids.put(Constants.INFY, Constants.IT);
		ids.put(Constants.IOCNS, Constants.IOC);
		ids.put(Constants.ITCNS, Constants.ITC);
		ids.put(Constants.JSWSTEEL, Constants.JVS);
		ids.put(Constants.KOTAKBANK, Constants.KMF);
		ids.put(Constants.LT, Constants.LT);
		ids.put(Constants.M, Constants.MM);
		ids.put(Constants.MARUTI, Constants.MU01);
		ids.put(Constants.NESTLEIND, Constants.NI);
		ids.put(Constants.NTPC, Constants.NTP);
		ids.put(Constants.ONGC, Constants.ONG);
		ids.put(Constants.POWERGRID, Constants.PGC);
		ids.put(Constants.RELIANCE, Constants.RI);
		ids.put(Constants.SBIN, Constants.SBI);
		ids.put(Constants.SHREECEM, Constants.SC12);
		ids.put(Constants.SUNPHARMA, Constants.SPI);
		ids.put(Constants.TATAMOTORS, Constants.TEL);
		ids.put(Constants.TATASTEEL, Constants.TIS);
		ids.put(Constants.TCSNS, Constants.TCS);
		ids.put(Constants.TECHM, Constants.TM4);
		ids.put(Constants.TITAN, Constants.TI01);
		ids.put(Constants.ULTRACEMCO, Constants.UTC);
		ids.put(Constants.UPL, Constants.SI10);
		ids.put(Constants.VEDL, Constants.SG);
		ids.put(Constants.WIPRO, Constants.W);
		ids.put(Constants.ZEEL, Constants.ZT);

		JSONArray finalList = new JSONArray();
		
		//Getting stock names from result.keySet() and fetching details of the stock 
		for (Object string : result.keySet()) {

			//Fetching details using moneycontrol api
			urlString = "https://www.moneycontrol.com/news18/stocks/overview/" + ids.get(string) + "/N?classic=true";
			URL urlForGetRequest = null;
			try {
				urlForGetRequest = new URL(urlString);
			} catch (MalformedURLException e) {
				LOG.error("Exception - " , e);
			}
			String readLine = null;
			String resp = null;
			HttpURLConnection conection = null;
			
			try {
				conection = (HttpURLConnection) urlForGetRequest.openConnection();
			} catch (IOException e) {
				LOG.error("Exception - " , e);
			}
			
			try {
				conection.setRequestMethod(Constants.GET);
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				LOG.error("Exception - " , e);
			}
			int responseCode = 0;
			
			try {
				responseCode = conection.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = null;
				
				try {
					in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				} catch (IOException e) {
					LOG.error("Exception - " , e);
				}
				StringBuffer response = new StringBuffer();
				
				try {
					while ((readLine = in.readLine()) != null) {

						response.append("\n" + readLine);
					}
				} catch (IOException e1) {
					LOG.error("Exception - " , e1);
				}
				
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("Exception - " , e);
				}

				resp = response.toString();
				
				//Converting string response to json
				JSONParser parse = new JSONParser();
				JSONObject jobj = null;
				
				try {
					jobj = (JSONObject) parse.parse(resp);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject jobj1 = (JSONObject) jobj.get(Constants.NSE);				
				//Adding details to JSONArray
	
				finalList.add(new KeyStats((String) jobj1.get(Constants.shortname), (String) jobj1.get(Constants.mktcap),
						(String) jobj1.get(Constants.lastvalue), (String) jobj1.get(Constants.CHG), (String) jobj1.get(Constants.percentchange),
						(String) jobj1.get(Constants.yearlyhigh), (String) jobj1.get(Constants.yearlylow), (String) jobj1.get(Constants.volume),
						(String) jobj1.get(Constants.dayavgvol), (String) jobj1.get(Constants.todaysopen)));


			} else {
				System.out.print("Error in connection");
			}

		}
		
		//Sample data returned by money control
		//"id":"PGC","shortname":"Power Grid Corp","lastvalue":"158.00","CHG":"-4.20","percentchange":"-2.59","lastupdate":"23 Apr, 2020 16:03","dayhigh":"161.90","daylow":"157.60","yearlyhigh":"216.25","yearlylow":"122.15","todaysopen":"161.50","volume":"18072426","yesterdaysclose":"162.20","mktcap":"82,659.12","pe":"10.77","bidprice":"0.00","bidqty":"0.00","offerprice":"158.00","offerqty":"122.00","topicid":"246800","eps":"20.42","bookvalue":"112.81","pricebook":"1.40","deliverables":"64.14","dividend":"83.30","div_yield":"5.27","market_lot":"1.00","face_value":"10.00","p_e":"7.74","pc":"3.96","ex":"N","direction":"-1","lcprice":"146.00","ucprice":"178.40","5dayavgvol":"12091786.40","mkt_depth_buy":"0","mkt_depth_sell":"0","vwap":"159.40","trade_msg":"","share_url":"http:\/\/m.moneycontrol.com\/india\/stockpricequote\/powergenerationdistribution\/powergridcorporationindia\/PGC","dispid":"PGC","bseid":"532898","nseid":"POWERGRID","isinid":"INE752E01010","sector":"Power - Generation & Distribution"	  

		return finalList;

	}
	  
	   



	public String getPrediction(String sector) throws IOException {

		return "inside prediction";

	}




//Show Nifty50  details
//String operation used to get desired response
public String showNifty(){
		List<Nifty50> niftyList = new ArrayList<>();
		String jsonString = null;

		URL urlForGetRequest = null;
		
		try {
			urlForGetRequest = new URL(
					"https://appfeeds.moneycontrol.com/jsonapi/market/marketmap&format=&type=0&ind_id=9");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String readLine = null;
		HttpURLConnection conection = null;
		
		try {
			conection = (HttpURLConnection) urlForGetRequest.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Exception - " , e);
		}
		
		try {
			conection.setRequestMethod(Constants.GET);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			LOG.error("Exception - " , e);
		}
		conection.setRequestProperty(Constants.userId,Constants.a1bcdef); // set userId its a sample here
		int responseCode = 0;
		
		try {
			responseCode = conection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			} catch (IOException e) {
				LOG.error("Exception - " , e);
			}
			StringBuffer response = new StringBuffer();
			
			try {
				while ((readLine = in.readLine()) != null) {
					
					response.append("\n" + readLine);
				}
			} catch (IOException e) {
				LOG.error("Exception - " , e);
			}
			try {
				in.close();
			} catch (IOException e) {
				LOG.error("Exception - " , e);
			}

			jsonString = response.substring(9, response.length()-1).toString();

		} else {
			System.out.println("Error in api request to moneycontrol");
		}
		
		return jsonString;
}



//Check whether user login credentials are matched or not
	public String getUserDetails(String username, String psswd) {
		
		try {
		
		//Creating reference to desired collection on firebase having requested username as id
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection(Constants.LoginCredentials).document(username);
		ApiFuture<DocumentSnapshot> future = documentReference.get();
		DocumentSnapshot document = future.get();
		UserCred credential = null;

		//if document is found for that username, check for password.If matches, return 1,else return -1
		if (document.exists()) {
			credential = document.toObject(UserCred.class);
			if (psswd.equals(credential.getPsswd())) {
				return "1";
			} else {
				//System.out.println("in if");
				return "-1";
			}

		} else {
			//System.out.println("outer if");
			return "-1";
		}
		}catch(Exception e)
		{
			LOG.error("Exception - " , e);
			return "-10";
		}
		
	}



//get saved recommendation of the user
public List<KeyStats> getSavedRecomm(String username,String timespanSelected) {

	//Instantiating object of KeyStats to be used to append in a list
	List<KeyStats> ls = new ArrayList<KeyStats>();
		
	try {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> filter=null;	
		Integer noOfDays=0;
		
		//Based on filters (All,week,month,year),noOfDays are changed to display the saved recommendations 
		if(timespanSelected.equals(Constants.All)) {
			filter=dbFirestore.collection(Constants.SavedRecommendation).whereEqualTo(Constants.username, username).get();			
		}
		else {
			
		if(timespanSelected.equals(Constants.week)) {
			noOfDays=7;
			
		}
		else if(timespanSelected.equals(Constants.month)){
			noOfDays=30;
		}
		else if(timespanSelected.equals(Constants.year)){
			noOfDays=365;
		}
		
		//Getting date for last week|month|year
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		String date_now = new SimpleDateFormat(Constants.ymd).format(new Date(System.currentTimeMillis() - (noOfDays * DAY_IN_MS)));

		//Applying timefilter to timestamp column on collection
		filter = dbFirestore.collection(Constants.SavedRecommendation).whereEqualTo(Constants.username, username).whereGreaterThanOrEqualTo("timestamp1", date_now)
				.get();

		}

		//Adding details to display in list
		List<QueryDocumentSnapshot> documents = null;
		
		try {
			documents = filter.get().getDocuments();
		} catch (InterruptedException e) {
			LOG.error("Exception - " , e);
		} catch (ExecutionException e) {
			LOG.error("Exception - " , e);
		}
		for (DocumentSnapshot document : documents) {

			ls.add(document.toObject(KeyStats.class));

		}
		}
		catch(NullPointerException e){
			
			LOG.error("Exception - " , e);

		}

		return ls;
		

}



//	Save Recommendation 
	public String saveRecomm(List<KeyStats> dataToStore){
		
        //timestamp when saving data
        String timeStamp1 = new SimpleDateFormat(Constants.ymdhms).format(new Date());
        
        //Format of date as to be displayed
        String DateToDisplay=new SimpleDateFormat(Constants.emdy).format(new Date());
        DateToDisplay=timeStamp1.substring(10)+" "+DateToDisplay;
        String key_id=null;
        
        Firestore dbFirestore = com.google.firebase.cloud.FirestoreClient.getFirestore();
    
        //Save recommendation for all the companies for requested sector
        for (KeyStats row : dataToStore) {
        	row.setTimestamp1(timeStamp1);
        	row.setTimestamp_to_display(DateToDisplay);
        	key_id=row.getUsername()+timeStamp1+row.getShortname();
        	ApiFuture<WriteResult> future = dbFirestore.collection(Constants.SavedRecommendation).document(key_id).set(row,
    				SetOptions.merge());
        	
        	
			
		}
        //return future.get().getUpdateTime().toString();
		return "1";


}
	
	//Save recommendation for selected companies of the sector 
	public String saveSelectedRecomm(KeyStats dataToStore) {

        String timeStamp1 = new SimpleDateFormat(Constants.ymdhms).format(new Date());
        String DateToDisplay=new SimpleDateFormat(Constants.emdy).format(new Date());
        DateToDisplay=timeStamp1.substring(11)+" "+DateToDisplay;
        String key_id=null;        
        Firestore dbFirestore = com.google.firebase.cloud.FirestoreClient.getFirestore();
        
        dataToStore.setTimestamp1(timeStamp1);
    	dataToStore.setTimestamp_to_display(DateToDisplay);
    	key_id=dataToStore.getUsername()+timeStamp1+dataToStore.getShortname();
    	ApiFuture<WriteResult> future = dbFirestore.collection(Constants.SavedRecommendation).document(key_id).set(dataToStore,
				SetOptions.merge());
		
		// TODO Auto-generated method stub
		return "1";
	}
	
	
	
	
	//For Sign up
	public String createUser(UserCred User)  {
		
		Firestore dbFirestore = com.google.firebase.cloud.FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection(Constants.LoginCredentials).document(User.getUsername());
		ApiFuture<DocumentSnapshot> future = documentReference.get();
		DocumentSnapshot document = null;
		try {
			document = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if username exists,return -1(failure), else return create a user and return 1 1
		if (document.exists()) {
			return "-1";
		}
		else {
			ApiFuture<com.google.cloud.firestore.WriteResult> collectionsApiFuture = dbFirestore.collection("LoginCredentials").document(User.getUsername()).set(User);
			return "1";

		}
		 }
	
	
	//Delete saved recommendation
	public Integer deleteSavedRecomm(String username,String timestamp1,String shortname){

		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> future=null;
		
		//Complex query used for deleting
		future = dbFirestore.collection("Saved_Recommendation").whereEqualTo("username", username).whereEqualTo("timestamp1",timestamp1).whereEqualTo("shortname",shortname).get();

		List<QueryDocumentSnapshot> documents = null;
		try {
			documents = future.get().getDocuments();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//deleting all the matched records
		for (DocumentSnapshot document : documents) {

			document.getReference().delete();

		

		}

		return 1;

	}
	
	
	
	//Delete all saved recommendation
	public String deleteAllSavedRecomm(String username) throws InterruptedException, ExecutionException {
		
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> future=null;
		future = dbFirestore.collection(Constants.SavedRecommendation).whereEqualTo(Constants.username, username).get();
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();

		for (DocumentSnapshot document : documents) {

			document.getReference().delete();
		
		}
			
		return "1";
	}

	
	
//Getting recommendation score using Alpha Vantage Api.Alpha Vantage api not currently working.If started working,uncomment this to resolve issue of limited calls on yahoo finance api.
/*
public float getScore(String company) {
		float sum = 0;
		
		//GYXAEW52CO89NOCW
		String url = null;
		url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=nse:" + company
				+ "&apikey=3WB4CEFLODZZJQX2";
		URL urlForGetRequest = null;
		try {
			urlForGetRequest = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String readLine = null;
		HttpURLConnection conection = null;
		try {
			conection = (HttpURLConnection) urlForGetRequest.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
		
		int responseCode = 0;
		try {
			responseCode = conection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuffer response = new StringBuffer();
			int count = 0;
			try {
				while ((readLine = in.readLine()) != null) {
					response.append("\n" + readLine);
					count++;
					if (count >= 78)

						break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.append("} } }");

			//System.out.println(response);
			
			Map<String, Object> retMap = new Gson().fromJson(
					response.toString(), new TypeToken<HashMap<String, Object>>() {
					}.getType());
			Map<String, Map<String, String>> retMap2 = (Map<String, Map<String, String>>) retMap
					.get("Time Series (Daily)");

			float i = 10;
			for (String key : retMap2.keySet())

			{
				Map<String, String> retMap3 = (Map<String, String>) retMap2.get(key);
				Float open1 = Float.parseFloat(retMap3.get("1. open"));
				Float close1 = Float.parseFloat(retMap3.get("4. close"));
				sum = i * (close1 / open1) + sum;
				i = i - 1;

			}
		} else {
			System.out.println("GET NOT WORKED");
		}
		return sum;

}*/
}
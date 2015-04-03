package com.android.softart.janet;


import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.mashape.unirest.http.JsonNode;

public class UlitimateWeatherHandler {
	
   private	JsonNode js; 

    public	UlitimateWeatherHandler(JsonNode js){
	   
	   this.js = js;
   }
  
    public	UlitimateWeatherHandler(String jsStr){
 	   
 	   this.js= new JsonNode(jsStr);
    }

public WeatherCurrentCondition  getCurrentWeather(){
	
	 
   WeatherCurrentCondition wcc = new WeatherCurrentCondition();
   wcc.setTempCelciusHigh(-274);
   wcc.setTempCelciusLow(-274);
   
	if (js!=null){
	
	   Calendar c = Calendar.getInstance();
	   Date date = new Date();
	   c.setTime(date);
	
	   int day = c.get( Calendar.DAY_OF_WEEK);
	   //System.out.print("Day of week "+ day); 
	   
	   String [] daysOfWeek = {"Sat","Sun","Mon","Tue","Wed","Thu","Fri"};
	   
	   String dayOfWeek = daysOfWeek[day];
	   //System.out.println("Day of week "+ dayOfWeek);
	   
	   
	  JSONArray jsArry =  js.getArray();
	  for (int i=0;i< jsArry.length();i++){
		  
		  try {
			JSONObject jo = jsArry.getJSONObject(i);
			if(dayOfWeek.contains( (String) jo.get("day_of_week"))){
				
				wcc.setDayofWeek(dayOfWeek);
				wcc.setTempCelciusLow( jo.getInt("low_celsius"));
				wcc.setTempCelciusHigh(jo.getInt("high_celsius"));
				wcc.setCondition(jo.getString("condition"));
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	  }
	  
	  //System.out.println(wcc.getCondition() ); 
   }
   
   

   return wcc;
}
}

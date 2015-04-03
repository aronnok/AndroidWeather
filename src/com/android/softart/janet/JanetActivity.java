package com.android.softart.janet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


 
public class JanetActivity extends Activity implements OnInitListener {
    /** Called when the activity is first created. */
	 private static final int REQUEST_CODE = 1234;
	 private ListView wordsList;
	 
	 private static final String JANET_RESPONSE = "You Said";  
	 private static final int SLEEP = 2000; // 2 seconds
	 private static final String NOT_FOUND = "Your requested city's temperature is unavailable";
	 private static final String REMOVE_PATTERN = "^(?i)(weather)|(temperature)\\s((of)|(at)|(for)|(in))\\s";
	 
	 private TextToSpeech mTts;
     private ArrayList<String> myStringArray1 =  new ArrayList<String>();
     private   ArrayAdapter<String> aa ;
	 
	    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        Button speakButton = (Button) findViewById(R.id.speakButton);
        Button getWeatherButton = (Button) findViewById(R.id.button1);
        final TextView ts = (TextView) findViewById(R.id.editText1);
        
        
        wordsList = (ListView) findViewById(R.id.list);
 
        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
        // TextToSpeech.OnInitListener
        mTts = new TextToSpeech(this, this  );
       
        
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,myStringArray1  );
        
        wordsList.setAdapter(aa);
        
        getWeatherButton.setOnClickListener(new OnClickListener(){
        	
        	  public void onClick(View v) {
                   
        		  
        		  //CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.row, myStringArray1);
        		   
        		 addItem(v, ts.getText().toString() );
        		  	
        		  //Log.e("Janet ","$$$$$$$$$$$$$$"+ts.getText().toString());
                  //Log.e("Response" ,getWeatherByCity( ts.getText().toString() ));
                }
        });
        
        
        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                onListItemClick(v,pos,id);
            }
        });
        
    }
	    
    
    private void addItem(View v, String ts){
    	 aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,myStringArray1  );
         
         wordsList.setAdapter(aa);
         
    	
		 this.myStringArray1.add(ts);
		 aa.notifyDataSetChanged();
	
    	
    }
    
    private void onListItemClick(View v, int pos, long id) {
		// TODO Auto-generated method stub
    	
    	String toastMsg =(String) wordsList.getItemAtPosition(pos);
    	
    	if(toastMsg.contains( "weather") || toastMsg.contains( "temperature")){
        	
            toastMsg = getWeatherByCity(toastMsg );
            
            Toast.makeText(JanetActivity.this, 
					toastMsg, Toast.LENGTH_SHORT).show();
	    	
            mTts.speak( toastMsg ,
                    TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                    null);
        }
    	else{
	    	Toast.makeText(JanetActivity.this, 
					toastMsg, Toast.LENGTH_SHORT).show();
	    	
	    	mTts.speak( toastMsg ,
                    TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                    null);
    	}
	}
 
    /**
     * Handle the action of the button being clicked
     */
    public void speakButtonClicked(View v)
    {
        startVoiceRecognitionActivity();
    }
 
    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Janet prototype...");
        startActivityForResult(intent, REQUEST_CODE);
    }
 
    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
            
            
            String speakMatched = (String) matches.get(0);
            
            	
            String response = JANET_RESPONSE + speakMatched; 
            mTts.speak(  response,
                    TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                    null);
            try{
            	Thread.sleep(SLEEP);
            }
            catch(Exception e){
            	
            	Log.e("Thread error!",e.getMessage() );
            	
            }
            if(speakMatched.contains( "weather") || speakMatched.contains( "temperature")){
            	
            	response = getWeatherByCity(speakMatched );
            	
            	Toast.makeText(JanetActivity.this, 
            			response, Toast.LENGTH_LONG).show();
                mTts.speak( response ,
	                    TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
	                    null);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private String getWeatherByCity(String weatherSt ){
    	
   
    	String response = NOT_FOUND;
    	
		try {
			/* Get what user typed to the EditText. */
			
			String cityParamString = NOT_FOUND;
			cityParamString = (weatherSt.replaceFirst(REMOVE_PATTERN,""));
			
			HttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpGet httpget = 
				new HttpGet("https://george-vustrey-weather.p.mashape.com/api.php?location="
								+cityParamString.replace(" ", "%20"));
			
			// Depends on your web service
			httpget.setHeader("Content-type", "application/json");
			httpget.setHeader("X-Mashape-Authorization", "Vfgw7KMkbAQjIBIrWmA2dlCHLHB8viL9");
			
			InputStream inputStream = null;
			String jsonResult = null;
			try {
			    HttpResponse httpResponse = httpclient.execute(httpget);           
			    HttpEntity entity = httpResponse.getEntity();

			    inputStream = entity.getContent();
			    // json is UTF-8 by default
			    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			    StringBuilder sb = new StringBuilder();

			    String line = null;
			    while ((line = reader.readLine()) != null)
			    {
			        sb.append(line + "\n");
			    }
			    jsonResult = sb.toString();
			} catch (Exception e) { 
			    // Oops
			}
			finally {
			    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
			}
			
			Log.e("Json", jsonResult);
			
		    UlitimateWeatherHandler uth = new UlitimateWeatherHandler(jsonResult);
		    WeatherCurrentCondition  wcc = uth.getCurrentWeather();
		    
		    response = "Current weather of  "+cityParamString+" is"+
			 " Low "+ wcc.getTempCelciusLow() + " degree celsius "+ 
			 " High "+wcc.getTempCelciusHigh()+ " degree celsius weather "+
			 " with "+ wcc.getCondition();
			
		} catch (Exception e) {
			
			
			Log.e( "WeatherQueryError", e.toString());
		}
		
		return response; 
    }
    
    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        
        if (status == TextToSpeech.SUCCESS) {
          
        	Toast.makeText(JanetActivity.this, 
					"Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
        	
            
        } else {
        	Toast.makeText(JanetActivity.this, 
					"Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }
    }

    
    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }

        super.onDestroy();
    }

}
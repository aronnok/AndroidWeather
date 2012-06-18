package com.android.softart.janet;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


 
public class JanetActivity extends Activity implements OnInitListener {
    /** Called when the activity is first created. */
	 private static final int REQUEST_CODE = 1234;
	 private ListView wordsList;
	 
	 private static final String JANET_RESPONSE = "You Said";  
	 private static final int SLEEP = 2000; // 2 seconds
	 private static final String NOT_FOUND = "Your requested city's temperature is unavailable";
	 private static final String REMOVE_PATTERN = "^(?i)(weather)|(temperature)\\s((of)|(at)|(for)|(in))\\s";
	 
	 private TextToSpeech mTts;

	 
	    /**
	     * Called with the activity is first created.
	     */
	    @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	 
	        Button speakButton = (Button) findViewById(R.id.speakButton);
	 
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
	        
	        mTts = new TextToSpeech(this,
	                this  // TextToSpeech.OnInitListener
	                );

	        
	        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
	                onListItemClick(v,pos,id);
	            }

				
	        });
	        
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
                
	            //StringBuffer st = new StringBuffer("You Said       ");
	            String speakMatched = (String) matches.get(0);
	            //for (String word: matches)
	            	//st.append(word +"    or    " );
	            	
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
	                mTts.speak( response ,
		                    TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		                    null);
	            }
	            
	            
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	    
	    
	    private String getWeatherByCity(String weatherSt ){
	    	

	    	URL url;
	    	String response = NOT_FOUND;
	    	
	    	//StringTokenizer st = new StringTokenizer(weatherSt);
	    	
			try {
				/* Get what user typed to the EditText. */
				
				String cityParamString = NOT_FOUND;
				
				cityParamString = weatherSt.replaceFirst(REMOVE_PATTERN,"");
				
						
				String queryString = "http://www.google.com/ig/api?weather="
						+ cityParamString;
				/* Replace blanks with HTML-Equivalent. */
				url = new URL(queryString.replace(" ", "%20"));

				/* Get a SAXParser from the SAXPArserFactory. */
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				/* Get the XMLReader of the SAXParser we created. */
				XMLReader xr = sp.getXMLReader();

				/*
				 * Create a new ContentHandler and apply it to the
				 * XML-Reader
				 */
				GoogleWeatherHandler gwh = new GoogleWeatherHandler();
				xr.setContentHandler(gwh);

				/* Parse the xml-data our URL-call returned. */
				xr.parse(new InputSource(url.openStream()));
			
				WeatherSet ws = gwh.getWeatherSet();
				
				WeatherCurrentCondition aWCC = ws.getWeatherCurrentCondition();
				int tempMin = aWCC.getTempCelsius();
				
				response = "Temperature of "+cityParamString+" is "+ tempMin + " degree celsius"; 
			} catch (Exception e) {
				
				Log.e( "WeatherQueryError", e.toString());
			}
			
			return response; 
	    }
	    
	    // Implements TextToSpeech.OnInitListener.
	    public void onInit(int status) {
	        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
	        if (status == TextToSpeech.SUCCESS) {
	            // Set preferred language to US english.
	            // Note that a language may not be available, and the result will indicate this.
	          /*  int result = mTts.setLanguage(Locale.US);
	            // Try this someday for some interesting results.
	            // int result mTts.setLanguage(Locale.FRANCE);
	            if (result == TextToSpeech.LANG_MISSING_DATA ||
	                result == TextToSpeech.LANG_NOT_SUPPORTED) {
	               // Lanuage data is missing or the language is not supported.
	                Log.e(TAG, "Language is not available.");
	            } else {
	                // Check the documentation for other possible result codes.
	                // For example, the language may be available for the locale,
	                // but not for the specified country and variant.

	                // The TTS engine has been successfully initialized.
	                // Allow the user to press the button for the app to speak again.
	                mAgainButton.setEnabled(true);
	                // Greet the user.
	                sayHello();
	                */
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
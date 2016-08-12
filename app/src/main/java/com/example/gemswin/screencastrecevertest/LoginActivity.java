package com.example.gemswin.screencastrecevertest;
   	




import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {
	
	
	Button login;
	EditText user;
	String username;
	String port;
	String password;
	EditText pass;
	EditText portedit;
	JSONParser jParser5 = new JSONParser();
	String	userget;
	String	passget;
	public static String	portget,studClass;
	JSONObject json;
	private PrefManager pref;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reciever);
    //    getActionBar().hide();
		
        
        pref = new PrefManager(getApplicationContext());
        AlertDialogManager alert = new AlertDialogManager();
	     ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

			// Check if Internet present
			if (!cd.isConnectingToInternet()) {
				// Internet Connection is not present
				alert.showAlertDialog(LoginActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
				// stop executing code by return
				return;
			}
    
       if(pref.getLogin() != null)
       {
    	   /*Intent reload = new Intent(LoginActivity.this, LoginActivity.class);
			startActivity(reload);
			finish();*/
    	   
    	   if( pref.getLogin().equals("client") )
           {
           	
           	
   	        // Toast.makeText(getApplicationContext(), "You are succesfully logged in as ADMIN.",Toast.LENGTH_SHORT).show();
   				
   			Intent admin = new Intent(LoginActivity.this, MainActivity_Reciever.class);
   			startActivity(admin);
   			finish();
           	
           }

    	   
       }
         
        
         user = (EditText)findViewById(R.id.roll);
         pass = (EditText)findViewById(R.id.pass);
		 portedit = (EditText)findViewById(R.id.port);
        
 
         
        login = (Button) findViewById(R.id.login);
        
        
        login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				
				
					 username = user.getText().toString();
				     password = pass.getText().toString();
				     port = 	portedit.getText().toString();
				     
				     new login().execute();
					
					/*if(username.equals("admin") && password.equals("admin"))
					{
					
					Intent admin = new Intent(LoginActivity.this, admin.class);
					
					startActivity(admin);
					finish();
					}
					
					if(username.equals("user") && password.equals("user"))
						{
						Intent zebra = new Intent(LoginActivity.this, buttons.class);
						
						startActivity(zebra);
						finish();
						
					}
					*/
				
								
			}
		});
        
        
        
        
    }
    
    
    private class login extends AsyncTask<String, Void, String> {
    	
    	private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage("Logging in...");
            this.dialog.show();
        }

    	
    	
 
		@Override
        protected String doInBackground(String... urls) 
        {
        	    WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                //String ip1 = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                String    ipPost = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());


        	
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roll_number", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("port", port));
			params.add(new BasicNameValuePair("ip", ipPost));
			String log = "http://176.32.230.250/anshuli.com/ScreenCast/loginClient.php";


            json = jParser5.makeHttpRequest(log, "POST", params);


	
     		
        	
    
        	//visible
            return null;
            
            
            
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {  //gone
           // //System.out.println("RESULT : " + result);
        	
        	this.dialog.dismiss();
        	try {
				// Checking for SUCCESS TAG
				
				
				
				
				//Toast.makeText(MainActivity_Reciever.this, (CharSequence) json, 1).show();
			
					 JSONArray account= json.getJSONArray("client");
				        for(int i = 0; i < account.length(); i++)
				        {
				        	json =account.getJSONObject(i);
				        	
				        		userget=json.getString("ROLL_NUMBER");
				        	 	passget= json.getString("PASSWORD");
							    portget= json.getString("PORT");

				        	 	 studClass = json.getString("Class");
				        	 	String name = json.getString("NAME");
							    pref.setName(name);
							    pref.setSerialNo(userget);
							//pref.setBatch(portget);

;                               pref.setBatch(studClass);
				        	 	
				        }
				        

				    	   
				    	   if(username.equals(userget) && password.equals(passget) && port.equals(portget))
				    	   {
				    	Toast.makeText(getApplicationContext(), "You are succesfully logged in.",Toast.LENGTH_SHORT).show();
							
						Intent client = new Intent(LoginActivity.this, MainActivity_Reciever.class);
						
						startActivity(client);
						finish();
						pref.setLogin("client");
						
						
						}
						

						

				       else
				       {
				    	   
				    	   Toast.makeText(getApplicationContext(),"Login failed:Check your credentials.",Toast.LENGTH_SHORT).show();
							 
				       }
				        
        	}
        	catch (Exception e) 
        	{
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),"Network connection Error!!!",Toast.LENGTH_LONG).show();
			}
        	
        	
        	
        	
        	
        	
       }
    }



    
}

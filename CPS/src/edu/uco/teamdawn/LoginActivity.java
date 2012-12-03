package edu.uco.teamdawn;

import android.app.Activity;
import android.app.AlertDialog;

import java.sql.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	public static String rslt= "";
	public static String username = "";
	public static String type = "";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutlogin);
		
		etUsername = (EditText) findViewById(R.id.editText1);
		etPassword = (EditText) findViewById(R.id.editText2);
		tvLoginFail = (TextView) findViewById(R.id.tvLoginFailed);
		btnLogin = (Button) findViewById(R.id.bLogin);
		bRegister = (Button) findViewById(R.id.bRegister);

		btnLogin.setOnClickListener(new View.OnClickListener() {
	
		
			public void onClick(View v) {
				tvLoginFail.setVisibility(View.INVISIBLE);
				
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				String result = "";
				//CallableStatement statement = null;
				//boolean value = false;
				
								
				Log.v("result", result);

				try {
					if (VerifyUser(username, password)) {

						Intent openUser = new Intent(getBaseContext(),
								SelectSpotActivity.class);
						startActivity(openUser);
						
					} else {
						etUsername.setText("");
						etPassword.setText("");
						tvLoginFail.setVisibility(View.VISIBLE);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		bRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(),
						RegisterWebViewActivity.class);
				startActivity(i);

			}

		});

	}

	// send to database for verification
	public boolean VerifyUser(String u, String p) throws InterruptedException {
		AlertDialog ad=new AlertDialog.Builder(this).create();
		try{
		String a=u;
	    String b=p;
	    rslt="START";	        
	    Caller c=new Caller();
	    c.a=a;
	    c.b=b;
	    c.ad=ad;
	    c.join();
	    c.start();
	    while(rslt=="START")
	    {
	     	try
	       	{
	       		Thread.sleep(10);
	       		
	       	}catch(Exception ex)
	       	{
	       		
	       	}
	    }
	    
	    ad.setMessage(a + ", " + b + ", " + rslt);
	    
		}catch(Exception ex)
	        {
	        	ad.setTitle("Error!");
	        	ad.setMessage(ex.toString());
	        	//ad.show();
	        }
	       ad.show();
	    
	       
	if(rslt.equals("true")) {
	    //if (rslt.equals("Commuter") || rslt.equals("FACULTY 24HR") || rslt.equals("FACULTY") ||
	    	//	rslt.equals("HOUSING") || rslt.equals("MULTI") || rslt.equals("VISITOR")){
	    	type = rslt;
	    	username = u;
	    //(rslt == "Commuter" /*|| rslt == "FACULTY 24HR" || rslt == "VISITOR" || rslt == "FACULTY" || 
	    		//rslt == "HOUSING" || rslt == "MULTI"*/) {
	    	return true;
	    } else {
	    	return false;
	    }
	    
		//Log.v("w", u);
		//Log.v("w", p);

		//if (u.equals("op") && p.equals("123")) {
		//	Log.v("w", "if statement ran");
		//	return true;
		//} else {

		//	return false;
		//}

	}

	EditText etUsername;
	EditText etPassword;
	Button btnLogin;
	TextView tvLoginFail;
	Button bRegister;

}

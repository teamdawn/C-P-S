package edu.uco.teamdawn;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	public static String rslt= "";
	public static String username = "";
	public static String type = "";
	public static String spot = "";
	public static boolean check = false;
	
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
			
				Log.v("result", result);

				try {
					if (VerifyUser(username, password)) {
						// return spot
						isUserCheckedIn(username);
						if(!check) {
							Intent selectSpot = new Intent(LoginActivity.this, 
									SelectSpotActivity.class);
							startActivity(selectSpot);
						} else {
							Intent viewSpot = new Intent(LoginActivity.this, 
									ViewSpotActivity.class);
							startActivity(viewSpot);
						}
							
						
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

	protected String isUserCheckedIn(String username2) {
		// TODO Auto-generated method stub
		CallSoap cs = new CallSoap();
		spot = cs.CallIsUserCheckedIn(username2);
		Log.v("spot", spot);
		if(spot.equals("null")) {
			check = false;
			return "null";
		} else {
			check = true;
			LotItemizedOverlay.selectedSpotNumber = Integer.parseInt(spot);
			return spot;
		}
	}

	// send to database for verification
	// return value is if user is checked in
	public boolean VerifyUser(String username, String password) throws InterruptedException {
		AlertDialog ad=new AlertDialog.Builder(this).create();
		try{
			CallSoap cs = new CallSoap();
			rslt = cs.CallVerifyUser(username, password);
	    
	    ad.setMessage(username + ", " + password + ", " + rslt);
	    
		}catch(Exception ex) {
	        ad.setTitle("Error!");
	        ad.setMessage(ex.toString());
	    }
	    ad.show();
	     
	    if(rslt.equals("true")) {
	    	//type = rslt;
	    	this.username = username;
	    	return true;
	    } else {
	    	return false;
	    }
	}

	EditText etUsername;
	EditText etPassword;
	Button btnLogin;
	TextView tvLoginFail;
	Button bRegister;

}

package edu.uco.teamdawn;

import android.app.Activity;
import java.sql.*;
//import com.microsoft.sqlserver.jdbc.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity {

	public static String connectionString = "jdbc:sqlserver://pjy6iajgqw.database.windows.net:1433" + ";" 
			+ "database=teamdawn_db;user=teamdawn@pjy6iajgqw;password={fashion123!}" + ";" 
			+ "encrypt=true" + ";" 
			+ "hostNameInCertificate=*.database.windows.net" + ";" 
			+ "loginTimeout=30" + ";";
	
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layoutlogin);

			etUsername = (EditText) findViewById(R.id.editText1);
			etPassword = (EditText) findViewById(R.id.editText2);
			tvLoginFail = (TextView) findViewById(R.id.tvLoginFailed);
			btnLogin = (Button) findViewById(R.id.bLogin);
			bRegister =(Button)findViewById(R.id.bRegister);
			

			btnLogin.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					tvLoginFail.setVisibility(View.INVISIBLE);
					
					if (VerifyUser(etUsername.getText().toString(), etPassword
							.getText().toString())) {

						//Log.v("w", "verified");

						Intent openUser = new Intent(getBaseContext(), ViewSpotActivity.class);
						 
						 startActivity(openUser);

					} else {
						etUsername.setText("");
						etPassword.setText("");
						tvLoginFail.setVisibility(View.VISIBLE);
					}
				}
			});
			
			bRegister.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getBaseContext(), RegisterWebViewActivity.class);
					startActivity(i);
					
				}
				
			});

		}
		
		

		// send to database for verification
		public boolean VerifyUser(String u, String p) {
			Log.v("w", u);
			Log.v("w", p);

			if (u.equals("op") && p.equals("123")) { 
				Log.v("w", "if statement ran");
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

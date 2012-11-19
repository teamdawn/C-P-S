package edu.uco.teamdawn;

import android.app.Activity;
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

	String connectionString = "jdbc:sqlserver://pjy6iajgqw.database.windows.net:1433;"
			+ "database=teamdawn_db;"
			+ "user=teamdawn@pjy6iajgqw;"
			+ "password={fashion123!};"
			+ "encrypt=true;"
			+ "hostNameInCertificate=*.database.windows.net;"
			+ "loginTimeout=30;";
		
		final String mobileServiceUrl = 
                "https://cps.azure-mobile.net/tables/Lot";
        final String mobileServiceAppId = 
        		"iRlBQCSbmkzvrkLLXVZOyryIXtFUfb62";
	
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
				CallableStatement statement = null;
				boolean value = false;
				
				Connection connection;
				//try {
					//connection = DriverManager.getConnection(connectionString);
					//Log.v("test","test");
					//String query = "call PROCEDURE sp_checkValidUser(?,?,?)";
					//statement = connection.prepareCall(query);
					//statement.setString(1, username);
				    //statement.setString(2, password);
				    //statement.registerOutParameter(3, Types.BOOLEAN);
					//statement.execute();
					//value = statement.getBoolean(3);
					//statement.close();
				//} catch (SQLException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				//}
				
				Log.v("result", result);
				//if(value) {
				if (VerifyUser(etUsername.getText().toString(), etPassword
						.getText().toString())) {

					Intent openUser = new Intent(getBaseContext(),
							SelectSpotActivity.class);
					startActivity(openUser);
				//}
				} else {
					etUsername.setText("");
					etPassword.setText("");
					tvLoginFail.setVisibility(View.VISIBLE);
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

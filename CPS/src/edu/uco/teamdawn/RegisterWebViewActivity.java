package edu.uco.teamdawn;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class RegisterWebViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutwebview);
		
		wv = (WebView)findViewById(R.id.wvRegister);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl("www.google.com"); // webaddress here.
		 	
		 	
	}

	private WebView wv;
	
	
}

package edu.uco.teamdawn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.*;

public class SpotViewActivity extends Activity {

	TextView lot;
	TextView spot;
	Button checkInOut;
	Button cancelReservation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spotview);
		
		lot = (TextView) findViewById(R.id.lotNumber);
		spot = (TextView) findViewById(R.id.spotNumber);
		
		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelButton);
		
		//RelativeLayout rl = (RelativeLayout) findViewById(R.id.rLayout);
		Intent intent = new Intent(SpotViewActivity.this, LotMapActivity.class);
		intent.putExtra("spotActivity", true);
		startActivity(intent);
	}
	
	
	
}
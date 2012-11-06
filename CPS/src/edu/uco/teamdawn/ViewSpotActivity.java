package edu.uco.teamdawn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;

public class ViewSpotActivity extends FragmentActivity {

	TextView lot;
	TextView spot;
	Button checkInOut;
	Button cancelReservation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewspotactivity);
		
		lot = (TextView) findViewById(R.id.lotNumber);
		spot = (TextView) findViewById(R.id.spotNumber);
		
		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelReservationButton);
		
		//RelativeLayout rl = (RelativeLayout) findViewById(R.id.rLayout);
		//Intent intent = new Intent(ViewSpotActivity.this, LotMapActivity.class);
		//intent.putExtra("spotActivity", true);
		//startActivity(intent);
	}
	
	
	
}
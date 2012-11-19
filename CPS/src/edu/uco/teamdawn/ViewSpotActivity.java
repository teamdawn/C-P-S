package edu.uco.teamdawn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;

public class ViewSpotActivity extends FragmentActivity
{
	TextView lotNumber;
	TextView spotNumber;
	Button checkInOut;
	Button cancelReservation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewspotactivity);
		
        //COUNTER
        //MyCounter counter = new MyCounter(5000,1000);
        //counter.start();
        
		lotNumber = (TextView) findViewById(R.id.lotNumber);
		spotNumber = (TextView) findViewById(R.id.spotNumber);
		
		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelReservationButton);
	}
}
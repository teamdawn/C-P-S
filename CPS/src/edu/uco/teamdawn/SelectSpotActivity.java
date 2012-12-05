package edu.uco.teamdawn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectSpotActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutselect);
		
		boolean reservationCanceled = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    reservationCanceled = extras.getBoolean("extra");
		}
		
		if(reservationCanceled) {
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setTitle("Reservation Canceled");
			ad.setMessage("Your reservation has been cancelled.");
		}

		Button buttonReserve = (Button) findViewById(R.id.bReserve);

		buttonReserve.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// if web service returns true, go to view spot
				
				if (reserveUserSpot()) {
					Log.v("test", "reserved spot");
					Intent viewSpot = new Intent(SelectSpotActivity.this,
						ViewSpotActivity.class);
					startActivity(viewSpot);
				} else {
					Log.v("fail", "reserve spot failed");
				}
			}
		});
	}
	// if spot number and lot id are not 0
	// call web service to update user spot as 'RESERVED'
	private boolean reserveUserSpot() {
		//LotItemizedOverlay.selectedLotID;		// check for non-zero
		//LotItemizedOverlay.selectedSpotNumber;	// check for non-zero
		String succ = "";
		
		AlertDialog ad = new AlertDialog.Builder(this).create();
		CallSoap cs = new CallSoap();
		try {
			
			if(LotItemizedOverlay.selectedSpotNumber != 0 && LotItemizedOverlay.selectedLotID != 0) {
				succ = cs.CallReserveUserSpot(LotItemizedOverlay.selectedSpotNumber, LoginActivity.username, 
						LotItemizedOverlay.selectedLotID);
			} else {
				succ = "false";
			}
		
			ad.setMessage(LotItemizedOverlay.selectedSpotNumber + ", " + LoginActivity.username + ", " + LotItemizedOverlay.selectedLotID + ", " + succ);

		} catch (Exception ex) {
			Log.v("Error", ex.getMessage().toString());
		}
		ad.show();
	
		if (succ.equals("true")) {
			return true;
		} else {
			return false;
		}
		
	}
}

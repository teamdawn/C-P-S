package edu.uco.teamdawn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewSpotActivity extends FragmentActivity implements
		OnClickListener {
	TextView lot;
	TextView spot;
	Button checkInOut;
	Button cancelReservation;

	// public MyCounter counter = new MyCounter(10000, 1000);
	private static final int WARNING = 0;
	private static final int FINAL = 1;
	private int timeLeft = 0;
	private String rslt = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewspotactivity);

		CallSoap cs = new CallSoap();
		// return if user is already checked into a spot
		rslt = cs.CallIsUserCheckedIn(LoginActivity.username);
		//rslt = "false";
		

		lot = (TextView) findViewById(R.id.lot);
		spot = (TextView) findViewById(R.id.spot);

		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelReservationButton);

		if (rslt.equals("true")) {
			checkInOut.setText("Check Out");
		} else {
			checkInOut.setText("Check In");
		}
		
		checkInOut.setOnClickListener(this);

		cancelReservation.setOnClickListener(this);

		// counter.start();

		lot.setText("Lot: " + LotItemizedOverlay.selectedLotID);
		spot.setText("Spot: " + LotItemizedOverlay.selectedSpotNumber);
	}

	/*
	 * public class MyCounter extends CountDownTimer {
	 * 
	 * public MyCounter(long millisInFuture, long countDownInterval) {
	 * super(millisInFuture, countDownInterval); // TODO Auto-generated
	 * constructor stub
	 * 
	 * 
	 * }
	 * 
	 * @Override public void onFinish() { // TODO Auto-generated method stub
	 * showDialog(FINAL);
	 * 
	 * }
	 * 
	 * @Override public void onTick(long millisUntilFinished) { // TODO
	 * Auto-generated method stub
	 * 
	 * timeLeft = (int) millisUntilFinished;
	 * 
	 * if (millisUntilFinished < 5000 && millisUntilFinished > 3000) {
	 * 
	 * showDialog(WARNING); } else if (millisUntilFinished < 3000) {
	 * dismissDialog(WARNING); }
	 * 
	 * }
	 * 
	 * }
	 */

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.checkInOutButton:
			CallSoap cs = new CallSoap();
			if (rslt.equals("true")) {
				cs.CallCheckOutUserSpot(LotItemizedOverlay.selectedSpotNumber,
						LotItemizedOverlay.selectedLotID);
				checkInOut.setText("Check In");
				Intent selectSpot = new Intent(ViewSpotActivity.this, SelectSpotActivity.class);
				startActivity(selectSpot);
			} else {
				cs.CallCheckInUserSpot(LotItemizedOverlay.selectedSpotNumber,
						LotItemizedOverlay.selectedLotID);
				checkInOut.setText("Check Out");
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setTitle("Check In");
				ad.setMessage("You have successfully checked into Lot " + LotItemizedOverlay.selectedLotID 
						+ " and Spot " + LotItemizedOverlay.selectedSpotNumber + ".");
			}
		case R.id.cancelReservationButton:
			CallSoap cs1 = new CallSoap();
			cs1.CallCancelReservation(LotItemizedOverlay.selectedSpotNumber,
					LotItemizedOverlay.selectedLotID);
			Intent selectSpot = new Intent(ViewSpotActivity.this, SelectSpotActivity.class);
			selectSpot.putExtra("extra", true);
			startActivity(selectSpot);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case WARNING:
			AlertDialog.Builder wDialog = new AlertDialog.Builder(this);
			wDialog.setTitle("Reminder : You Have " + timeLeft
					+ " minutes Left");

			AlertDialog alert = wDialog.show();
			return alert;

		case FINAL:
			AlertDialog.Builder fDialog = new AlertDialog.Builder(this);
			fDialog.setTitle("final notice");
			fDialog.setMessage("Your Reservation Time has Expired!");
			fDialog.setIcon(android.R.drawable.alert_light_frame);
			fDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// auto-cancel reservation
							Intent selectSpot = new Intent(
									ViewSpotActivity.this,
									SelectSpotActivity.class);
							startActivity(selectSpot);
						}

					});

			alert = fDialog.show();
			boolean check = false;
			CallSoap cs = new CallSoap();
			cs.CallSetUserStatus(LoginActivity.username, check);
			return alert;
		}
		return null;
	}

}

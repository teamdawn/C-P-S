package edu.uco.teamdawn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;

public class ViewSpotActivity extends FragmentActivity {
	TextView lotNumber;
	TextView spotNumber;
	Button checkInOut;
	Button cancelReservation;
	
	public MyCounter counter = new MyCounter(10000, 1000);
	private static final int WARNING = 0;
	private static final int FINAL = 1;
	private int timeLeft = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewspotactivity);

		lotNumber = (TextView) findViewById(R.id.lotNumber);
		spotNumber = (TextView) findViewById(R.id.spotNumber);

		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelReservationButton);
		counter.start();
	}

	public class MyCounter extends CountDownTimer {

		public MyCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
			
			
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			showDialog(FINAL);
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

			timeLeft = (int) millisUntilFinished;
			
			if (millisUntilFinished < 5000 && millisUntilFinished > 3000) {

				showDialog(WARNING);
			} else if (millisUntilFinished < 3000) {
				dismissDialog(WARNING);
			}

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
							Intent selectSpot = new Intent(ViewSpotActivity.this,
									SelectSpotActivity.class);
							startActivity(selectSpot);
							

						}

					});

			alert = fDialog.show();

			return alert;
		}
		return null;
	}

}

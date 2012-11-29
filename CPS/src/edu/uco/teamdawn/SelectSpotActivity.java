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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SelectSpotActivity extends FragmentActivity implements
		OnItemSelectedListener {

	private String[] lot = { "a", "b", "c" };
	private String[] spot = { "1", "32", "3" };

		
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutselect);

		Spinner spinnerLot = (Spinner) findViewById(R.id.spinner);
		Spinner spinnerSpot = (Spinner) findViewById(R.id.sspot);
		
		Button buttonReserve = (Button) findViewById(R.id.bReserve);

		Toast.makeText(SelectSpotActivity.this, "toasted", Toast.LENGTH_LONG).show();
		
		ArrayAdapter<String> lotAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, lot);
		ArrayAdapter<String> spotAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, spot);

		spinnerLot.setAdapter(lotAdapter);
		spinnerSpot.setAdapter(spotAdapter);

		spinnerLot.setOnItemSelectedListener(this);
		spinnerSpot.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						parent.getContext(),
						"OnItemSelectedListener : "
								+ parent.getItemAtPosition(pos).toString(),
						Toast.LENGTH_SHORT).show();

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		buttonReserve.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// make a stored procedure to reserve spot
				// if ( stored procedure went through ) {
				// if ( lot and spot are selected ) {
				
				Intent viewSpot = new Intent(SelectSpotActivity.this,
						ViewSpotActivity.class);
				startActivity(viewSpot);
				
			}

		});

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(
				parent.getContext(),
				"OnItemSelectedListener : "
						+ parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	
}


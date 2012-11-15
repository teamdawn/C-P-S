package edu.uco.teamdawn;

import com.google.android.maps.MapView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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

	private MapView mapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutselect);

		Spinner spinnerLot = (Spinner) findViewById(R.id.spinner);
		Spinner spinnerSpot = (Spinner) findViewById(R.id.sspot);
		Button buttonReserve = (Button) findViewById(R.id.bReserve);

		Toast.makeText(SelectSpotActivity.this, "FY$", Toast.LENGTH_LONG)
				.show();

		Drawable defaultMarker = this.getResources().getDrawable(
			R.drawable.androidmarker);
	//	LotItemizedOverlay lotOverlay = new LotItemizedOverlay(defaultMarker,
		//		this, 1, "Lot 1", 35654125, -97473500, 19, mapView);

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

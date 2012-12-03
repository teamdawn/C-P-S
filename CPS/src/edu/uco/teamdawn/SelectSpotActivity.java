package edu.uco.teamdawn;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	public String[] availSpotByLotID;
	public String spotsByLotID;
	public static String[] lotsByUserType;
	private String[] lot = { "1", "2", "3" };
	private String[] spot = { "1", "32", "3" };
	private int lotID = 14;
	public static String type = "Commuter";
	public static String reserve = "";
	public int spotNumber = 101;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutselect);

		 getSpotsByLotID();
		/*try {
			lotsByUserType();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		Spinner spinnerLot = (Spinner) findViewById(R.id.spinner);
		Spinner spinnerSpot = (Spinner) findViewById(R.id.sspot);

		Button buttonReserve = (Button) findViewById(R.id.bReserve);

		Toast.makeText(SelectSpotActivity.this, "toasted", Toast.LENGTH_LONG)
				.show();

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
				reserveUserSpot();
				Intent viewSpot = new Intent(SelectSpotActivity.this,
						ViewSpotActivity.class);
				startActivity(viewSpot);

			}

		});

	}

	private void reserveUserSpot() {
		// TODO Auto-generated method stub
		AlertDialog ad = new AlertDialog.Builder(this).create();
		try {
			reserve = "START";
			Caller3 c = new Caller3();
			c.spotNumber = spotNumber;
			c.username = LoginActivity.username;
			c.lotID = lotID;
			c.ad = ad;
			c.join();
			c.start();
			while (reserve == "START") {
				try {
					Thread.sleep(10);

				} catch (Exception ex) {

				}
			}

			ad.setMessage(spotNumber + ", " + LoginActivity.username + ", " + lotID + ", " + reserve);

		} catch (Exception ex) {
			ad.setTitle("Error!");
			ad.setMessage(ex.toString());
			// ad.show();
		}
		ad.show();
	}

	private void lotsByUserType() throws InterruptedException {
		// TODO Auto-generated method stub
		AlertDialog ad = new AlertDialog.Builder(this).create();
		lotsByUserType[0] = "0";
		Caller2 c = new Caller2();
		c.type = type;
		c.ad = ad;
		c.join();
		c.start();
		while (lotsByUserType[0].equals("0")) {
			try {
				Thread.sleep(10);

			} catch (Exception ex) {

			}
		}
		ad.setMessage(lotsByUserType.toString());

		// lot = lotsByUserType;
		ad.show();
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

	private void getSpotsByLotID() {
		// TODO Auto-generated method stub
		AlertDialog ad = new AlertDialog.Builder(this).create();
		CallSoap cs = new CallSoap();
		Object[] object = cs.CallGetSpotsByLotID(lotID);
		//ad.setMessage(jsonObject.toString());

		/*
		 * JSONObject[] todos = null; Object o = new Object(); CallSoap cs = new
		 * CallSoap(); o = cs.CallGetSpotsByLotID(lotID); ByteArrayOutputStream
		 * baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new
		 * ObjectOutputStream(baos); oos.writeObject(o); oos.flush();
		 * oos.close(); InputStream in = new
		 * ByteArrayInputStream(baos.toByteArray());
		 * 
		 * BufferedReader bufferReader = new BufferedReader( new
		 * InputStreamReader(in));
		 * 
		 * // responseString will hold our JSON data StringBuilder
		 * responseString = new StringBuilder(); String line;
		 * 
		 * // Loop through the buffered input, reading JSON data while ((line =
		 * bufferReader.readLine()) != null) { responseString.append(line); }
		 * 
		 * // Convert responseString into a JSONArray JSONArray jsonArray = new
		 * JSONArray(responseString.toString());
		 * 
		 * // Will hold an array of JSON objects todos = new
		 * JSONObject[jsonArray.length()];
		 * 
		 * // values is very important. It is the string array that will // get
		 * assigned to our ListView control. String[] values = new
		 * String[jsonArray.length()];
		 * 
		 * // Loop through the objects. The ultimate goal is to have // an array
		 * of strings called "values" for (int i = 0; i < jsonArray.length();
		 * i++) { todos[i] = jsonArray.getJSONObject(i); values[i] =
		 * todos[i].get("SpotNumber").toString(); ad.setMessage(values[i] +
		 * ", "); }
		 */

		ad.show();

	}

}

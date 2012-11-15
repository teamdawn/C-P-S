package edu.uco.teamdawn;

import com.google.android.maps.MapView;
import android.os.Bundle;
import android.app.Activity;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Add these import statements
import android.util.Log;     // for logging

import org.json.JSONArray;   // for JSONArray
import org.json.JSONObject;  // for JSONObject

import java.io.InputStream;         // for reading the response as bytes
import java.io.BufferedInputStream; // for reading the response as buffered bytes  
import java.io.BufferedReader;      // for reading bytes in a buffered manner
import java.io.InputStreamReader;   // for reading bytes into BufferedReader
import java.net.HttpURLConnection;  // for HttpURLConnection
import java.net.URL;                // for URL

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class ViewSpotActivity extends FragmentActivity {

	private MapView mapView;
	LotItemizedOverlay lot;
	
	TextView lotNumber;
	TextView spotNumber;
	Button checkInOut;
	Button cancelReservation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewspotactivity);
		
		final String mobileServiceUrl = 
                "https://cps.azure-mobile.net/tables/Lot";
        final String mobileServiceAppId = 
        		"iRlBQCSbmkzvrkLLXVZOyryIXtFUfb62";
		
		Drawable defaultMarker = this.getResources().getDrawable(R.drawable.androidmarker);
		lot = new LotItemizedOverlay(defaultMarker, this, 1, "Lot 1", 35654125, -97473500, 19, mapView);
		
		lotNumber = (TextView) findViewById(R.id.lotNumber);
		spotNumber = (TextView) findViewById(R.id.spotNumber);
		
		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelReservationButton);
		
		//RelativeLayout rl = (RelativeLayout) findViewById(R.id.rLayout);
		//Intent intent = new Intent(ViewSpotActivity.this, LotMapActivity.class);
		//intent.putExtra("spotActivity", true);
		//startActivity(intent);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater oMenu = getMenuInflater();
        oMenu.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onMenuItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.mapStreetView:
    			mapView.setSatellite(false);
    			mapView.invalidate();
    			return true;
	
    		case R.id.mapSatView:
    			mapView.setSatellite(true);
    			mapView.invalidate();
    			return true;
    			
    		case R.id.showRouteWithMaps:
    			// Shows route, but no navigation
    			
    			Intent intentMaps = new Intent(Intent.ACTION_VIEW, 
    								Uri.parse("http://maps.google.com/maps?" 
    								+ "saddr=35.466682,-97.414251&"
    								+ "daddr="
    								+ lot.getSelectedSpot().getCenterLatString() + ","
    								+ lot.getSelectedSpot().getCenterLongString()));
    			intentMaps.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    			this.startActivity(intentMaps);
    			return true;
    			
    		case R.id.launchNavigation:
    			// Navigation Beta version in emulator, warns you not to use while driving.
    			// Not Beta on real devices?
    			Intent intentNavBeta = new Intent(Intent.ACTION_VIEW, 
    									Uri.parse("google.navigation:q="
    									+ lot.getSelectedSpot().getCenterLatString() + ","
    									+ lot.getSelectedSpot().getCenterLongString()));
    			this.startActivity(intentNavBeta);
    			return true;
    	}
    	return false;
    }

    protected boolean isRouteDisplayed()
    {
    	return false;
    }

	
	
}
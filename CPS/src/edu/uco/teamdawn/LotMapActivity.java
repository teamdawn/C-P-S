package edu.uco.teamdawn;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import android.util.Log;     // for logging
import org.json.JSONArray;   // for JSONArray
import org.json.JSONObject;  // for JSONObject
import java.io.InputStream;         // for reading the response as bytes
import java.io.BufferedInputStream; // for reading the response as buffered bytes  
import java.io.BufferedReader;      // for reading bytes in a buffered manner
import java.io.InputStreamReader;   // for reading bytes into BufferedReader
import java.net.HttpURLConnection;  // for HttpURLConnection
import java.net.URL;                // for URL

public class LotMapActivity extends MapActivity
{
	private boolean showSatelliteView = true;
	private MapView mapView;
	private MapController mapController;
	private LotItemizedOverlay lot;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.lotmapactivity);
        
    	mapView = (MapView) findViewById(R.id.lotmapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(showSatelliteView);
                
        if(LotItemizedOverlay.selectedSpotNumber == 0)
        	this.updateLotID(17);
        else
        	this.updateLotID(LotItemizedOverlay.selectedLotID);
    }
    
    private void updateLotID(int lotID)
    {
    	mapView.getOverlays().clear();
    	
    	Drawable defaultMarker = this.getResources().getDrawable(R.drawable.androidmarker);
        
    	// TODO: Download lot from database in separate thread
        lot = new LotItemizedOverlay(defaultMarker, this, lotID, "Lot 17", 35654125, -97473500, 19, mapView);
     
        // Download spots from database in a separate thread
        new DownloadSpotsTask().execute();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	SubMenu sm = menu.addSubMenu(1, 6, 5, "Lots");
    	sm.add(2, 6, 0, "Lot 13");
    	sm.add(2, 7, 1, "Lot 14");
    	sm.add(2, 8, 2, "Lot 17");
    	sm.add(2, 9, 3, "Lot 18");
        MenuInflater oMenu = getMenuInflater();
        oMenu.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	//Toast.makeText(this, item.getItemId(), Toast.LENGTH_LONG).show();
    	switch(item.getItemId()) {
    		case 5:
    			return true;
    		case 6:
    			updateLotID(13);
    			return true;
    		case 7:
    			updateLotID(14);
    			return true;
    		case 8:
    			updateLotID(17);
    			return true;
    		case 9:
    			updateLotID(18);
    			return true;
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
    			LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
    			{
    				// Issue message to turn on GPS
    				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    				dialog.setTitle("Enable GPS");
    				dialog.setMessage("Please enable GPS, then try again.");
    				dialog.show();
    				return true;
    			}
    			
    			// Define the criteria how to select the location provider -> use default
    		    Criteria criteria = new Criteria();
    		    String provider = lm.getBestProvider(criteria, false);
    		    Location location = lm.getLastKnownLocation(provider);

    		    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("Launching Maps");
				dialog.setMessage("http://maps.google.com/maps?" 
						+ "saddr="
						+ location.getLatitude() + ","
						+ location.getLongitude()
						+ "&daddr="
						+ lot.getSelectedSpot().getCenterLatString() + ","
						+ lot.getSelectedSpot().getCenterLongString());
				dialog.show();
    		    
    			Intent intentMaps = new Intent(Intent.ACTION_VIEW, 
    								Uri.parse("http://maps.google.com/maps?" 
    								+ "saddr="
    								+ location.getLatitude() + ","
    								+ location.getLongitude()
    								+ "&daddr="
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

    @Override
    protected boolean isRouteDisplayed()
    {
    	return false;
    }
    
    private class DownloadSpotsTask extends AsyncTask<Void, Integer, Integer>
    {
    	// TODO: Use stored procedure to get only spots for this lot.
    	private final String mobileServiceUrl = 
                "https://cps.azure-mobile.net/tables/Spot";
        private final String mobileServiceAppId = 
        		"iRlBQCSbmkzvrkLLXVZOyryIXtFUfb62";
        
    	// Do the long-running work in here
        @Override
        protected Integer doInBackground(Void... voids)
        {/*
        	// Temporary hardcoded spots for testing only
            int rowLat = 35654538;
            int colOneLong = -97473800;
            int colOffset = 30;
            int rotation = 45;
            lot.addSpot(new SpotOverlayItem(101, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(102, "AVAILABLE", "none", "FACULTY", rowLat - 2, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(103, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 4, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(104, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 6, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(105, "AVAILABLE", "none", "COMMUTER", rowLat - 8, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(106, "AVAILABLE", "none", "COMMUTER", rowLat - 10, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(107, "AVAILABLE", "none", "HOUSING", rowLat - 12, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(108, "AVAILABLE", "none", "HOUSING", rowLat - 14, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(109, "AVAILABLE", "none", "MULTI", rowLat - 16, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(110, "AVAILABLE", "none", "MULTI", rowLat - 18, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(111, "AVAILABLE", "none", "VISITOR", rowLat - 20, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(112, "AVAILABLE", "none", "VISITOR", rowLat - 22, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(113, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 24, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(114, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 26, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(115, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 28, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(116, "RESERVED", "op", "FACULTY 24HR", rowLat - 30, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(117, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 32, colOneLong + colOffset * 20, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(118, "AVAILABLE", "none", "FACULTY 24HR", rowLat - 34, colOneLong + colOffset * 21, rotation, lot.getZoomLevel()));
            
            // Row 2
            rowLat = 35654388;
            colOneLong = -97473825;
            rotation = 0;
            lot.addSpot(new SpotOverlayItem(201, "AVAILABLE", "none", "FACULTY 24HR", rowLat, colOneLong + colOffset * 0, 90, lot.getZoomLevel()));
            /*
            lot.addSpot(new SpotOverlayItem(202, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 1, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(203, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(204, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(205, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(206, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(207, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(208, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(209, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(210, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(211, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(212, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(213, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(214, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(215, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(216, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(217, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(218, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(219, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(220, "AVAILABLE", "none", "FACULTY", rowLat, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(221, "OCCUPIED", "none", "FACULTY", rowLat, colOneLong + colOffset * 20, rotation, lot.getZoomLevel()));
            // Row 3
            rowLat = 35654330;
            colOneLong = -97473825;
            rotation = 0;
            lot.addSpot(new SpotOverlayItem(301, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 0, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(302, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 1, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(303, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(304, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(305, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(306, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(307, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(308, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(309, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(310, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(311, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(312, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(313, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(314, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(315, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(316, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(317, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(318, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(319, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(320, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(321, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 20, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(322, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 21, rotation, lot.getZoomLevel()));
            
            // Row 4
            rowLat = 35654218;
            colOneLong = -97473825;
            rotation = 90;
            lot.addSpot(new SpotOverlayItem(401, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(402, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(403, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(404, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(405, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(406, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(407, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(408, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(409, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(410, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(411, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(412, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(413, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(414, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(415, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(416, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(417, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(418, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(419, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 20, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(420, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 21, rotation, lot.getZoomLevel()));
            // Row 5
            rowLat = 35654168;
            colOneLong = -97473825;
            rotation = 90;
            lot.addSpot(new SpotOverlayItem(501, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(502, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(503, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(504, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(505, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(506, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(507, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(508, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(509, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(510, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(511, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(512, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(513, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(514, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(515, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(516, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(517, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(518, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(519, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 20, rotation, lot.getZoomLevel()));

            // Row 6
            rowLat = 35654049;
            colOneLong = -97473797;
            rotation = 0;
            lot.addSpot(new SpotOverlayItem(601, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 0, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(602, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 1, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(603, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(604, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(605, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(606, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(607, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(608, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(609, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(610, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(611, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(612, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(613, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(614, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(615, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(616, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(617, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(618, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(619, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(620, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
    	    // Row 7
            rowLat = 35653994;
            colOneLong = -97473797;
            lot.addSpot(new SpotOverlayItem(701, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 0, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(702, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 1, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(703, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(704, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(705, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(706, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(707, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(708, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(709, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(710, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(711, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(712, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(713, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(714, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(715, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(716, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(717, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(718, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 17, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(719, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 18, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(720, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 19, rotation, lot.getZoomLevel()));
         
            // Row 8
            rowLat = 35653877;
            colOneLong = -97473785;
            lot.addSpot(new SpotOverlayItem(801, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(802, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(803, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(804, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(805, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(806, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(807, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(808, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(809, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(810, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(811, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(812, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(813, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(814, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 15, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(815, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 16, rotation, lot.getZoomLevel()));
            // Row 9
            rowLat = 35653820;
            colOneLong = -97473785;
            lot.addSpot(new SpotOverlayItem(901, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 2, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(902, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 3, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(903, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 4, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(904, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 5, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(905, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 6, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(906, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 7, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(907, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 8, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(908, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 9, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(909, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 10, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(910, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 11, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(911, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 12, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(912, "AVAILABLE", "none", "COMMUTER", rowLat, colOneLong + colOffset * 13, rotation, lot.getZoomLevel()));
            lot.addSpot(new SpotOverlayItem(913, "OCCUPIED", "none", "COMMUTER", rowLat, colOneLong + colOffset * 14, rotation, lot.getZoomLevel()));
			*/
            
        	// Mobile service locked out. Reverting to hardcoded test data
            try
            {
            	/*
        		// Start building the request object to get the data 
                URL url = new URL(mobileServiceUrl);

                // Build a request object to connect to Azure Mobile Services
                HttpURLConnection urlRequest = (HttpURLConnection) url.openConnection();

                // Reading data so the http verb is "GET"
                urlRequest.setRequestMethod("GET");

                // Start building up the request header
                // (1) The data is JSON format 
                // (2) We need to pass the service app id (we get this from the Azure Portal)
                urlRequest.addRequestProperty("Content-Type", "application/json");
                urlRequest.addRequestProperty("ACCEPT", "application/json");
                urlRequest.addRequestProperty("X-ZUMO-APPLICATION", mobileServiceAppId);
                */
                try    
                {
                	/*
                    InputStream in = new BufferedInputStream(
                            urlRequest.getInputStream());
                    BufferedReader bufferReader = new BufferedReader(
                            new InputStreamReader(in));

                    // responseString will hold our JSON data
                    StringBuilder responseString = new StringBuilder();
                    
                    // Loop through the buffered input, reading JSON data
                    String line;
                    while ((line = bufferReader.readLine()) != null) 
                    {
                        responseString.append(line);
                    }
                	 */
                    // Convert responseString into a JSONArray
                	
                			

                	
                	CallSoap cs = new CallSoap();
                    JSONArray jsonArray = new JSONArray(cs.CallGetSpotsByLotID(lot.getLotId()));
            		//JSONArray jsonArray = new JSONArray(rslt);
					
                    // We hold the json results
                    
                    JSONObject[] results = new JSONObject[jsonArray.length()];
                    

                    // Loop through the objects.
                    for (int i = 0; i < jsonArray.length(); i++) 
                    {
                        results[i] = jsonArray.getJSONObject(i);
                        
                        
                        // TODO: Remove check for LotID, after query is fixed to only return correct rows.
                        //int lotID = results[i].getInt("LotId");
                        //if(lotID != lot.getLotId())
                        //	continue;
                        
                        int spotNumber = results[i].getInt("SpotNumber");
                        String status = results[i].getString("Status");
                        String user = results[i].getString("User");
                        String type = results[i].getString("Type");
                        int centerLat = (int)(results[i].getDouble("CenterLat") * 1000000);
                        int centerLong = (int)(results[i].getDouble("CenterLong") * 1000000);
                        int rotation = results[i].getInt("Rotation");

                        Log.v("type", type);
                        
                        lot.addSpot(new SpotOverlayItem(spotNumber, status, user, type,
                        		centerLat, centerLong, rotation, lot.getZoomLevel()));
                    }
					
                } catch (Exception ex) {
                    Log.e("LotMapActivity Failure", "Error getting JSON from Server: "+ ex.getMessage());
                } finally {
                    //urlRequest.disconnect();
                }
              } catch (Exception ex) {
                Log.e("LotMapActivity Failure", "Error opening HTTP Connection: " + ex.getMessage());
              }
        	
        	for(SpotOverlayItem spot : lot.getAllSpots())
        	{
        		// TODO: Check the actual username
        		if(spot.getUser().equals("op"))
        			spot.setSelected(true);
        		
        		spot.refreshOverlay(lot.getZoomLevel());
        	}
        	
            // publishProgress() can be called to provide status updates
            // publishProgress(123);

        	// Escape early if cancel() is called
            // if (isCancelled()) break;
            
            return 0;
        }

        // This is called each time you call publishProgress()
        //@Override
        //protected void onProgressUpdate(Integer... progress) {
        //    setProgressPercent(progress[0]);
        //}

        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(Integer result) {
        	
        	mapView.getOverlays().add(lot);

            mapController = mapView.getController();
            mapController.setCenter(new GeoPoint(lot.getCenterLat(), lot.getCenterLong()));
            mapController.setZoom(lot.getZoomLevel());
            
        	lot.InvalidateMap();
        }
    }
}

package edu.uco.teamdawn;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
                
        Drawable defaultMarker = this.getResources().getDrawable(R.drawable.androidmarker);
        
        // TODO: Download lot from database in separate thread
        lot = new LotItemizedOverlay(defaultMarker, this, 17, "Lot 17", 35654125, -97473500, 19, mapView);
        
        mapView.getOverlays().add(lot);

        mapController = mapView.getController();
        mapController.setCenter(new GeoPoint(lot.getCenterLat(), lot.getCenterLong()));
        mapController.setZoom(lot.getZoomLevel());
        
        // Download spots from database in a separate thread
        new DownloadSpotsTask().execute();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater oMenu = getMenuInflater();
        oMenu.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
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
    			// TODO: Replace saddr coordinates with current GPS coordinates.
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
        {
        	try
            {
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
                
                try    
                {
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

                    // Convert responseString into a JSONArray
                    JSONArray jsonArray = new JSONArray(responseString.toString());

                    // We hold the json results
                    JSONObject[] results = new JSONObject[jsonArray.length()];

                    // Loop through the objects.
                    for (int i = 0; i < jsonArray.length(); i++) 
                    {
                        results[i] = jsonArray.getJSONObject(i);
                        
                        // TODO: Remove check for LotID, after query is fixed to only return correct rows.
                        int lotID = results[i].getInt("LotID");
                        if(lotID != lot.getLotId())
                        	continue;
                        
                        int spotNumber = results[i].getInt("SpotNumber");
                        String status = results[i].getString("Status");
                        String user = results[i].getString("Username");
                        String type = results[i].getString("Type");
                        int centerLat = (int)(results[i].getDouble("CenterLat") * 1000000);
                        int centerLong = (int)(results[i].getDouble("CenterLong") * 1000000);
                        int rotation = results[i].getInt("Rotation");

                        lot.addSpot(new SpotOverlayItem(spotNumber, status, user, type,
                        		centerLat, centerLong, rotation, lot.getZoomLevel()));
                        
                        
                    }

                } catch (Exception ex) {
                    Log.e("LotMapActivity Failure", "Error getting JSON from Server: "+ ex.getMessage());
                } finally {
                    urlRequest.disconnect();
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
        	lot.InvalidateMap();
        }
    }
}

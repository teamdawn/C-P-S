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

import android.util.Log; // for logging
import org.json.JSONArray; // for JSONArray
import org.json.JSONObject; // for JSONObject
import java.io.InputStream; // for reading the response as bytes
import java.io.BufferedInputStream; // for reading the response as buffered bytes  
import java.io.BufferedReader; // for reading bytes in a buffered manner
import java.io.InputStreamReader; // for reading bytes into BufferedReader
import java.net.HttpURLConnection; // for HttpURLConnection
import java.net.URL; // for URL

public class LotMapActivity extends MapActivity {
	private boolean showSatelliteView = true;
	private MapView mapView;
	private MapController mapController;
	private LotItemizedOverlay lot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lotmapactivity);

		mapView = (MapView) findViewById(R.id.lotmapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(showSatelliteView);

		if (LotItemizedOverlay.selectedSpotNumber == 0)
			this.updateLotID(17);
		else
			this.updateLotID(LotItemizedOverlay.selectedLotID);
	}

	private void updateLotID(int lotID) {

		mapView.getOverlays().clear();

		LotItemizedOverlay.selectedLotID = lotID;

		// Drawable defaultMarker = this.getResources().getDrawable(
		// R.drawable.androidmarker);

		// TODO: Download lot from database in separate thread
		// lot = new LotItemizedOverlay(defaultMarker, this, lotID, "Lot 17",
		// 35654125, -97473500, 19, mapView);

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
		// Toast.makeText(this, item.getItemId(), Toast.LENGTH_LONG).show();
		switch (item.getItemId()) {
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
			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				// Issue message to turn on GPS
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("Enable GPS");
				dialog.setMessage("Please enable GPS, then try again.");
				dialog.show();
				return true;
			}

			// Define the criteria how to select the location provider -> use
			// default
			Criteria criteria = new Criteria();
			String provider = lm.getBestProvider(criteria, false);
			Location location = lm.getLastKnownLocation(provider);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Launching Maps");
			dialog.setMessage("http://maps.google.com/maps?" + "saddr="
					+ location.getLatitude() + "," + location.getLongitude()
					+ "&daddr=" + lot.getSelectedSpot().getCenterLatString()
					+ "," + lot.getSelectedSpot().getCenterLongString());
			dialog.show();

			Intent intentMaps = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?" + "saddr="
							+ location.getLatitude() + ","
							+ location.getLongitude() + "&daddr="
							+ lot.getSelectedSpot().getCenterLatString() + ","
							+ lot.getSelectedSpot().getCenterLongString()));
			intentMaps.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			this.startActivity(intentMaps);

			return true;

		case R.id.launchNavigation:
			// Navigation Beta version in emulator, warns you not to use while
			// driving.
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
	protected boolean isRouteDisplayed() {
		return false;
	}

	private class DownloadSpotsTask extends AsyncTask<Void, Integer, Integer> {
		// Do the long-running work in here
		@Override
		protected Integer doInBackground(Void... voids) {
			// First get the Lot
			try {
				CallSoap cs = new CallSoap();
				Log.v("CallSoap", "CallSoap");
				JSONArray jsonArray = new JSONArray(cs.CallGetLotByLotID(LotItemizedOverlay.selectedLotID));
				Log.v("Create JSONArray", "Create JSONArray");
				JSONObject[] results = new JSONObject[jsonArray.length()];
				Log.v("Create JSONObject", "Create JSONObject");
				results[0] = jsonArray.getJSONObject(0);
				Log.v("populate JSONObject", "populate JSONObject");
				
				String name = results[0].getString("Name");
				Log.v("LotName", name);
				int zoom = results[0].getInt("Zoom");
				Log.v("LotZoom", String.valueOf(zoom));
				int centerLat = (int) (results[0].getDouble("CenterLat") * 1000000);
				Log.v("LotLat", String.valueOf(centerLat));
				int centerLong = (int) (results[0].getDouble("CenterLong") * 1000000);
				Log.v("LotLong", String.valueOf(centerLong));

				Drawable defaultMarker = getResources().getDrawable(
						R.drawable.androidmarker);
				Log.v("Drawable", "drawable");

				lot = new LotItemizedOverlay(defaultMarker, getBaseContext(),
						LotItemizedOverlay.selectedLotID, name, centerLat,
						centerLong, zoom, mapView);
				Log.v("LotCreated", "done");
			} catch (Exception ex) {
				Log.e("LotMapActivity Failure",
						"Error getting Lot JSON from Server: " + ex);
			}

			// Now get the spots
			try {
				CallSoap cs = new CallSoap();
				JSONArray jsonArray = new JSONArray(
						cs.CallGetSpotsByLotID(lot.getLotId()));
				JSONObject[] results = new JSONObject[jsonArray.length()];

				// Loop through the objects.
				for (int i = 0; i < jsonArray.length(); i++) {
					results[i] = jsonArray.getJSONObject(i);

					int spotNumber = results[i].getInt("SpotNumber");
					String status = results[i].getString("Status");
					String user = results[i].getString("User");
					String type = results[i].getString("Type");
					int centerLat1 = (int) (results[i].getDouble("CenterLat") * 1000000);
					int centerLong1 = (int) (results[i].getDouble("CenterLong") * 1000000);
					int rotation = results[i].getInt("Rotation");

					Log.v("type", type);

					lot.addSpot(new SpotOverlayItem(spotNumber, status, user,
							type, centerLat1, centerLong1, rotation, lot
									.getZoomLevel()));
				}

			} catch (Exception ex) {
				Log.e("LotMapActivity Failure",
						"Error getting Spots JSON from Server: " + ex);
			}

			return 0;
		}

		// This is called each time you call publishProgress()
		// @Override
		// protected void onProgressUpdate(Integer... progress) {
		// setProgressPercent(progress[0]);
		// }

		// This is called when doInBackground() is finished
		@Override
		protected void onPostExecute(Integer result) {

			for (SpotOverlayItem spot : lot.getAllSpots()) {
				// TODO: Check the actual username
				if (spot.getUser().equals(LoginActivity.username))
					spot.setSelected(true);

				spot.refreshOverlay(lot.getZoomLevel());
			}
			
			mapView.getOverlays().add(lot);

			mapController = mapView.getController();
			mapController.setCenter(new GeoPoint(lot.getCenterLat(), lot
					.getCenterLong()));
			mapController.setZoom(lot.getZoomLevel());

			lot.InvalidateMap();
		}
	}
}

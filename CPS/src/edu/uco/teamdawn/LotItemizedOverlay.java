package edu.uco.teamdawn;

import java.util.ArrayList;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class LotItemizedOverlay extends ItemizedOverlay<SpotOverlayItem> {
	private ArrayList<SpotOverlayItem> spots;
	private int selectedIndex = -1;
	
	private Context mContext;
	private int zoomLevel;

	private int lotId;
	private String name;

	private int centerLat;
	private int centerLong;
	
	private MapView mapView;
	private boolean dirty = true;
	
	public static int selectedLotID = 17;
	public static int selectedSpotNumber = 0;

	public LotItemizedOverlay(Drawable defaultMarker, Context context, int lotId, String name,
			int centerLat, int centerLong, int zoom, MapView mv) {

		super(boundCenter(defaultMarker));
		mContext = context;
		this.mapView = mv;

		this.lotId = lotId;
		this.name = name;
		this.centerLat = centerLat;
		this.centerLong = centerLong;
		this.zoomLevel = zoom;
		this.spots = new ArrayList<SpotOverlayItem>();
		selectedLotID = lotId;
	}

	@Override
	protected SpotOverlayItem createItem(int i) {
		return spots.get(i);
	}

	@Override
	public int size() {
		return spots.size();
	}

	public void addSpot(SpotOverlayItem spot) {
		spots.add(spot);
		populate();
	}

	public SpotOverlayItem getSpot(int spotNumber) {
		return spots.get(spotNumber);
	}

	public ArrayList<SpotOverlayItem> getAllSpots() {
		return spots;
	}

	@Override
	protected boolean onTap(int index) {
		
		// don't allow tapping of unavailable spots
		if(!spots.get(index).getStatus().equals("AVAILABLE") || LoginActivity.check)
			return true;
		
		// Set all spots to not selected
		for(SpotOverlayItem spot : spots)
		{
			spot.setSelected(false);
			spot.refreshOverlay(zoomLevel);
		}
		
		// now update actual spot that was tapped
		SpotOverlayItem spot = spots.get(index);
		spot.setSelected(true);
		spot.refreshOverlay(zoomLevel);
		selectedIndex = index;
		selectedLotID = this.lotId;
		selectedSpotNumber = spot.getSpotNumber();
		
		///Toast.makeText(mContext, "You selected spot " + spot.getSpotNumber(), Toast.LENGTH_LONG).show();
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("Spot Selected");
		dialog.setMessage("You selected spot " + spot.getSpotNumber() + ".");
		dialog.setPositiveButton("OK", null);
				//+ "\nI-height: " + spot.getMarker(0).getIntrinsicHeight()
				//+ "\nI-width: " + spot.getMarker(0).getIntrinsicWidth()
				//+ "\nWidth: " + spot.getWidth()
				//+ "\nHeight: " + spot.getHeight()
				//+ "\nCenterLat:" + spot.getCenterLatString()
				//+ "\nCenterLong:" + spot.getCenterLongString());

		dialog.show();
		
		// call this to redraw since we changed color spot
		mapView.invalidate();
		
		return true;
	}
	
	public void InvalidateMap()
	{
		mapView.invalidate();
	}
	
	public SpotOverlayItem getSelectedSpot()
	{
		if(selectedIndex != -1)
			return spots.get(selectedIndex);
		return null;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (dirty || zoomLevel != mapView.getZoomLevel()) {
			dirty = false;
			zoomLevel = mapView.getZoomLevel();
			for (SpotOverlayItem spot : spots) {
				spot.refreshOverlay(zoomLevel);
			}
		}

		super.draw(canvas, mapView, false);
	}

	public int getLotId() {
		return lotId;
	}

	public String getName() {
		return name;
	}

	public int getCenterLat() {
		return centerLat;
	}

	public int getCenterLong() {
		return centerLong;
	}
	
	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
		mapView.invalidate();
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
}

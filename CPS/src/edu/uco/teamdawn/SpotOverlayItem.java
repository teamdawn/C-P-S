package edu.uco.teamdawn;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;

public class SpotOverlayItem extends OverlayItem {
	private int spotNumber;
	private String status;
	private String user;
	private String type;
	private int centerLat;
	private int centerLong;
	private int rotation;
	private int zoom;
	private int width = 10;
	private int height = 22;
	private boolean selected = false;

	public SpotOverlayItem(int spotNumber, String status, String user, String type,
			int centerLat, int centerLong, int rotation, int zoom) {
		super(new GeoPoint(centerLat, centerLong),
				Integer.toString(spotNumber), type);
		this.spotNumber = spotNumber;
		this.status = status;
		this.user = user;
		this.type = type;
		this.centerLat = centerLat;
		this.centerLong = centerLong;
		this.rotation = rotation;
		this.zoom = zoom;
	}

	public void refreshOverlay(int zoom) {
		PictureDrawable pd;
		this.zoom = zoom;
		switch (zoom) {
		case 19:
			width = 5;
			height = 11;
			pd = new PictureDrawable(this.drawSpot());
			break;
		case 20:
			width = 10;
			height = 22;
			pd = new PictureDrawable(this.drawSpot());
			break;
		case 21:
			width = 20;
			height = 44;
			pd = new PictureDrawable(this.drawSpot());
			break;
		case 22:
			width = 32;
			height = 70;
			pd = new PictureDrawable(this.drawSpot());
			break;
		default:
			//pd = new PictureDrawable(this.drawPointer());
			pd = new PictureDrawable(this.drawSpot());
			break;
		}
		pd.setBounds(-(int) Math.ceil(pd.getIntrinsicWidth() / 2),
				-(int) Math.ceil(pd.getIntrinsicHeight() / 2),
				(int) Math.ceil(pd.getIntrinsicWidth() / 2),
				(int) Math.ceil(pd.getIntrinsicHeight() / 2));
		super.setMarker(pd);
	}

	private Picture drawSpot() {
		Picture pic = new Picture();

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		if(selected)
		{
			// yellow
			paint.setARGB(255, 255, 255, 0);
		}
		else if(status.equals("AVAILABLE"))
		{
			if(type.equals("COMMUTER"))
			{
				// red
				paint.setARGB(255, 255, 0, 0);
			}
			else if(type.equals("FACULTY"))
			{
				// light blue
				paint.setARGB(255, 153, 217, 234);
			}
			else if(type.equals("FACULTY 24HR"))
			{
				// dark blue
				paint.setARGB(255, 0, 0, 255);
			}
			else if(type.equals("HOUSING"))
			{
				// green
				paint.setARGB(255, 0, 220, 0);
			}
			else if(type.equals("MULTI"))
			{
				// pale yellow
				paint.setARGB(255, 255, 255, 128);
			}
			else if(type.equals("VISITOR"))
			{
				// gray
				paint.setARGB(255, 192, 192, 192);
			}
			else
			{
				// default = black
				paint.setARGB(255, 0, 0, 0);
			}
		}
		else
		{
			// not AVAILABLE = black
			paint.setARGB(255, 0, 0, 0);
		}
		
		Canvas canvas;
		Rect rect;
		
		switch(rotation)
		{
			case 0:
				// vertical (car points north/south)
				canvas = pic.beginRecording(width, height);
				rect = new Rect(0, 0, width, height);
				break;
			case 90:
				// horizontal (car points east/west)
				canvas = pic.beginRecording(height, width);
				rect = new Rect(0, 0, height, width);
				break;
			default:
				// vertical (car points north/south)
				canvas = pic.beginRecording(width, height);
				rect = new Rect(0, 0, width, height);
				break;
		}
		
		canvas.drawRect(rect, paint);
		pic.endRecording();
		
		return pic;
	}

	private Picture drawPointer() {
		Picture pic = new Picture();

		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		paint.setStyle(Paint.Style.FILL);

		Canvas canvas = pic.beginRecording(50, 50);

		canvas.drawCircle(25, 25, 25, paint);

		pic.endRecording();

		return pic;
	}
	
	public int getSpotNumber() {
		return spotNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public int getCenterLat() {
		return centerLat;
	}
	
	public String getCenterLatString() {
		float f = centerLat;
		f /= 1000000;
		return Float.toString(f);
	}

	public int getCenterLong() {
		return centerLong;
	}

	public String getCenterLongString() {
		float f = centerLong;
		f /= 1000000;
		return Float.toString(f);
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getRotation() {
		return rotation;
	}
}

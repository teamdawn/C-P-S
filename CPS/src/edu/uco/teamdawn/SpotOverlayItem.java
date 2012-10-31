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
			pd = new PictureDrawable(this.drawPointer());
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
				// blue
				paint.setARGB(255, 0, 0, 255);
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
		
		// These 4 lines make a good Picture, but no rotation
		Canvas canvas = pic.beginRecording(width, height);
		Rect rect = new Rect(0, 0, width, height);
		canvas.drawRect(rect, paint);
		pic.endRecording();

		
		/* this is an attempt to make it rotate. doesn't work currently
		// Create rotation matrix
		Matrix rotateMatrix = new Matrix();
		rotateMatrix.setRotate(rotation, canvas.getWidth()/2, canvas.getHeight()/2);
		PictureDrawable pd = new PictureDrawable(pic);
		Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas2 = new Canvas(bitmap); 
	    pd.setBounds(0, 0, canvas2.getWidth(), canvas2.getHeight());
	    pd.draw(canvas2);
		// Draw bitmap onto canvas using matrix
		canvas.drawBitmap(bitmap, rotateMatrix, null);
		*/
		
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

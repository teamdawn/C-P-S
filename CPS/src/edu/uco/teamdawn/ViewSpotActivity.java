package edu.uco.teamdawn;

import edu.uco.dmoore35.tp.AddCourse;
import edu.uco.dmoore35.tp.CoursesActivity;
import edu.uco.dmoore35.tp.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class ViewSpotActivity extends FragmentActivity {

	TextView lot;
	TextView spot;
	Button checkInOut;
	Button cancelReservation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewspotactivity);
		
		lot = (TextView) findViewById(R.id.lotNumber);
		spot = (TextView) findViewById(R.id.spotNumber);
		
		checkInOut = (Button) findViewById(R.id.checkInOutButton);
		cancelReservation = (Button) findViewById(R.id.cancelReservationButton);
		
		//RelativeLayout rl = (RelativeLayout) findViewById(R.id.rLayout);
		//Intent intent = new Intent(ViewSpotActivity.this, LotMapActivity.class);
		//intent.putExtra("spotActivity", true);
		//startActivity(intent);
	}
	 	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	menu.add(1, 1, 0,"Add a Course");
	        getMenuInflater().inflate(R.menu.activity_courses, menu);
	        return true;
	    }

		@Override
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
			// TODO Auto-generated method stub
			switch(item.getItemId()){
			case 1:
				//add a course
				Intent addCourse = new Intent(CoursesActivity.this, AddCourse.class);
				startActivity(addCourse);
				break;
			}
			return super.onMenuItemSelected(featureId, item);
		
		}

	
	
}
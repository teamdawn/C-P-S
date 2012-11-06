package edu.uco.teamdawn;

import com.inazaruk.example.ActivityHostFragment;

import android.app.Activity;

public class LotMapFragment extends ActivityHostFragment {
    
    @Override
    protected Class<? extends Activity> getActivityClass() {
        return LotMapActivity.class;
    }
}

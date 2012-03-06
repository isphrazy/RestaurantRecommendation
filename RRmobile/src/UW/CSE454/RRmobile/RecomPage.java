package UW.CSE454.RRmobile;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;


public class RecomPage extends MapActivity{

	private Button listB;
	private Button mapB;
	private boolean mode;//0 for list, 1 for map
	
	private MapView map;
	
	/**
	 * start the activity
	 */
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.recom_page_layout);
		
        initiateVars();
	}
	
	private void initiateVars() {
		listB = (Button) findViewById(R.id.list_b);
		mapB = (Button) findViewById(R.id.map_b);
		mode = false;
		map = new MapView(this, getResources().getString(R.string.map_api));
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void changeMode(View view){
		
	}

}

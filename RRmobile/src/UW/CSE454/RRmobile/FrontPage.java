package UW.CSE454.RRmobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class FrontPage extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page_layout);
    }
    
    public void search(View view){
    	
    	Log.d("search", "search");
    }
}
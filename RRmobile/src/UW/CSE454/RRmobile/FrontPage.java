package UW.CSE454.RRmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FrontPage extends Activity {
	
	private EditText et;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.front_page_layout);
        
        getLayouts();
    }
    
    private void getLayouts(){
    	et = (EditText) findViewById(R.id.search_bar);
    	//if the user has already loged in, then login options could be removed
    	Settings settings = Settings.getInstance(this);
    	if(settings.hasAt()){
    		findViewById(R.id.member_layouts).setVisibility(View.INVISIBLE);
    		TextView username = (TextView) findViewById(R.id.username_t);
    		username.setText("Welcome: " + settings.getUsername());
    		username.setVisibility(View.VISIBLE);
    	}
    	findViewById(R.id.user_profile_b).setVisibility(View.VISIBLE);
    	
    }
    

	public void onClick(View view){
		boolean flag = true;
		Intent intent = new Intent();
		Class c = null;
		
    	switch (view.getId()){
    	case R.id.search_b:
    		if(et.getText().toString().length() < 1){
    			flag = false;
    			Toast.makeText(this, "restaurant should not be empty", Toast.LENGTH_SHORT);
    		}
    		c = SearchResultPage.class;
    		intent.putExtra("keyword", et.getText().toString());
    		break;
    	case R.id.register_b:
    		c = RegisterPage.class;
    		break;
    	case R.id.login_b:
    		intent.putExtra("nextactivity", "UW.CSE454.RRmobile.FrontPage");
    		c = LoginPage.class;
        	intent.setClass(this, c);
        	startActivity(intent);
        	finish();
    		break;
    	case R.id.user_profile_b:
    		break;
    	}
    	if(flag){
    		intent.setClass(this, c);
    		startActivity(intent);
    	}
    	
    }
}
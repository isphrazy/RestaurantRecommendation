package UW.CSE454.RRmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class FrontPage extends Activity {
	
//	private Button searchButton;
//	private Button registerButton;
//	private Button LoginButton;
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
    	if(Settings.getInstance(this).hasAt())
    		findViewById(R.id.member_layouts).setVisibility(View.INVISIBLE);
    	
    }
    
//    private void initiateVar() {
//    	searchButton = (Button) findViewById(R.id.search_b);
//    	registerButton = (Button) findViewById(R.id.register_b);
//    	LoginButton = (Button) findViewById(R.id.login_b);
//	}

	public void onClick(View view){
		
		Intent intent = new Intent();
		Class c = null;
		
    	switch (view.getId()){
    	case R.id.search_b:
    		c = SearchResultPage.class;
    		intent.putExtra("keyword", et.getText().toString());
    		break;
    	case R.id.register_b:
    		c = RegisterPage.class;
    		break;
    	case R.id.login_b:
    		c = LoginPage.class;
    		break;
    	
    	}
    	intent.setClass(this, c);
    	startActivity(intent);
    	
    }
}
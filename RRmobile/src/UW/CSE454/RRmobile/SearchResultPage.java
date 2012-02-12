package UW.CSE454.RRmobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class SearchResultPage extends Activity {
	
	private String keyword;
	
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.search_result_layout);
        
        initiateVar();
        
        connect();
        
	}

	private void connect() {
		String query = "http://www.kurlin.com/454/api/api_search.php?restaurant_name=" + keyword;
	}

	private void initiateVar() {
		keyword = getIntent().getStringExtra("keyword");
	}
}

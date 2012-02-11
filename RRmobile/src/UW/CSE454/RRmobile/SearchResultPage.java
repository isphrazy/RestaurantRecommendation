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
        
	}

	private void initiateVar() {
		keyword = getIntent().getStringExtra("keyword");
		Log.e("keywords", keyword);
	}
}

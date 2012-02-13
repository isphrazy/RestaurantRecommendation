package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchResultPage extends Activity {
	
	private String keyword;
	private ProgressBar pb;
	private ListView lv;
	private RestaurantsArrayAdapter adapter;
	private TextView messageEt;
	
	private List<Restaurant> list;
	
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.search_result_layout);
        
        initiateVar();
        
        connect();
        
	}

	private void connect() {
		new SearchAsync().execute();
	}

	private void initiateVar() {
		keyword = getIntent().getStringExtra("keyword");
		pb = (ProgressBar) findViewById(R.id.progressBar);
		lv = (ListView) findViewById(R.id.restaurant_l);
		messageEt = (TextView) findViewById(R.id.message);
		
		adapter = new RestaurantsArrayAdapter(this, list);
		lv.setAdapter(adapter);
		
		
	}
	
	//fetch response from server
	private class SearchAsync extends AsyncTask<Void, Void, Void>{

		private String response;
		
		@Override
		protected Void doInBackground(Void... params) {
			String query = "http://www.kurlin.com/454/api/api_search.php?restaurant_name=" + keyword;
			
			HttpClient client = new DefaultHttpClient();
			HttpResponse hr = null;
			try {
				hr = client.execute(new HttpGet(query));
				HttpEntity entity = hr.getEntity();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
				response = br.readLine();
				publishProgress();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			Log.e("response", response);
			return null;
		}
		
		protected void onProgressUpdate(Void... v){
			JSONArray restaurants = null;
			String first = null;
			try {
				restaurants = new JSONArray(response);
				first = restaurants.getString(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(first.equals(-1)){//no restaurant found
				messageEt.setText("Sorry, we could not find restaurant " + keyword + " ,please try again");
			}else if(first.equals(0)){
				messageEt.setText("Do you mean:");
			}else{
				messageEt.setText("You may also like");
				adapter.notifyDataSetChanged();
				pb.setVisibility(View.INVISIBLE);
			}
			
		}
		
	}
	
	private class RestaurantsArrayAdapter extends ArrayAdapter<Restaurant>{
		
		private Context context;
		private List<Restaurant> list;
		
		public RestaurantsArrayAdapter(Context context, List<Restaurant> list) {
			super(context, R.layout.search_result_entry, list);
			this.context = context;
			this.list = list;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			Restaurant restaurant = list.get(position);
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View rowView = inflater.inflate(R.layout.search_result_entry, null, true);
			((TextView) findViewById(R.id.result_b_name)).setText(restaurant.businessName);
			((TextView) findViewById(R.id.result_address)).setText(restaurant.address);
			
			return rowView;
		}
	}
}

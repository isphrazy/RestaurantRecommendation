package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultPage extends Activity {
	
	private String keyword;
	private ProgressBar pb;
	private ListView lv;
	private RestaurantsArrayAdapter adapter;
	private TextView messageEt;
	private int entry;
	private String sure;
	
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
		
		list = new ArrayList<Restaurant>();
		sure = getIntent().getStringExtra("sure");
		if(sure == null) sure = "";
		
		
	}
	
	//fetch response from server
	private class SearchAsync extends AsyncTask<Void, Void, Void>{

		private String response = "";
		
		@Override
		protected Void doInBackground(Void... params) {
			String query = "http://kurlin.com/454/api_search.php?restaurant_name=" + keyword + sure;
			query = query.replace(" ", "%20");
			Log.e("qquery: ", query);
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
			return null;
		}
		
		protected void onProgressUpdate(Void... v){
			JSONArray resp = null;
			String first = null;
			Log.e("RESP: ", response);
			try {
				resp = new JSONArray(response);
				first = resp.getString(0);
				if(first.equals("-1")){//no restaurant found
					messageEt.setText("Sorry, we could not find restaurant " + keyword + " ,please try again");
				}else{
					if(first.equals("0")){
						messageEt.setText("Do you mean:");
						entry = R.layout.search_result_entry;
						JSONObject restaurants = resp.getJSONObject(1);
						JSONArray rNames  = restaurants.names();
						for(int i = 0; i < rNames.length(); i++){
							String id = rNames.getString(i);
							JSONObject restaurant = restaurants.getJSONObject(id);
							Restaurant r = new Restaurant();
							r.businessName = restaurant.getString("Business Name");
							r.address = restaurant.getString("Address");
							r.id = id;
							list.add(r);
						}
					}else{
						messageEt.setText("You may also like:");
						for(int i = 0; i < resp.length(); i++){
							JSONObject restaurant = resp.getJSONObject(i);
							Restaurant r = new Restaurant();
							r.businessName = restaurant.getString("business_name");
							r.address = restaurant.getString("address");
							r.id = restaurant.getString("name");
							r.priceLevel = restaurant.getString("price");
							
							JSONArray jCategory = restaurant.getJSONArray("category");
							String[] category = new String[jCategory.length()];
							for(int j = 0; j < jCategory.length(); j++){
								category[j] = jCategory.getString(j);
							}
							r.category = category;
							
							JSONArray jReviews = restaurant.getJSONArray("reviews");
							double[] reviews = new double[jReviews.length()];
							for(int j = 0; j < jReviews.length(); j++){
								reviews[j] = jReviews.getDouble(j);
							}
							r.reviews = reviews;
							list.add(r);
						}
						entry = R.layout.relevant_restaurants_entry;
					}
					adapter = new RestaurantsArrayAdapter(SearchResultPage.this, list);
					lv.setAdapter(adapter);
				}
				pb.setVisibility(View.INVISIBLE);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	//array adapter for restaurant listview
	private class RestaurantsArrayAdapter extends ArrayAdapter<Restaurant>{
		
		private Context context;
		private List<Restaurant> list;
		
		public RestaurantsArrayAdapter(Context context, List<Restaurant> list) {
			super(context, entry, list);
			this.context = context;
			this.list = list;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			Restaurant r = list.get(position);
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View rowView = inflater.inflate(entry, null, true);
			if(entry != R.layout.relevant_restaurants_entry){
				((TextView) rowView.findViewById(R.id.result_b_name)).setText(r.businessName);
				((TextView) rowView.findViewById(R.id.result_address)).setText(r.address);
				lv.setOnItemClickListener(new RestaurantsClickListener());
			}else{
				((TextView) rowView.findViewById(R.id.restaurant_name)).setText(r.businessName);
				((TextView) rowView.findViewById(R.id.address)).setText(r.address);
				((TextView) rowView.findViewById(R.id.price)).setText("Price level: " + r.priceLevel);
				String category = "";
				for(int i = 0; i < r.category.length; i ++)
					category +=  r.category[i] + " ";
				
				((TextView) rowView.findViewById(R.id.category)).setText("Category: " + category);
				
				String review = "Reviews: Food:" + (new DecimalFormat("0.0").format(r.reviews[0]))
										+ " Service: " + (new DecimalFormat("0.0").format(r.reviews[1]))
										+ " Decor: " + (new DecimalFormat("0.0").format(r.reviews[2]));
				((TextView) rowView.findViewById(R.id.review)).setText(review);
				lv.setOnItemClickListener(new RelevantClickListener());
			}
			
			return rowView;
		}
	}
	
	
	private class RestaurantsClickListener implements OnItemClickListener{
		
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			Restaurant r = list.get(position);
			sure = "&sure=yes";
			Intent i = new Intent();
			i.setClass(SearchResultPage.this, SearchResultPage.class);
			i.putExtra("sure", sure);
			i.putExtra("keyword", r.id);
			startActivity(i);
		}
	}
	
	private class RelevantClickListener implements OnItemClickListener{
		
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			
		}
	}

}

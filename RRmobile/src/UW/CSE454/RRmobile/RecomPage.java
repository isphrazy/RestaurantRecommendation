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
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class RecomPage extends MapActivity{
	
	private String PD_TITLE = "Loading Data";
	private String PD_MESSAGE = "Please wait...";

	private Button listB;
	private Button mapB;
	private ListView lv;
	
	private MapView map;
	private ProgressDialog pd;
	
	private String aT;
	private List<Restaurant> list;
	private RestaurantsArrayAdapter adapter;
	private boolean mode;//0 for list, 1 for map
	private RClickListener rClickListener;
	
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
		aT = Settings.getInstance(this).getAt();
		list = new ArrayList<Restaurant>();
		rClickListener = new RClickListener();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * 
	 * @param view listB or mapB
	 */
	public void changeMode(View view){
		if(view == listB && mode){//click on listB while currently is in map view
			listB.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabb));
			mapB.setBackgroundDrawable(getResources().getDrawable(R.drawable.taba));
			mode = false;
		}else if(view == mapB && !mode){//click on mapB while currently is in list view
			listB.setBackgroundDrawable(getResources().getDrawable(R.drawable.taba));
			mapB.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabb));
			mode = true;
		}
	}

	private class FetchData extends AsyncTask<Void, Void, Void>{
		
		private String response;
		
		protected void onPreExecute (){
				pd = ProgressDialog.show(RecomPage.this, PD_TITLE, PD_MESSAGE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String query = "http://kurlin.com/454/api/api_member_recom.php?access_token=" + aT;
			query = query.replace(" ", "%20");
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
			pd.dismiss();
			try {
				JSONObject rs = new JSONObject(response);
				JSONArray ja = rs.names();
				for(int i = 0; i < ja.length(); i++){
					Restaurant r = new Restaurant();
					String rId = ja.getString(i);
					JSONObject rInfo = rs.getJSONObject(rId);
					r.id = rId;
					r.businessName = rInfo.getString("Business Name");
					r.address = rInfo.getString("Address");
					r.lon = rInfo.getDouble("Longitude");
					r.lat = rInfo.getDouble("Latitude");
					list.add(r);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapter = new RestaurantsArrayAdapter(RecomPage.this, list);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(rClickListener);
			setUpMap();
		}
	}
	
	//array adapter for restaurants listview
	private class RestaurantsArrayAdapter extends ArrayAdapter<Restaurant>{
		
		private Context context;
		private List<Restaurant> list;
		
		public RestaurantsArrayAdapter(Context context, List<Restaurant> list) {
			super(context, R.layout.relevant_restaurants_entry, list);
			this.list = list;
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			Restaurant r = list.get(position);
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View rowView = inflater.inflate(R.layout.relevant_restaurants_entry, null, true);
			
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
			
			rowView.findViewById(R.id.pic).setTag(position);
			
			lv.setOnItemClickListener(rClickListener);
			return rowView;
		}
	}
	
	// when relevant restaurant is clicked
	private class RClickListener implements OnItemClickListener{
		
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			Intent i = new Intent();
			i.setClass(RecomPage.this, DetailPage.class);
			i.putExtra("name", list.get(position).id);
			startActivity(i);
		}
	}

	public void setUpMap() {
		map = new MapView(this, getResources().getString(R.string.map_api));
		List<Overlay> mapOverlays = map.getOverlays();
		RRItemizedOverlay itemizedoverlay = new RRItemizedOverlay(
																RecomPage.this.getResources().getDrawable(R.drawable.map_mark), 
																RecomPage.this);
		int leftMax = (int) (180 * 1e6);
		int rightMax = (int) (-180 * 1e6);
		int upMax = (int) (-90 * 1e6);
		int downMax = (int) (90 * 1e6);
		for(int i = 0; i < list.size(); i++){
			Restaurant r = list.get(i);
			
			int lat = (int)(r.lat * 1e6);
			int lon = (int)(r.lon * 1e6);
			leftMax = Math.min(lon, leftMax);
			rightMax = Math.max(lon, rightMax);
			upMax = Math.max(lat, upMax);
			downMax = Math.min(lat, downMax);
			
			GeoPoint point = new GeoPoint(lat, lon);
			OverlayItem overlayitem = new OverlayItem(point, r.businessName, r.address);
			itemizedoverlay.addOverlay(overlayitem);
		}
		mapOverlays.add(itemizedoverlay);
		MapController mc = map.getController();
		mc.setCenter(new GeoPoint((upMax + downMax) / 2, (leftMax + rightMax) / 2));
//		mc.zoomToSpan(itemizedoverlay.getLatSpanE6(), itemizedoverlay.getLonSpanE6());
		mc.setZoom((upMax - downMax) / (int) (90 * 1e6));
	}
}

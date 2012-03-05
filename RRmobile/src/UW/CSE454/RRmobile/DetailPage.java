package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This page shows the detail about given restaurant
 * @author Pingyang He
 *
 */
public class DetailPage extends MapActivity{
	
	private String PD_TITLE = "Loading Data";
	private String PD_MESSAGE = "Please wait...";
	
	private String rId;
	private ProgressDialog pd;
	private TextView phoneNum;
	private RMapView map;
	
	private double mLat;
	private double mLon;
	
	/**
	 * start activity
	 */
	public void onCreate(Bundle savedInstanceState){
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail_layout);
		
		initiateVar();
		
		new FetchDetailAsync().execute();
	}
	
	//initiate variables
	private void initiateVar() {
		rId = getIntent().getStringExtra("name");
		pd = ProgressDialog.show(DetailPage.this, PD_TITLE, PD_MESSAGE);
		map = (RMapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		
	}

	//fetching data from background
	private class FetchDetailAsync extends AsyncTask<Void, Void, Void>{
		
		private String response;
		
		@Override
		//fetch data from background
		protected Void doInBackground(Void... params) {
			String query = "http://kurlin.com/454/api/api_detail.php?name=" + rId;
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
		
		//update the page
		protected void onProgressUpdate(Void... v){
			pd.dismiss();
			try {
				JSONObject r = new JSONObject(response);
				String bName = r.getString("Business Name").replace("&amp;", "and");
				((TextView) findViewById(R.id.b_name)).setText(bName);
				((TextView) findViewById(R.id.price)).setText("Price Level: " + r.getString("Price Range"));
				String address = r.getString("Address");
				((TextView) findViewById(R.id.address)).setText(address);
				
				((TextView) findViewById(R.id.category)).setText(r.getString("Category"));
				
				JSONArray ja = r.getJSONArray("review");
				DecimalFormat df = new DecimalFormat("0.0");
				((TextView) findViewById(R.id.f_review)).setText("Food: " + df.format(ja.get(0)));
				((TextView) findViewById(R.id.s_review)).setText("Service: " + df.format(ja.get(1)));
				((TextView) findViewById(R.id.d_review)).setText("Decor: " + df.format(ja.get(2)));
				
				int lat = (int) (r.getDouble("Latitude") * 1e6);
				int lon = (int) (r.getDouble("Longitude") * 1e6);
				phoneNum = ((TextView) findViewById(R.id.phone));
				if(phoneNum == null) ((LinearLayout) findViewById(R.id.phone_ll)).setVisibility(View.INVISIBLE);
				else phoneNum.setText(r.getString("Phone number"));
				
				//set google map
				List<Overlay> mapOverlays = map.getOverlays();
				RRItemizedOverlay itemizedoverlay = new RRItemizedOverlay(
																		DetailPage.this.getResources().getDrawable(R.drawable.map_mark), 
																		DetailPage.this);
				GeoPoint point = new GeoPoint(lat, lon);
				OverlayItem overlayitem = new OverlayItem(point, bName, address);
				itemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedoverlay);
				MapController mc = map.getController();
				mc.setCenter(point);
//				mc.zoomToSpan(itemizedoverlay.getLatSpanE6(), itemizedoverlay.getLonSpanE6());
				mc.setZoom(15);
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * make a phone call when the view is pressed
	 * @param v image view of call
	 */
	public void call(View v) {
		Log.e("call", phoneNum.getText().toString());
	    try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:" + phoneNum.getText().toString()));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	        Log.e("helloandroid dialing example", "Call failed", e);
	    }
	}
	
	public void onClick(View view){
		//go relevant restaurants list
//		Intent i = new Intent();
//		i.setClass(this, RelevantRestaurantList.class);
//		i.putExtra("id", rId);
//		startActivity(i);
		Intent i = new Intent();
		i.setClass(this, SearchResultPage.class);
		i.putExtra("sure", "&sure=yes");
		i.putExtra("keyword", rId);
		startActivity(i);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}

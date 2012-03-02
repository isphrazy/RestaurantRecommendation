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
	private MapView map;
	
	private List<Overlay> mapOverlays;
	private GeoPoint point;
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
		map = (MapView) findViewById(R.id.mapview);
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
				((TextView) findViewById(R.id.b_name)).setText(r.getString("Business Name").replace("&amp;", "and"));
				((TextView) findViewById(R.id.price)).setText("Price Level: " + r.getString("Price Range"));
				((TextView) findViewById(R.id.address)).setText(r.getString("Address"));
				
				((TextView) findViewById(R.id.category)).setText(r.getString("Category"));
				
				JSONArray ja = r.getJSONArray("review");
				DecimalFormat df = new DecimalFormat("0.0");
				((TextView) findViewById(R.id.f_review)).setText("Food: " + df.format(ja.get(0)));
				((TextView) findViewById(R.id.s_review)).setText("Service: " + df.format(ja.get(1)));
				((TextView) findViewById(R.id.d_review)).setText("Decor: " + df.format(ja.get(2)));
				
				phoneNum = ((TextView) findViewById(R.id.phone));
				if(phoneNum == null) ((LinearLayout) findViewById(R.id.phone_ll)).setVisibility(View.INVISIBLE);
				else phoneNum.setText(r.getString("Phone number"));
				
				mapOverlays = map.getOverlays();
				RRItemizedOverlay itemizedoverlay = new RRItemizedOverlay(
																		DetailPage.this.getResources().getDrawable(R.drawable.map_mark), 
																		DetailPage.this);
				point = new GeoPoint(19240000,-99120000);
				OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
				itemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedoverlay);
				
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
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}

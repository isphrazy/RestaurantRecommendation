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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private Button likeB;
	
	private double mLat;
	private double mLon;
	private Settings settings;
	private String accessToken;
	private String likedRestaurants;
	
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
//		pd = ProgressDialog.show(DetailPage.this, PD_TITLE, PD_MESSAGE);
		map = (RMapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		likeB = (Button) findViewById(R.id.like_b);
		settings = Settings.getInstance(DetailPage.this);
		accessToken = settings.getAt();
	}

	//fetching data from background
	private class FetchDetailAsync extends AsyncTask<Void, Void, Void>{
		
		private String response;
		private JSONObject r;
		private double[] location;
		
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
				Log.e("response: ", response);
				if(accessToken != null){//already logged in
					HttpEntity userEntity = new DefaultHttpClient().execute(
							new HttpGet("http://kurlin.com/454/api/api_liked_r.php?access_token=" + accessToken)).getEntity();
					BufferedReader uBr = new BufferedReader(
							new InputStreamReader(userEntity.getContent()));
					likedRestaurants = uBr.readLine();
//					Log.e("uBr: ", likedRestaurants);
				}
				
//				location = getMyLocation();
//				if(location[0] != 0 && location[1] != 0){
//					Log.e("got it", "yeaaaaah");
//					try {
//						r = new JSONObject(response);
//						getUrl(location[1], location[0], r.getDouble("Latitude"), r.getDouble("Longitude"));
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
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
//			pd.dismiss();
			try {
				Log.e("onProgress", "in");
				r = new JSONObject(response);
				String bName = "";
				try{
					bName = r.getString("Business Name").replace("&amp;", "and");
					((TextView) findViewById(R.id.b_name)).setText(bName);
				}catch (JSONException e){
					e.printStackTrace();
				}
				
				try{
					((TextView) findViewById(R.id.price)).setText("Price Level: " + r.getString("Price Range"));
				}catch (JSONException e){
					e.printStackTrace();
				}
				
				String address = "";
				try{
					address = r.getString("Address");
					((TextView) findViewById(R.id.address)).setText(address);
				}catch (JSONException e){
					e.printStackTrace();
				}
				
				try{
					((TextView) findViewById(R.id.category)).setText(r.getString("Category"));
				}catch (JSONException e){
					e.printStackTrace();
				}
				
				try{
					JSONArray ja = r.getJSONArray("review");
					DecimalFormat df = new DecimalFormat("0.0");
					((TextView) findViewById(R.id.f_review)).setText("Food: " + df.format(ja.get(0)));
					((TextView) findViewById(R.id.s_review)).setText("Service: " + df.format(ja.get(1)));
					((TextView) findViewById(R.id.d_review)).setText("Decor: " + df.format(ja.get(2)));
				}catch (JSONException e){
					e.printStackTrace();
				}
				

				
				try{
					phoneNum = ((TextView) findViewById(R.id.phone));
					if(phoneNum == null) ((LinearLayout) findViewById(R.id.phone_ll)).setVisibility(View.INVISIBLE);
					else phoneNum.setText(r.getString("Phone number"));
				}catch (JSONException e){
					e.printStackTrace();
				}
				
				
				
				//set google map
				try{
					int lat = (int) (r.getDouble("Latitude") * 1e6);
					int lon = (int) (r.getDouble("Longitude") * 1e6);
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
					mc.setZoom(13);
				}catch (JSONException e){
					e.printStackTrace();
				}
				
				
				if(likedRestaurants != null && likedRestaurants.length() > 2){//liked some restaurants
					JSONArray rJa = new JSONArray(likedRestaurants);
					for(int i = 0; i < rJa.length(); i++){
						if(rJa.optString(i).equals(rId)){
							likeB.setEnabled(false);
							likeB.setText("Liked");
							break;
						}
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private double[] getMyLocation(){
		
//		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
//		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		return new double[]{location.getLongitude(), location.getLatitude()};
		CurrentLocationFinder locationFinder = new CurrentLocationFinder(DetailPage.this);
		locationFinder.getLocation();
		int count = 10;
		try {
			while(locationFinder.latitude == 0 || locationFinder.longitude == 0 && count > 0){
				Thread.sleep(200);
				count--;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new double[]{locationFinder.latitude, locationFinder.longitude};
		
//		LocationListener locationListener = new LocationListener() {
//			
//			Location l;
//			
//		    public void onLocationChanged(Location location) {
//		    	loca = location;
//		    }
//
//			@Override
//			public void onProviderDisabled(String provider) {
//			}
//
//			@Override
//			public void onProviderEnabled(String provider) {
//			}
//
//			@Override
//			public void onStatusChanged(String provider, int status,
//					Bundle extras) {
//			}
//		};
//
//		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
	}
	
	/**
	 * make a phone call when the view is pressed
	 * @param v image view of call
	 */
	public void call(View v) {
//		Log.e("call", phoneNum.getText().toString());
	    try {
	    	String number = "";
	    	if(phoneNum != null && (number = phoneNum.getText().toString()).length() != 0){
	    		Intent callIntent = new Intent(Intent.ACTION_CALL);
	    		callIntent.setData(Uri.parse("tel:" + number));
	    		startActivity(callIntent);
	    	}
	    } catch (ActivityNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public void onClick(View view){
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
	
	public void likeClick(View view){
		if(accessToken != null){//have already logged in
			likeB.setText("Liked");
			new IsLikeAsyncTask().execute(new String[]{rId, "1", Settings.getInstance(this).getAt()});
			likeB.setEnabled(false);
		}else{
			Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
			try {
				Thread.sleep(500);
				Intent i = new Intent();
				i.setClass(this, LoginPage.class);
				i.putExtra("return", "1");
				startActivityForResult(i, 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	//make sure that the login status is correct
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(settings.hasAt()){
			new IsLikeAsyncTask().execute(new String[]{rId, "1", Settings.getInstance(this).getAt()});
			likeB.setEnabled(false);
			likeB.setText("Liked");
			accessToken = settings.getAt();
		}
	}
	
	 public static String getUrl(double fromLat, double fromLon,
			   double toLat, double toLon) {// connect to map web service
			  StringBuffer urlString = new StringBuffer();
			  urlString.append("http://maps.google.com/maps?f=d&hl=en");
			  urlString.append("&saddr=");// from
			  urlString.append(Double.toString(fromLat));
			  urlString.append(",");
			  urlString.append(Double.toString(fromLon));
			  urlString.append("&daddr=");// to
			  urlString.append(Double.toString(toLat));
			  urlString.append(",");
			  urlString.append(Double.toString(toLon));
			  urlString.append("&ie=UTF8&0&om=0&output=kml");
			  return urlString.toString();
	}
	 
	private class Point {
		String mName;
		String mDescription;
		String mIconUrl;
		double mLatitude;
		double mLongitude;
	}
	
	
	private class Road {
		public String mName;
		public String mDescription;
		public int mColor;
		public int mWidth;
		public double[][] mRoute = new double[][] {};
		public Point[] mPoints = new Point[] {};
	}
	
	
}

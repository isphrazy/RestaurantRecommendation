package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class ProfilePage extends Activity{

	private String PD_TITLE = "Loading Data";
	private String PD_MESSAGE = "Please wait...";
	
	private String aT;
	private Settings settings;
	private ListView lv;
	private ProgressDialog pd;
	private List<Restaurant> list;
	
	private RestaurantsArrayAdapter adapter;
	private RClickListener rClickListener;
	private boolean editingMode;
	
	/**
	 * start the activity
	 */
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.profile_layout);
		initiateVar();
		
		new FetchDetailAsync().execute();
		
	}
	
	//initiate varaibles
	private void initiateVar() {
		rClickListener = new RClickListener();
		settings = Settings.getInstance(ProfilePage.this);
		aT = settings.getAt();
		((TextView) findViewById(R.id.title)).setText(settings.getUsername());
		lv = (ListView) findViewById(R.id.r_lv);
		list = new ArrayList<Restaurant>();
		editingMode = false;
	}

	//fetch the user's favoriate restaurant list
	private class FetchDetailAsync extends AsyncTask<Void, Void, Void>{
		
		private String response;
		
		protected void onPreExecute (){
			pd = ProgressDialog.show(ProfilePage.this, PD_TITLE, PD_MESSAGE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String query = "http://kurlin.com/454/api/api_profile.php?access_token=" + aT;
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
//				Log.e("jid: ", ja.toString());
				
				for(int i = 0; i < ja.length(); i++){
					Restaurant r = new Restaurant();
					String rId = ja.getString(i);
					JSONObject rInfo = rs.getJSONObject(rId);
					r.id = rId;
					r.businessName = rInfo.getString("Business Name");
					r.address = rInfo.getString("Address");
					list.add(r);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapter = new RestaurantsArrayAdapter(ProfilePage.this, list);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(rClickListener);
		}
	}
	
	//array adapter for restaurants listview
	private class RestaurantsArrayAdapter extends ArrayAdapter<Restaurant>{
		
		private Context context;
		private List<Restaurant> list;
		
		public RestaurantsArrayAdapter(Context context, List<Restaurant> list) {
			super(context, R.layout.profile_entry, list);
			this.list = list;
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			Restaurant r = list.get(position);
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View rowView = inflater.inflate(R.layout.profile_entry, null, true);
			((TextView) rowView.findViewById(R.id.result_b_name)).setText(r.businessName);
			((TextView) rowView.findViewById(R.id.result_address)).setText(r.address);
//			if(rowView.findViewById(R.id.remove_pic) == null) Log.e("getView", "null");
			if(editingMode){
				ImageView rIv = (ImageView) rowView.findViewById(R.id.remove_pic);
				rIv.setTag(position);
				rIv.setVisibility(View.VISIBLE);
			}
			return rowView;
		}
	}
	
	/**
	 * remove the given restaurant
	 * @param view the delete button
	 */
	public void remove(View view){
		int position = (Integer)view.getTag();
		Restaurant r = list.get(position);
		new IsLikeAsyncTask().execute(new String[]{r.id, "0", Settings.getInstance(this).getAt()});
		((ImageView)view).setImageResource(R.drawable.liked);
		list.remove(position);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * show the remove buttons for each restaurant
	 * @param view editing button
	 */
	public void editClick(View view){
		editingMode = !editingMode;
		((Button) findViewById(R.id.edit_b)).setText(editingMode ? "Done" : "Edit");
		lv.setAdapter(adapter);
	}
	
	// when relevant restaurant is clicked
	private class RClickListener implements OnItemClickListener{
		
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			Intent i = new Intent();
			i.setClass(ProfilePage.this, DetailPage.class);
			i.putExtra("name", list.get(position).id);
			startActivity(i);
		}
	}
}

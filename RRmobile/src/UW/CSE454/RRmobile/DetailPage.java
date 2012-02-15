package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailPage extends Activity{
	
	private String PD_TITLE = "Loading Data";
	private String PD_MESSAGE = "Please wait...";
	
	private String rId;
	private ProgressDialog pd;
	private TextView phoneNum;
	
	public void onCreate(Bundle savedInstanceState){
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail_layout);
		
		initiateVar();
		
		new FetchDetailAsync().execute();
	}
	
	private void initiateVar() {
		rId = getIntent().getStringExtra("name");
		pd = ProgressDialog.show(DetailPage.this, PD_TITLE, PD_MESSAGE);
	}

	private class FetchDetailAsync extends AsyncTask<Void, Void, Void>{
		
		private String response;
		
		@Override
		protected Void doInBackground(Void... params) {
			String query = "http://kurlin.com/454/api_detail.php?name=" + rId;
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
				JSONObject r = new JSONObject(response);
				((TextView) findViewById(R.id.b_name)).setText(r.getString("Business Name"));
				((TextView) findViewById(R.id.price)).setText("Price Level: " + r.getString("Price Range"));
				((TextView) findViewById(R.id.address)).setText(r.getString("Address"));
				
				((TextView) findViewById(R.id.category)).setText("Category: " + r.getString("Category"));
				
				JSONArray ja = r.getJSONArray("review");
				DecimalFormat df = new DecimalFormat("0.0");
				String review = "Food: " + (df.format(ja.get(0)))
									+ "\nService: " + (df.format(ja.get(1)))
									+ "\nDecor: " + (df.format(ja.get(2)));
//				((TextView) findViewById(R.id.f_review)).setText(df.format(ja.get(0));
				
				phoneNum = ((TextView) findViewById(R.id.phone));
				if(phoneNum == null) ((LinearLayout) findViewById(R.id.phone_ll)).setVisibility(View.INVISIBLE);
				else phoneNum.setText(r.getString("Phone number"));

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	
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
}

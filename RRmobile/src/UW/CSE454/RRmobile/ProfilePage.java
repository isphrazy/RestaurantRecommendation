package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

public class ProfilePage extends Activity{
	
	private String aT;
	private Settings settings;
	
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		initiateVar();
		
	}
	
	private void initiateVar() {
		settings = Settings.getInstance(ProfilePage.this);
		aT = settings.getAt();
	}

	private class FetchDetailAsync extends AsyncTask<Void, Void, Void>{
		
		private String response;
		
		@Override
		protected Void doInBackground(Void... params) {
			String query = "http://kurlin.com/454/api_detail.php?access_token=" + aT;
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
	}
		
}

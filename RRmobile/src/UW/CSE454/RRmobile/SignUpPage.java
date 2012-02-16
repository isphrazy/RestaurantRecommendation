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
import android.util.Log;
import android.view.Window;
import android.widget.EditText;

public class SignUpPage extends Activity{
	
	private final String USER_N_Q = "username";
	private final String PASSWORD_Q = "password";
	private final String PASSWROD2_Q = "password2";
	private final String AGREE = "agree";
	private final String EMAIL = "email";
	
	private EditText userEt;
	private EditText passwordEt;
	private EditText password2Et;
	private EditText agreeEt;
	private EditText emailEt;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.signup_layout);
        
    }
    
	//fetch response from server
	private class LoginAsync extends AsyncTask<Void, Void, Void>{
		
		private String response;
		@Override
		protected Void doInBackground(Void... arg0) {
			String query = "http://kurlin.com/454/api_signup.php?"
					+ USER_N_Q + "=" + arg0[0] + "&"
					+ PASSWORD_Q + "=" + arg0[1];
			Log.e("login", query);
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

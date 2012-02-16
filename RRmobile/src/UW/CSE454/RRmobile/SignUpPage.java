package UW.CSE454.RRmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * user sign up page
 * @author Pingyang
 *
 */
public class SignUpPage extends Activity{
	
	private String PD_TITLE = "Loading Data";
	private String PD_MESSAGE = "Please wait...";
	
	private final String USER_N_Q = "username";
	private final String PASSWORD_Q = "password";
	private final String PASSWROD2_Q = "password2";
	private final String AGREE = "agree";
	private final String EMAIL = "email";
	
	private EditText userEt;
	private EditText passwordEt;
	private EditText password2Et;
	private EditText emailEt;
	private ProgressDialog pd;
	
	private Class nextActivity;
	
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.signup_layout);
        
        initiateVar();
    }
    
    // initiate varaibles
	private void initiateVar() {
		userEt = (EditText) findViewById(R.id.user_n_et);
		passwordEt = (EditText) findViewById(R.id.password_et);
		password2Et = (EditText) findViewById(R.id.password2_et);
		emailEt = (EditText) findViewById(R.id.email_et);
		
	}
	
	private String username;
	private String pw;
	private String pw2;
	private String email;
	/**
	 * signup
	 * @param view signup button
	 */
	public void onClick(View view){
		String errorMessage = "";
		
		boolean valid = true;
		username = userEt.getText().toString().trim();
		pw = passwordEt.getText().toString().trim();
		pw2 = password2Et.getText().toString().trim();
		email = emailEt.getText().toString().trim();
		
		if(username.length() < 3){ 
			errorMessage += "username should not be empty";
			valid = false;
		}
		if(pw.length() < 6){
			errorMessage += "\npassword should more than 6 chars";
			valid = false;
		}
		if(!pw2.equals(pw)){
			errorMessage += "\n2 passwrods are not the same";
			valid = false;
		}
		if(valid)
			new SignUpAsync().execute();
		else
			Toast.makeText(SignUpPage.this, errorMessage, Toast.LENGTH_SHORT).show();
	}

	//fetch response from server
	private class SignUpAsync extends AsyncTask<Void, Void, Void>{
		
		private String response = "";
		
		protected void onPreExecute (){
			pd = ProgressDialog.show(SignUpPage.this, PD_TITLE, PD_MESSAGE);

		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			String query = "http://kurlin.com/454/api_signup.php?"
					+ USER_N_Q + "=" + username + "&"
					+ PASSWORD_Q + "=" + pw + "&"
					+ PASSWROD2_Q + "=" + pw2 + "&"
					+ EMAIL + "=" + email + "&"
					+ AGREE + "=yes";
			query = query.replace(" ", "%20");
			Log.e("query: ", query);
			HttpClient client = new DefaultHttpClient();
			HttpResponse hr = null;
			try {
				hr = client.execute(new HttpGet(query));
				HttpEntity entity = hr.getEntity();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
//				String buff = "";
//				while((buff = br.readLine()) != null)
					response += br.readLine();
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
			if(response.startsWith("[")){
				try {
					JSONArray ja = new JSONArray(response);
					String message = "";
					for(int i = 0; i < ja.length(); i++){
						message += ja.getString(i) + "\n";
					}
					Toast.makeText(SignUpPage.this, message, Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					
				}
			}else if(response.equals("-1")){
				Toast.makeText(SignUpPage.this, "database temporarly unavailable", Toast.LENGTH_SHORT).show();
			}else{
				JSONObject ja = new JSONObject();
				try {
					ja.put("username", username);
					ja.put("access_token", response);
					Settings.getInstance(SignUpPage.this).saveUserInfo(ja.toString());
				} catch (JSONException e) {
				}
				try {
					String next = getIntent().getStringExtra("nextactivity");
					if(next != null){
						nextActivity = Class.forName(getIntent().getStringExtra("nextactivity"));
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if(getIntent().getStringExtra("return") == null){
					Intent i = new Intent();
					i.setClass(SignUpPage.this, nextActivity);
					startActivity(i);
				}
				finish();
//				finishActivity(0);
			}
		}
	}
}

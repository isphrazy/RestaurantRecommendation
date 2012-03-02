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
 * Login page for the app
 * @author Pingyang He
 *
 */
public class LoginPage extends Activity{
	
	private final String LOGIN_TITLE = "Login";
	private final String LOGIN_MESSAGE = "Please wait...";
	private final String LOGIN_FAIL = "username or password is wrong, please try again";
	private final String EMPTY_PU = "username or password should not be empty";
	private final String USERNAME_Q = "myusername";
	private final String PASSWORD_Q = "mypassword";
	
	private EditText usernameEt;
	private EditText passwordEt;
	private Class next;
	private ProgressDialog pd;
	
	public void onCreate(Bundle savedInstanceState){
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        
        initiateVar();
        
	}
	
	//initiate variables
	private void initiateVar() {
		usernameEt = (EditText) findViewById(R.id.username_et);
		passwordEt = (EditText) findViewById(R.id.password_et);
	}

	/**
	 * deal with click events
	 * @param view the view that has been clicked
	 */
	public void onClick(View view){
		switch(view.getId()){
			case R.id.register_b:
				Intent i = new Intent();
				i.setClass(this, SignUpPage.class);
				if(getIntent().getStringExtra("return") == null){
					i.putExtra("nextactivity", getIntent().getStringExtra("nextactivity"));
					startActivity(i);
				}else{
					i.putExtra("return","1");
					startActivityForResult(i, 0);
					finish();
				}
				break;
			case R.id.login_b:
				String username = usernameEt.getText().toString().trim();
				String password = passwordEt.getText().toString().trim();
				if(username.length() < 1 || password.length() < 1){
					Toast.makeText(LoginPage.this, EMPTY_PU, Toast.LENGTH_SHORT);
				}else{
					pd = ProgressDialog.show(LoginPage.this, LOGIN_TITLE, LOGIN_MESSAGE);
					new LoginAsync().execute(new String[]{username, password});
				}
				break;
		}
	}

	//fetch response from server
	private class LoginAsync extends AsyncTask<String, Void, Void>{
		
		private String response;
		@Override
		protected Void doInBackground(String... arg0) {

			Log.e("login", "loging");
			String query = "http://kurlin.com/454/api/api_login.php?"
					+ USERNAME_Q + "=" + arg0[0] + "&"
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
		
		
		protected void onProgressUpdate(Void... v){
//			Log.e("onProgressUpdate", response);
			pd.dismiss();
			if(response.equals("false")){
				Toast.makeText(LoginPage.this, LOGIN_FAIL, Toast.LENGTH_LONG).show();
			}else{
				
				Settings.getInstance(LoginPage.this).saveUserInfo(response);
				Intent intent = new Intent();
				if(getIntent().getStringExtra("return") == null){
					try {
						next = Class.forName(getIntent().getStringExtra("nextactivity"));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					intent.setClass(LoginPage.this, next);
					startActivity(intent);
				}
				finish();
			}
		}
	}
}

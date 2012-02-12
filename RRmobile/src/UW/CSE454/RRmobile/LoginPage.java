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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity{
	
	private final String USERNAME_Q = "myusername";
	private final String PASSWORD_Q = "mypassword";
	
	private EditText usernameEt;
	private EditText passwordEt;
	
	public void onCreate(Bundle savedInstanceState){
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        
        initiateVar();
        
	}
	
	private void initiateVar() {
		usernameEt = (EditText) findViewById(R.id.username_et);
		passwordEt = (EditText) findViewById(R.id.password_et);
	}

	public void onClick(View view){
		switch(view.getId()){
			case R.id.register_b:
				break;
			case R.id.login_b:
				login();
				break;
		}
	}

	private void login() {
		Log.e("login", "loging");
		String username = usernameEt.getText().toString().trim();
		String password = passwordEt.getText().toString().trim();
		if(username.length() < 1 || password.length() < 1){
			Toast.makeText(this, "username or password should not be empty", Toast.LENGTH_SHORT);
		}else{
			String query = "http://kurlin.com/454/api_login.php?"
					+ USERNAME_Q + "=" + username + "&"
					+ PASSWORD_Q + "=" + password;
			Log.e("login", query);
			HttpClient client = new DefaultHttpClient();
			HttpResponse hr = null;
			try {
				hr = client.execute(new HttpGet(query));
				HttpEntity entity = hr.getEntity();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
				String response = br.readLine();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			if(response == null) Log.e("response", "nullll");
//			else Log.e("response", response);
		}
	}
}

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

import android.os.AsyncTask;
import android.util.Log;

/**
 * update user's favoriate restuarants
 * @author Pingyang He
 *
 */
public class IsLikeAsyncTask extends AsyncTask<String, Void, Void> {

	private String response;
	
	@Override
	protected Void doInBackground(String... params) {
		
		String query = "http://kurlin.com/454/is_like.php?rid=" + params[0] + 
														"&like=" + params[1] + 
														"&access_token=" + params[2];
		query = query.replace(" ", "%20");
		Log.e("query: ", query);
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

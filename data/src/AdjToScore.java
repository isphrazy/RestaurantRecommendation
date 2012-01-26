import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdjToScore {
	
	private static final String ADJ_SOURCE_FILE_NAME = "all.polarityData";
	private static final String TARGET_FILE_NAME = "adjScore.data";
	
	public static void main(String[] args) throws JSONException, IOException{
		Scanner adjScn = new Scanner(new File(ADJ_SOURCE_FILE_NAME));
		StringBuilder sb = new StringBuilder();
		while(adjScn.hasNext()){
			sb.append(adjScn.nextLine());
		}
		JSONObject adjs = new JSONObject(sb.toString());
		JSONObject adjScoreJSON = new JSONObject();
		
		for(String adj : JSONObject.getNames(adjs)){
			JSONArray scores = adjs.getJSONArray(adj);
			int score = 0;
			for(int i = 0; i < scores.length(); i++){
				score += (i - 2) * scores.getInt(i);
			}
			adjScoreJSON.put(adj, score);
		}
		
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(adjScoreJSON.toString());
		out.close();
	}
}

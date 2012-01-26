import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
//		int max = Integer.MIN_VALUE;
//		int min = Integer.MAX_VALUE;
		while(adjScn.hasNext()){
			sb.append(adjScn.nextLine());
		}
		JSONObject adjs = new JSONObject(sb.toString());
		JSONObject adjScoreJSON = new JSONObject();
		
//		Map<String, Integer> adjScoreMap = new HashMap<String, Integer>();
		
		for(String adj : JSONObject.getNames(adjs)){
			JSONArray scores = adjs.getJSONArray(adj);
			int score = 0;
			int offset = 2;
			int total = 0;
			for(int i = 0; i < scores.length(); i++){
				int partScore = scores.getInt(i);
				total += partScore;
				score += (i - offset) * scores.getInt(i);
			}
			double finalScore = 1.0 * score / (offset * total);
			adjScoreJSON.put(adj, finalScore);
		}
		
		
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(adjScoreJSON.toString());
		out.close();
	}
}

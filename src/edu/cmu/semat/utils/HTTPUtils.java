package edu.cmu.semat.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

// source http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

public class HTTPUtils {

		// HTTP GET request
		public static String sendGet(String url) throws Exception {
	 
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			con.setRequestMethod("GET");
	 
			int responseCode = con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			return response.toString();
		}
	 
		// HTTP POST request
		// Data should look like: sn=C02G8416DRJM&cn=&locale=&caller=&num=12345
		public static String sendPost(String url, String data) throws Exception {
	 
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			con.setRequestMethod("POST");
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
//			wr.writeChars(data);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			return response.toString();
		}
		
			
		public static JSONObject sendPost(String url, JSONObject holder) throws Exception {

			HttpPost post = new HttpPost(url);
			DefaultHttpClient client = new DefaultHttpClient();
			String response = null;
			JSONObject json = new JSONObject();

			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);

			// setup the request headers
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/json");

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = client.execute(post, responseHandler);
			return (new JSONObject(response));
		}		
}

package com.metaddev.haktcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.metaddev.haktcode.MenuActivity.MyCustomAdapter;

import utils.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;

public class ViewItemActivity extends Activity {
	Context context;
	Intent intent;
	ArrayList<HashMap<String, String>> productsList;
	JSONArray products = null;
	ArrayList<Bitmap> bitmap_img;
	Bitmap srcBitmap;
	JSONParser jParser = new JSONParser();
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	ArrayList<Bitmap> bipmaplist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_item);
		context = this;
		intent = getIntent();
		productsList = new ArrayList<HashMap<String, String>>();
		bipmaplist = new ArrayList<Bitmap>();
	}
	
	class getUnderMenu extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		
		@SuppressLint("NewApi")
		protected String doInBackground(String... args) {
			try {
				products = getJSONfromURL(intent.getStringExtra("url")+"/"+intent.getStringExtra("id")+".json");
						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
								 map.put("id", c.getString("id"));
								 map.put("title", c.getString("title"));
								 
								 JSONArray allergens = new JSONArray(c.getString("allergens"));
								 JSONArray traces = new JSONArray(c.getString("traces"));
								 JSONArray additives = new JSONArray(c.getString("additives"));
								 for (int a = 0; a < allergens.length(); a++) {
									 
								 }
								 
								 for (int b = 0; b < traces.length(); b++) {
									 
								 }
								 for (int d = 0; d < traces.length(); d++) {
									 
								 }
								 Log.d("Title", c.getString("title"));
								 if(c.getString("picture_320_200_url").contains("null") || c.getString("picture_320_200_url").isEmpty()){
									 map.put("images", "http://opplevsondreland.no/upload_images/default.png");
								 }else{
									 map.put("images", c.getString("picture_320_200_url"));
								 }
								 Log.d("Title", c.getString("picture_320_200_url"));
								productsList.add(map);
						}
						preLoadSrcBitmap(productsList);
						
			} catch (Exception e) {
				Log.d("feil", e.getMessage());
			}

			return null;
		}
		
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
				   
				}
              
               });
		}


	}
	public static JSONArray getJSONfromURL(String url) throws Exception, IOException{
	    InputStream is = null;
	    String result = "";
	    JSONArray jArray = null;

	            HttpClient httpclient = new DefaultHttpClient();
	            HttpGet httpget = new HttpGet(url);
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();

	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf8"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	            System.out.println(sb);
	        jArray = new JSONArray(result);            
	    return jArray;
	}

	private void preLoadSrcBitmap(ArrayList<HashMap<String, String>> objects){
		
		 BitmapFactory.Options bmOptions;
		 bmOptions = new BitmapFactory.Options();
		 bmOptions.inSampleSize = 1;
		 bipmaplist.clear();
		
		 for(int i = 0; i < objects.size(); i++){
			 try{
				 	srcBitmap = LoadImage(objects.get(i).get("images"), bmOptions);
				 	bipmaplist.add(srcBitmap );
			 }catch(Exception e){
				 
			 }
		 	
		 }
		}
		
		private Bitmap LoadImage(String URL, BitmapFactory.Options options)
		{     
		 Bitmap bitmap = null;
		 InputStream in = null;     
		 try {
		  in = OpenHttpConnection(URL);
		  bitmap = BitmapFactory.decodeStream(in, null, options);
		  in.close();
		 } catch (IOException e1) {
		 }

		 return bitmap;               
		}

		private InputStream OpenHttpConnection(String strURL) throws IOException{
		 InputStream inputStream = null;
		 URL url = new URL(strURL);
		 URLConnection conn = url.openConnection();

		 try{
		  HttpURLConnection httpConn = (HttpURLConnection)conn;
		  httpConn.setRequestMethod("GET");
		  httpConn.connect();
		 
		  if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		   inputStream = httpConn.getInputStream(); 
		  } 
		 }
		 catch (Exception ex){
		 }

		 return inputStream;
		}



}

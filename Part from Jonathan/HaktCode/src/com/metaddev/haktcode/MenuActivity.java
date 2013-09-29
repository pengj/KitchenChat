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
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import utils.JSONParser;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {

	ListView lv;
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
		// I use the activity of after list because something was wrong
		setContentView(R.layout.activity_menu);
		context = this;
		intent = getIntent();
		
		productsList = new ArrayList<HashMap<String, String>>();
		bipmaplist = new ArrayList<Bitmap>();
		lv = (ListView)findViewById(R.id.listView1);
	
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		new getUnderMenu().execute();
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
			
				
				
				products = getJSONfromURL(intent.getStringExtra("url"));
						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);
							
							 HashMap<String, String> map = new HashMap<String, String>();
								 map.put("id", c.getString("id"));
								 map.put("title", c.getString("title"));
								Log.d("Title", c.getString("title"));
								 if(c.getString("picture_55_55_url").contains("null") || c.getString("picture_55_55_url").isEmpty()){
									 map.put("images", "http://opplevsondreland.no/upload_images/default.png");
								 }else{
									 map.put("images", c.getString("picture_55_55_url"));
								 }
								 Log.d("Title", c.getString("picture_55_55_url"));
								
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
				     lv.setAdapter(new MyCustomAdapter(MenuActivity.this, R.layout.item, productsList));
					//dia("Her",productsList.get(0).get("title") +"  "+productsList.get(0).get("title"),"ok","" );
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

public class MyCustomAdapter extends ArrayAdapter<HashMap<String, String>> {
	 Bitmap bm;
	 //productsList = new ArrayList<HashMap<String, String>>();
	 public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> objects) {
	  super(context, textViewResourceId, objects);
	  // TODO Auto-generated constructor stub
	 
	  bm = srcBitmap;
	 }

	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	  // TODO Auto-generated method stub
	  
	 
	  View row = convertView;
	 
	  if(row==null){
	   LayoutInflater inflater=getLayoutInflater();
	   row=inflater.inflate(R.layout.item, parent, false);
	  }
	
	  TextView label1=(TextView)row.findViewById(R.id.titel);
	  label1.setText(Html.fromHtml("  <strong>"+productsList.get(position).get("title")+"</strong>"));
	 
	  TextView label2=(TextView)row.findViewById(R.id.info);
	  
	  ImageView icon = (ImageView)row.findViewById(R.id.imageView1);
	  icon.setImageBitmap(bipmaplist.get(position));
	 
	  return row;
	 }
	}

public void dia(String titel, String info,String first_btn,String second_btn){
	
	AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
 	alertDialog.setTitle(titel);
 	alertDialog.setMessage(info);
 	alertDialog.setPositiveButton(first_btn, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  finish();
          }
      });

     alertDialog.setNeutralButton(second_btn, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
         	
				finish();
         }
     });
     alertDialog.show();
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



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.list_info, menu);
		return true;
	}

}

package com.joyn.kitchenchat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONConnection implements Callable<String>{


	private static final String TAG="JSONConnection";

	private final int TIMEOUT_MILLISEC = 10*1000;

	private int portOfServer;
	private String IpAdressOfServer;
	private String json;
	private String idJSON;
	private Context context;

	public JSONConnection (Context context, int portOfServer, String IpAdressOfServer, String json, String idJSON) {
		this.portOfServer = portOfServer;
		this.IpAdressOfServer = IpAdressOfServer;
		this.json = json;
		this.idJSON = idJSON;
		this.context = context;
	}


	public String call(){
		int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams); //we create the HTTP connection. We dont use DelfaultHTTP in order to choose the port number
		HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT_MILLISEC); //Timeout Limit
		HttpResponse response;
		String result = "";

		try{

			StringEntity se=null;
			if(json!=null)
			{
				se = new StringEntity( json + "\n"); //create the message to be sent
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/json"));

			}

			String uriServer = "http://"+IpAdressOfServer+"/" + idJSON; //setting the server address

			Log.d(TAG,"Sending JSON to " + uriServer);

			HttpPost post = new HttpPost(uriServer);

			if(se !=null)
				post.setEntity(se);//posting the message

			response = client.execute(post);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {

				InputStream responseFromServer = response.getEntity().getContent(); //Get the data in the entity
				result = fromInputStreamToString(responseFromServer);
				Log.d(TAG,"response from server = " + result);
			}
			else {
				Log.d(TAG,"Did not receive response from server");
			}

		}
		catch(Exception e){
			Log.d(TAG,"Impossible to post message. Server is maybe offline, or you asked for the wrong port number.");
			e.printStackTrace();
		}

		return result;
	}

	private String fromInputStreamToString (InputStream in) {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));//converting the input stream to string
		StringBuilder total = new StringBuilder();
		String line;
		try {
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG,"Impossible to convert from InputStream to String");
		}
		return (total.toString());
	}
}
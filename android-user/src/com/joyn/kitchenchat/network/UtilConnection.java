package com.joyn.kitchenchat.network;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class UtilConnection {


	public static String sendHttpsMessage(Context context, String message, String ip, int port, String idJSON) {
		// preparing the thread for result
		ExecutorService service = Executors.newFixedThreadPool(1);
		Future<String> task = service.submit(new JSONConnection(context, port, ip, message, idJSON));

		String responseFromServer = "";

		try {
			responseFromServer = task.get();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
			responseFromServer = "Impossible to send message";
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}

		service.shutdownNow();

		return responseFromServer;
	}

	public static String getStringValueFromJSON (String jsonTxt, String key) throws JSONException {

		JSONObject json = new JSONObject( jsonTxt );
		return json.getString(key);

	}

}

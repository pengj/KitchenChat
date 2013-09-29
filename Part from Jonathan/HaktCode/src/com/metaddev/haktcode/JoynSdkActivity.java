package com.metaddev.haktcode;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class JoynSdkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joyn_sdk);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.joyn_sdk, menu);
		return true;
	}

}

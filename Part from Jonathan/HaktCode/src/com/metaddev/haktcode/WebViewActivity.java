package com.metaddev.haktcode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	WebView wv;
	Intent in;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		in = getIntent();
		wv = (WebView)findViewById(R.id.webView1);
		
		wv.loadUrl(in.getStringExtra("http://google.com"));
	}

	

}

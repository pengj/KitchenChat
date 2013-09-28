package com.joyn.kitchenchat.ui;

import com.joyn.kitchenchat.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class LogIn extends Activity {
	private int SPLASH_TIME_OUT = 3000; // 5s

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.log_in);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		final View login_button = findViewById (R.id.login_button);
		
		login_button.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View v) {
                Intent i = new Intent(LogIn.this, HomeScreen.class);
                startActivity(i);
 
                // close this activity
                finish();
			}
		});
		
		final View signup_button = findViewById (R.id.signup_button);
		
		signup_button.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View v) {
                Intent i = new Intent(LogIn.this, FirstTime.class);
                startActivity(i); 
			}
			
		});
		
		// Show the splash for a while, then launch the main activity
		new Handler().postDelayed(new Runnable() {			 
            @Override
            public void run() {
            	login_button.setVisibility(View.VISIBLE);
            	signup_button.setVisibility(View.VISIBLE);
            	
            	findViewById(R.id.facebook).setVisibility(View.VISIBLE);
            	findViewById(R.id.twitter).setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);

	}
}

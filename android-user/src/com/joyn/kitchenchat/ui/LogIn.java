package com.joyn.kitchenchat.ui;

import com.joyn.kitchenchat.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
	private int SPLASH_TIME_OUT = 1500;

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
		
		final View facebook_button = findViewById (R.id.facebook);
		final View twitter_button = findViewById (R.id.twitter);
		
		// Show the splash for a while, then launch the main activity
		new Handler().postDelayed(new Runnable() {			 
            @Override
            public void run() {
            	login_button.setPivotX(login_button.getWidth()/2);
            	login_button.setPivotY(login_button.getHeight()/2);
            	
            	signup_button.setPivotX(login_button.getWidth()/2);
            	signup_button.setPivotY(login_button.getHeight()/2);
            	
            	facebook_button.setPivotX(login_button.getWidth()/2);
            	facebook_button.setPivotY(login_button.getHeight()/2);
            	
            	twitter_button.setPivotX(login_button.getWidth()/2);
            	twitter_button.setPivotY(login_button.getHeight()/2);
            	
            	ObjectAnimator alpha1=ObjectAnimator.ofFloat(login_button, "alpha", 0f, 1f);
                alpha1.setDuration(300);

                ObjectAnimator alpha2=ObjectAnimator.ofFloat(signup_button, "alpha", 0f, 1f);
                alpha2.setDuration(300);

                ObjectAnimator alpha3=ObjectAnimator.ofFloat(facebook_button, "alpha", 0f, 1f);
                alpha2.setDuration(300);

                ObjectAnimator alpha4=ObjectAnimator.ofFloat(twitter_button, "alpha", 0f, 1f);
                alpha2.setDuration(300);

                AnimatorSet animset=new AnimatorSet();
                animset.play(alpha1).after(250);
                animset.play(alpha2).after(100);
                animset.play(alpha3).after(150);
                animset.play(alpha2);
                animset.start();
            }
        }, SPLASH_TIME_OUT);

	}
}

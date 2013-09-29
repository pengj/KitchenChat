package com.joyn.kitchenchat.ui;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.utils.AppConstants;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HomeScreen extends Activity implements TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			Tab tab = actionBar.newTab()
					.setText("Welcome")
					.setTabListener(this);
			actionBar.addTab(tab);

			tab = actionBar.newTab()
					.setText("Search")
					.setTabListener(this);
			actionBar.addTab(tab);
		}
		
		AppConstants appConstants = ((AppConstants)getApplicationContext());

		TextView nicknameTextView = (TextView) findViewById(R.id.nickname_textview);
		nicknameTextView.setText(appConstants.getUserNickname());
		
		// TODO: temporary set them selected
		findViewById(R.id.restriction1).setSelected(true);
		findViewById(R.id.restriction2).setSelected(true);
		
		final View restaurant = findViewById (R.id.restaurant_selection);
		
		// Simulate map
		findViewById(R.id.map).setOnClickListener(
				new OnClickListener () {
					@Override
					public void onClick(View v) {
						int visibility = 
								(restaurant.getVisibility() == View.VISIBLE) ?
										View.INVISIBLE : View.VISIBLE;
						
						restaurant.setVisibility(
								visibility);
					}
				}
		);
		
		findViewById(R.id.restaurant_selection).setOnClickListener(
				new OnClickListener () {
					@Override
					public void onClick (View v) {
		                Intent i = new Intent(HomeScreen.this, Restaurant.class);
		                startActivity(i);		 
					}
				}
		);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}

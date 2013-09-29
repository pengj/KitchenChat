package com.joyn.kitchenchat.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.utils.AppConstants;

public class FirstTime extends Activity implements TabListener {
	private static final String TAG = FirstTime.class.getSimpleName ();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.first_time_general_info);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			Tab tab = actionBar.newTab()
					.setText("General Info")
					.setTabListener(this);
			actionBar.addTab(tab);

			tab = actionBar.newTab()
					.setText("Search")
					.setTabListener(this);
			actionBar.addTab(tab);
		}

		int restrictions[] = {
				R.id.restriction1,
				R.id.restriction2,
				R.id.restriction3,
				R.id.restriction4,
				R.id.restriction5,
				R.id.restriction6,
				R.id.restriction7,
				R.id.restriction8,
				R.id.restriction9,
		};
		
		final EditText nickName = (EditText) findViewById(R.id.nickname_edittext);
		
		for (int i=0;i<9;i++) {
			View view = findViewById(restrictions[i]);
			if (view != null) {
				view.setOnClickListener(
					new OnClickListener () {

						@Override
						public void onClick(View v) {
							Log.v(TAG, "Tap on "+v.getTag());
							v.setSelected(!v.isSelected());
						}
						
					});
			}
		}
		
		findViewById(R.id.create_button).setOnClickListener(
				new OnClickListener () {

					@Override
					public void onClick(View v) {
						AppConstants appConstants = ((AppConstants)getApplicationContext());
						
						String nick = nickName.getText().toString();
						if (nick != null && !nick.isEmpty())
							appConstants.setUserNickname(nick);
						else
							appConstants.setUserNickname("Rebecca");
						
		                Intent i = new Intent(FirstTime.this, HomeScreen.class);
		                startActivity(i);
		 
		                // close this activity
		                finish();
					}
					
				});
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

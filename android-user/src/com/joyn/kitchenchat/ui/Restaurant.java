package com.joyn.kitchenchat.ui;

import java.util.ArrayList;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.utils.AppConstants;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Restaurant extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		AppConstants appConstants = ((AppConstants)getApplicationContext());

		// Nickname
		TextView nickNameWelcome = (TextView) findViewById(R.id.nickname_welcome);
		nickNameWelcome.setText(appConstants.getUserNickname() + ", welcome to");
		
		ListView menuList = (ListView) findViewById (R.id.menu_list);
		
		ArrayList<Integer> menuItems = new ArrayList<Integer>();
		
		int menuItemsID[]= {
				R.drawable.item_menu_1,
				R.drawable.item_menu_2,
				R.drawable.item_menu_3,
				R.drawable.item_menu_4,
				R.drawable.item_menu_5,
				R.drawable.item_menu_6,
				R.drawable.item_menu_7				
		};

		for (int i: menuItemsID) {
			menuItems.add(i);
		}

		ImageAdapter imageAdapter = new ImageAdapter(this, menuItems);

		menuList.setDivider(null);
		menuList.setDividerHeight(0);
		menuList.setAdapter(imageAdapter);
	}	
}

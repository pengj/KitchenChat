package com.joyn.kitchenchat.ui;

import com.joyn.kitchenchat.com.Consts;
import com.joyn.kitchenchat.network.Contact;

import com.joyn.kitchenchat.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void onStartChat(View v){
		Contact friend = new Contact();
		friend.setName("Kitchen");
		friend.setPhoneNumber(Consts.KITCHEM_PHONE);
		Contact me = new Contact();
		me.setName("Me");
		me.setPhoneNumber(Consts.MY_PHONE);
		Intent intent = new Intent(this, SingleChatView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	intent.putExtra(SingleChatView.EXTRA_MODE, SingleChatView.MODE_OUTGOING);
    	intent.putExtra(SingleChatView.EXTRA_CONTACT, friend.getPhoneNumber());
		startActivity(intent);	
	}

}

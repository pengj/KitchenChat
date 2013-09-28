package com.joyn.kitchenchat.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.network.Contact;
import com.joyn.kitchenchat.network.RCS_Wrapper_Send;
import com.joyn.kitchenchat.network.RCS_Wrapper_Send.OnMessageReceivedListener;
import com.joyn.kitchenchat.network.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class ChatView extends Activity implements OnMessageReceivedListener {

	private static final String TAG = "ChatView";

	private RCS_Wrapper_Send chatController;
	private EditText input_message;
	private ScrollView mScrollView;
	private ViewGroup mMessageContainer;

	private String first_text;
	private String remoteContact;

	
	private ImageView image_contact;
	
	private TextView title_tv;
	
	private boolean float_shown=false;
	
	private boolean event_on = false;
	private boolean todo_on =false;
	
	private String date;
	private String time;
	private String place;
	private long time_long;
	
	 SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	

	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.chat_view);

		// This will contain extras info we need to show
		// Like the friend contact
		Bundle extras = getIntent().getExtras();

		Contact friend = extras.getParcelable("friend");
		Contact me = extras.getParcelable("me");

		// Handle the Send button click
		Button sendMessageButton = (Button) findViewById(R.id.sendButton);
		sendMessageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});

		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mMessageContainer = (ViewGroup) findViewById(R.id.messageContainer);

		// Show the friend name
		remoteContact = getIntent().getStringExtra("contact");
		if (remoteContact != null) {
			Log.d(TAG, "RemoteContact=" + remoteContact);
			friend = Utils.getContactFromPhoneNumber(this, remoteContact);
		}else{
			
			remoteContact = friend.getPhoneNumber();
		}


		// based on the phone number, find friend name
	/*	TextView friendLabel = (TextView) findViewById(R.id.friendLabel);
		friendLabel.setText(friend.getName());*/
		//setTitle(getString(R.string.chat_with) + " " + friend.getName());
		title_tv=(TextView)findViewById(R.id.chatview_title);
		set_Title("  "+getString(R.string.chat_with) + " " + friend.getName());
		//set the image for the contact
		image_contact=(ImageView)findViewById(R.id.chatview_friends);

		// the input message
		input_message = (EditText) findViewById(R.id.messageBox);
		input_message.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d(TAG, "input="+s);
			}
		});

		// TODO: use a ChatController to handle chat messages
		/*
		 * ChatController chatController = new ChatController (me, friend);
		 * chatController.setOnMessageReceivedListener
		 * (onMessageReceivedListener);
		 */
		Log.d(TAG, "contract=" + friend.getPhoneNumber());
		chatController = new RCS_Wrapper_Send(this);

	}
	
	
	
	
	/**
	 * set the title bar
	 * **/
	private void set_Title(String friend_name)
	{
		Typeface face=Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
		title_tv.setTypeface(face);
		
		title_tv.setText(friend_name);
	}
	
	
	/**
	 * set the contact image
	 * **/
	private void set_Image()
	{
		
	}
	
	/**
	 * The back button action
	 * ***/
	public void onBackAction(View v)
	{
		finish();
	}
	
	
	@Override
	public void onMessageReceived(android.os.Message message) {
		showMessage((String) message.obj, false);
	
	}
	
	protected void showMessage (String message, boolean leftSide) {
        
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 5, 0, 0);
		Typeface face=Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
		
        if (leftSide) {
        	final View messageView = View.inflate(getApplicationContext(), R.layout.chattext_right, null);

    		TextView textView = (TextView)messageView.findViewById(R.id.chattext_left_text);

    		textView.setText(message);
            
            textView.setBackgroundColor(getResources().getColor(R.color.aqua));
    		
    		//textView.setLayoutParams(params);
    		 
    		textView.setTypeface(face); 
    		textView.setTextSize(18);
    		textView.setTextColor(getResources().getColor(android.R.color.black));
    		
    		messageView.setLayoutParams(params);
    		
    		runOnUiThread(new Runnable() {
    			@Override
    			public void run() {
    				
    				mMessageContainer.addView(messageView);

    				// Scroll to bottom
    				if (mScrollView.getChildAt(0) != null) {
    					mScrollView.scrollTo(mScrollView.getScrollX(), mScrollView.getChildAt(0).getHeight());
    				}

    				mScrollView.fullScroll(View.FOCUS_DOWN);
    			}
    		});
        } else {
        	final View messageView = View.inflate(getApplicationContext(), R.layout.chattext_left, null);

    		TextView textView = (TextView)messageView.findViewById(R.id.chattext_left_text);

    		textView.setText(message);

        	textView.setBackgroundColor(getResources().getColor(R.color.lime));
        	textView.setGravity(Gravity.RIGHT);
        	//textView.setLayoutParams(params);

    		textView.setTypeface(face); 
    		textView.setTextSize(18);
    		textView.setTextColor(getResources().getColor(android.R.color.black));
    		
    		messageView.setLayoutParams(params);
        	
        	runOnUiThread(new Runnable() {
    			@Override
    			public void run() {
    				
    				mMessageContainer.addView(messageView);

    				// Scroll to bottom
    				if (mScrollView.getChildAt(0) != null) {
    					mScrollView.scrollTo(mScrollView.getScrollX(), mScrollView.getChildAt(0).getHeight());
    				}

    				mScrollView.fullScroll(View.FOCUS_DOWN);
    			}
    		});
        }


	
	}

	protected void sendMessage() {
		String message = input_message.getText().toString();
		input_message.setText("");

		chatController.sendMessage(message);

		showMessage(message, true);

		
	}

	@Override
	protected void onDestroy() {
		chatController.Quit_MSG();
		super.onDestroy();
	}

	
}

/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.joyn.kitchenchat.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.gsma.joyn.JoynServiceListener;
import org.gsma.joyn.chat.ChatLog;
import org.gsma.joyn.chat.ChatMessage;
import org.gsma.joyn.chat.ChatService;
import org.gsma.joyn.contacts.ContactsService;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.com.Consts;
import com.joyn.kitchenchat.network.Utils;

import android.app.AlertDialog;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


/**
 * Chat view
 */
public abstract class ChatView extends ListActivity implements OnClickListener, OnKeyListener, JoynServiceListener, TextToSpeech.OnInitListener {	

	private static final String TAG = "ChatView";

	private static final int REQUEST_CODE = 1234;


	/**
	 * UI handler
	 */
	protected Handler handler = new Handler();

	/**
	 * Progress dialog
	 */
	protected Dialog progressDialog = null;

	/**
	 * Chat API
	 */
	protected ChatService chatApi = null;

	/**
	 * Message composer
	 */
	protected EditText composeText;


	/**
	 * Send button
	 */
	protected Button sendBtn;
	
	protected ImageButton startChatBtn, talkChatBtn;
	
	protected ImageButton orderBtn;
	
	protected ScrollView sendTextView;


	/**
	 * Linear layout for the dish 
	 * ***/
	protected LinearLayout Chat_layout;

	protected boolean Chat_on=false;
	
	
	//for the order 
	protected boolean user_ordered = false;
	
	protected boolean user_cooked = false;
	
	protected boolean user_finished = false;
	
	
	protected boolean kitchen_ordered = false;
	
	protected boolean kitchen_cooked = false;
	
	protected boolean kitchen_finished = false;

	/**
	 * Message list adapter
	 */
	protected MessageListAdapter msgListAdapter;

	/**
	 * Contacts API
	 */
	protected ContactsService contactsApi;    

	/**
	 * Utility class to manage the is-composing status
	 */
	protected IsComposingManager composingManager = null;



	/**
	 * TextToSpeech
	 * **/
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set layout
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.chat_view);

		// Set the message list adapter
		msgListAdapter = new MessageListAdapter(this);
		setListAdapter(msgListAdapter);


		composeText = (EditText)findViewById(R.id.userText);
		composeText.setOnClickListener(this);
		composeText.setOnKeyListener(this);
		composeText.addTextChangedListener(mUserTextWatcher);

		// Set send button listener
		sendBtn = (Button)findViewById(R.id.send_button);
		
		startChatBtn = (ImageButton)findViewById(R.id.button_chat);
		
		talkChatBtn = (ImageButton)findViewById(R.id.button_talk);
		
		orderBtn = (ImageButton)findViewById(R.id.button_order);
		
		sendTextView = (ScrollView)findViewById(R.id.layout_text);


		// the layout for the chat
		Chat_layout = (LinearLayout)findViewById(R.id.chatbox);


		//update the UI for chief 
		Check_chiefUI();

		// Instanciate API
		chatApi = new ChatService(getApplicationContext(), this);
		contactsApi = new ContactsService(getApplicationContext(), null);

		// Connect API
		chatApi.connect();
		contactsApi.connect();


		tts = new TextToSpeech(this, this);
	}


	private void Check_chiefUI(){

		if(Consts.KITCHEN)
		{
			sendBtn.setText("Talk");
			
			Chat_layout.setVisibility(View.VISIBLE);
			
			startChatBtn.setVisibility(View.GONE);
			
			orderBtn.setVisibility(View.GONE);
			
			talkChatBtn.setVisibility(View.VISIBLE);
			
			sendTextView.setVisibility(View.GONE);

			Chat_on =true;
		}
	}



	@Override
	public void onDestroy() {


		// Disconnect API
		chatApi.disconnect();
		contactsApi.disconnect();

		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}

		super.onDestroy();


	}


	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			//int result = tts.setLanguage(Locale.getDefault());

			int result = tts.setLanguage(Locale.FRANCE);
			//setTtsLanguage(tts, Locale.ITALIAN);


			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}
	


	private void SpeakOut(String text){

		if((text!=null)&&(text.length()>0))
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}
	
	
	
class reStuff extends AsyncTask<Void, Void, Void>{
        
        String translatedText = "";
        String pre_text="";
        String contact = "";
        
        
        public reStuff(String text, String c)
        {
        	pre_text=text;
        	contact = c;
        	
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                
                if(pre_text.contains("Put-Order")||pre_text.contains("Put-Cook")||pre_text.contains("Put-Finish"))
                {
                	translatedText = pre_text;
                }else{
                	translatedText = Translate(pre_text);
                }
                
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                translatedText = e.toString();
            }
             
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        
        	//only speek for the chief
			if(Consts.KITCHEN)
				SpeakOut(translatedText);

			// Quit the session
			
			
			
			addMessageHistory(ChatLog.Message.Direction.INCOMING, contact, translatedText);
    		
        }
         
    }
	
	
	class bgStuff extends AsyncTask<Void, Void, Void>{
        
        String translatedText = "";
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String text = composeText.getText().toString();
                
                if(text.contains("Put-Order")||text.contains("Put-Cook")||text.contains("Put-Finish"))
                {
                	translatedText = text;
                }else{
                	translatedText = Translate(text);
                }
                
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                translatedText = e.toString();
            }
             
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	Log.d(TAG, "Send Text:"+translatedText);
    		
    		// Check if the service is available
    		boolean registered = false;
    		try {
    			if ((chatApi != null) && chatApi.isServiceRegistered()) {
    				registered = true;
    			}
    		} catch(Exception e) {}
    		if (!registered) {
    			Utils.showMessage(ChatView.this, getString(R.string.label_service_not_available));
    			return;
    		}

    		// Send text message
    		String msgId = sendMessage(translatedText);
    		if (msgId != null) {
    			// Add text to the message history
    			addMessageHistory(ChatLog.Message.Direction.OUTGOING, getString(R.string.label_me), translatedText);
    			composeText.setText(null);
    		} else {
    			Utils.showMessage(ChatView.this, getString(R.string.label_send_im_failed));
    		}
        }
         
    }
	
	
	private String Translate(String text){
		
		   Translate.setClientId("JoynKitchenTest");
	       Translate.setClientSecret("R3PJJGUubanOcm8Ifl6fHZ2OS2L48Ymm6fqAb9/TLA4=");
	        
	       String translatedText = text;
	        
	       // English AUTO_DETECT -> France Change this if u wanna other languages
	       try {
	    	if(Consts.KITCHEN)
	    	{
	    		translatedText = Translate.execute(text,Language.FRENCH);
	    	}else{
	    		translatedText = Translate.execute(text,Language.ENGLISH);
	    	}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       return translatedText;
		
		
	}
	

	/**
	 * Message composer listener
	 * 
	 * @param v View
	 */
	public void onClick(View v) {
		sendText();

	}



	/**
	 * The send button action, same as above code
	 * 
	 * **/
	public void onSendAction(View v){

		if(Consts.KITCHEN)
		{
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "AndroidBite Voice Recognition...");
			startActivityForResult(intent, REQUEST_CODE);
		}else{
			sendText();
		}


	}
	
	public void onTalkChat(View v){
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fr"); 
		
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Android Voice Recognition...");
		startActivityForResult(intent, REQUEST_CODE);
		
	}
	


	/**
	 * The ask button action
	 * 
	 * **/
	public void onStartChat(View v){

		Chat_layout.setVisibility(View.VISIBLE);
		
		startChatBtn.setVisibility(View.GONE);
		
		talkChatBtn.setVisibility(View.GONE);

		Chat_on =true;

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
		{
			ArrayList<String> matches = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);

			composeText.setText(matches.get(0));

			sendText();
			

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * The reverse back button action
	 * 
	 * **/

	private void onBackAction(){

		if((Chat_on)&&(!Consts.KITCHEN)){
			Chat_layout.setVisibility(View.GONE);
			startChatBtn.setVisibility(View.VISIBLE);
			Chat_on=false;
		}else{
			// Quit the session
			quitSession();

			finish();
		}

	}


	/**
	 * Message composer listener
	 * 
	 * @param v View
	 * @param keyCode Key code
	 * @event Key event
	 */
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				sendText();
				return true;
			}
		}


		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			Log.d(TAG, "onback pressed");
			onBackAction();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Hide progress dialog
	 */
	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}        

	/**
	 * Add a message in the message history
	 * 
	 * @param direction Direction
	 * @param contact Contact
	 * @param text Text message
	 */
	protected void addMessageHistory(int direction, String contact, String text) {
		TextMessageItem item = new TextMessageItem(direction, contact, text);
		msgListAdapter.add(item);
	}

	/**
	 * Add a notif in the message history
	 * 
	 * @param notif Notification
	 */
	protected void addNotifHistory(String notif) {
		NotifMessageItem item = new NotifMessageItem(notif);
		msgListAdapter.add(item);
	}    

	/**
	 * Send a text and display it
	 */
	private void sendText() {

		String text = composeText.getText().toString();
		
		text=Parse_SendText(text);


		if ((text == null) || (text.length() == 0)) {
			return;
		}

		new bgStuff().execute();
	}


	
	
	public void onOrderClick(View v)
	{
		
		if(Consts.KITCHEN)
		{
			
			//send cooking
			if(kitchen_ordered)
			{
				//send cooking information to user
				String txt = "Put-Cook";
				composeText.setText(txt);
				sendText();
				
				
				orderBtn.setBackground(getResources().getDrawable(R.drawable.cooking));
				
				kitchen_cooked=true;
				kitchen_ordered=false;
				
				return;
			//send finished	
			}else if( kitchen_cooked)
			{
				//send finish informaiton to user
				String txt = "Put-Finish";
				composeText.setText(txt);
				sendText();
				
				orderBtn.setVisibility(View.GONE);
				return;
			}
			
			
			
			
		}else{
			
			if(! user_ordered)
			{
				user_ordered = true;
				//send order text to kitchen
				String txt = "Put-Order";
				composeText.setText(txt);
				sendText();
				
				Chat_layout.setVisibility(View.GONE);
				startChatBtn.setVisibility(View.VISIBLE);
				startChatBtn.setBackground(getResources().getDrawable(R.drawable.chef_busy));
				
				
			}
			
		}
		
		
	}
	
	private String Parse_SendText(String txt){

		
		return txt;


	}
	
	private String Parse_ReceviedText(String txt){
		
		if(txt.contentEquals("Put-Order"))
		{
			
			kitchen_ordered=true;
			orderBtn.setVisibility(View.VISIBLE);
			orderBtn.setBackground(getResources().getDrawable(R.drawable.ordered));
			
			return null;
		}
		
		if(txt.contentEquals("Put-Cook"))
		{
			orderBtn.setBackground(getResources().getDrawable(R.drawable.waiting));
			kitchen_cooked=true;
			
			return null;
		}
		
		if(txt.contentEquals("Put-Finish"))
		{
			orderBtn.setBackground(getResources().getDrawable(R.drawable.cooked));
			kitchen_finished=true;
			startChatBtn.setVisibility(View.VISIBLE);
			startChatBtn.setBackground(getResources().getDrawable(R.drawable.thank_chef));
			
			return null;
		}
		
		
		
		
		
		return txt;
	}

	/**
	 * Display received message
	 * 
	 * @param msg Instant message
	 */
	protected void displayReceivedMessage(ChatMessage msg) {
		String contact = msg.getContact();
		String txt = msg.getMessage();

		Log.d(TAG, "Received Text:"+txt);


		String after_process = Parse_ReceviedText(txt);

		if(after_process!=null)
		{
			
			new reStuff(after_process, contact).execute();
			
			
		}
	}



	/**********************************************************************
	 ******************	Deals with isComposing feature ********************
	 **********************************************************************/

	private final TextWatcher mUserTextWatcher = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {
			// Check if the text is not null.
			// we do not wish to consider putting the edit text back to null (like when sending message), is having activity 
			if (s.length()>0) {
				// Warn the composing manager that we have some activity
				if (composingManager != null) {
					composingManager.hasActivity();
				}
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	};

	/**
	 * Utility class to handle is_typing timers (see RFC3994)  
	 */
	protected class IsComposingManager{

		// Idle time out (in ms)
		private int idleTimeOut = 0;

		// Active state refresh interval (in ms)
		private final static int ACTIVE_STATE_REFRESH = 60*1000; 

		// Clock handler
		private ClockHandler handler = new ClockHandler();

		// Is composing state
		public boolean isComposing = false;

		// Event IDs
		private final static int IS_STARTING_COMPOSING = 1;
		private final static int IS_STILL_COMPOSING = 2;
		private final static int MESSAGE_WAS_SENT = 3;
		private final static int ACTIVE_MESSAGE_NEEDS_REFRESH = 4;
		private final static int IS_IDLE = 5;

		public IsComposingManager(int timeout) {
			idleTimeOut = timeout;
		}

		// Clock handler class
		private class ClockHandler extends Handler {
			public void handleMessage(Message msg){
				switch(msg.what){
				case IS_STARTING_COMPOSING :{
					// Send a typing status "active"
					ChatView.this.setTypingStatus(true);

					// In IDLE_TIME_OUT we will need to send a is-idle status message 
					handler.sendEmptyMessageDelayed(IS_IDLE, idleTimeOut);

					// In ACTIVE_STATE_REFRESH we will need to send an active status message refresh
					handler.sendEmptyMessageDelayed(ACTIVE_MESSAGE_NEEDS_REFRESH, ACTIVE_STATE_REFRESH);
					break;
				}    			
				case IS_STILL_COMPOSING :{
					// Cancel the IS_IDLE messages in queue, if there was one
					handler.removeMessages(IS_IDLE);

					// In IDLE_TIME_OUT we will need to send a is-idle status message
					handler.sendEmptyMessageDelayed(IS_IDLE, idleTimeOut);
					break;
				}
				case MESSAGE_WAS_SENT :{
					// We are now going to idle state
					composingManager.hasNoActivity();

					// Cancel the IS_IDLE messages in queue, if there was one
					handler.removeMessages(IS_IDLE);

					// Cancel the ACTIVE_MESSAGE_NEEDS_REFRESH messages in queue, if there was one
					handler.removeMessages(ACTIVE_MESSAGE_NEEDS_REFRESH);
					break;
				}	    			
				case ACTIVE_MESSAGE_NEEDS_REFRESH :{
					// We have to refresh the "active" state
					ChatView.this.setTypingStatus(true);

					// In ACTIVE_STATE_REFRESH we will need to send an active status message refresh
					handler.sendEmptyMessageDelayed(ACTIVE_MESSAGE_NEEDS_REFRESH, ACTIVE_STATE_REFRESH);
					break;
				}
				case IS_IDLE :{
					// End of typing
					composingManager.hasNoActivity();

					// Send a typing status "idle"
					ChatView.this.setTypingStatus(false);

					// Cancel the ACTIVE_MESSAGE_NEEDS_REFRESH messages in queue, if there was one
					handler.removeMessages(ACTIVE_MESSAGE_NEEDS_REFRESH);
					break;
				}
				}
			}
		}

		/**
		 * Edit text has activity
		 */
		public void hasActivity() {
			// We have activity on the edit text
			if (!isComposing){
				// If we were not already in isComposing state
				handler.sendEmptyMessage(IS_STARTING_COMPOSING);
				isComposing = true;
			} else {
				// We already were composing
				handler.sendEmptyMessage(IS_STILL_COMPOSING);
			}
		}

		/**
		 * Edit text has no activity anymore
		 */
		public void hasNoActivity(){
			isComposing = false;
		}

		/**
		 * The message was sent
		 */
		public void messageWasSent(){
			handler.sendEmptyMessage(MESSAGE_WAS_SENT);
		}
	}



	/**
	 * Message item
	 */
	protected abstract class MessageItem {
		private int direction;

		private String contact;

		public MessageItem(int direction, String contact) {
			this.direction = direction;
			this.contact = contact;
		}

		public int getDirection() {
			return direction;
		}

		public String getContact() {
			return contact;
		}
	}	

	/**
	 * Text message item
	 */
	private class TextMessageItem extends MessageItem {
		private String text;

		public TextMessageItem(int direction, String contact, String text) {
			super(direction, contact);

			this.text = text;
		}

		public String getText() {
			return text;
		}
	}	

	/**
	 * Notif message item
	 */
	private class NotifMessageItem extends MessageItem {
		private String text;

		public NotifMessageItem(String text) {
			super(ChatLog.Message.Direction.IRRELEVANT, null);

			this.text = text;
		}

		public String getText() {
			return text;
		}
	}	

	/**
	 * Message list adapter
	 */
	public class MessageListAdapter extends ArrayAdapter<MessageItem> {
		private Context context; 

		public MessageListAdapter(Context context) {
			super(context, R.layout.chat_view_item);

			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			MessageItemHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				row = inflater.inflate(R.layout.chat_view_item, parent, false);
				holder = new MessageItemHolder();
				holder.text = (TextView)row.findViewById(R.id.item_text);
				row.setTag(holder);
			} else {
				holder = (MessageItemHolder)row.getTag();
			}

			MessageItem item = (MessageItem)getItem(position);
			String line;
			if (item.getDirection() == ChatLog.Message.Direction.OUTGOING) {
				line = "[" + getString(R.string.label_me) + "] ";
			} else {
				line = "[" + item.getContact() + "] ";
			}
			if (item instanceof NotifMessageItem) {
				NotifMessageItem notifItem = (NotifMessageItem)item;
				holder.text.setText(notifItem.getText());
			} else {
				TextMessageItem txtItem = (TextMessageItem)item;
				String txt = txtItem.getText();
				line += txt;

				holder.text.setText(line);
			}

			return row;
		}

		private class MessageItemHolder {
			TextView text;
		}
	}



	/**
	 * Send message
	 * 
	 * @param msg Message
	 * @return Message ID
	 */
	protected abstract String sendMessage(String msg);

	/**
	 * Quit the session
	 */
	protected abstract void quitSession();

	/**
	 * Update the is composing status
	 * 
	 * @param isTyping Is compoing status
	 */
	protected abstract void setTypingStatus(boolean isTyping);
}

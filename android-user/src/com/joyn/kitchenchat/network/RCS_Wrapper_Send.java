package com.joyn.kitchenchat.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import om.joyn.kitchenchat.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import com.joyn.kitchenchat.ui.ChatView;
import com.orangelabs.rcs.core.ims.service.im.chat.ChatError;
import com.orangelabs.rcs.core.ims.service.im.chat.imdn.ImdnDocument;
import com.orangelabs.rcs.provider.settings.RcsSettings;
import com.orangelabs.rcs.service.api.client.ClientApiListener;
import com.orangelabs.rcs.service.api.client.ImsEventListener;
import com.orangelabs.rcs.service.api.client.contacts.ContactsApi;
import com.orangelabs.rcs.service.api.client.eventslog.EventsLogApi;
import com.orangelabs.rcs.service.api.client.messaging.GeolocMessage;
import com.orangelabs.rcs.service.api.client.messaging.IChatEventListener;
import com.orangelabs.rcs.service.api.client.messaging.IChatSession;
import com.orangelabs.rcs.service.api.client.messaging.IMessageDeliveryListener;
import com.orangelabs.rcs.service.api.client.messaging.InstantMessage;
import com.orangelabs.rcs.service.api.client.messaging.MessagingApi;
import com.orangelabs.rcs.utils.PhoneUtils;

/**
 * The RCS wrapper
 * which use to setup the session
 * send and receive msg
 * **/
public class RCS_Wrapper_Send implements ClientApiListener, ImsEventListener{
	
	private static final String TAG="RCS_Wrapper_Send";
	
	
	public static final int MSG_SUCCESS = 100001;
	public static final int MSG_DELEVED = 200001;
	public static final int MSG_ERROR = 300001;

	//the holding activity
	protected ChatView mActivity;
	
	//the contact string
	protected String con_String;
    
    /**
     * Progress dialog
     */
	protected Dialog progressDialog = null;
	
	/**
	 * Messaging API
	 */
	protected MessagingApi messagingApi;

	/**
	 * Chat session 
	 */
	protected IChatSession chatSession = null;

	/**
	 * Participants
	 */
	protected ArrayList<String> participants;

	
	/**
     * UI handler
     */
	protected Handler handler = new Handler();
	
	 /**
     * Utility class to manage IsComposing status
     */
	private IsComposingManager composingManager;
	
	/**
	 * Flag indicating that the activity is put on background
	 */
	private boolean isInBackground = false;
	
	/**
	 * Messages that were received while we were in background as have to be marked as displayed
	 */
	private List<InstantMessage> imReceivedInBackgroundToBeDisplayed = new ArrayList<InstantMessage>();

	/**
	 * Messages that were received while we were in background as have to be marked as read
	 */
	private List<InstantMessage> imReceivedInBackgroundToBeRead = new ArrayList<InstantMessage>();
	
	
	
	/**
	 * The constractor the wrapper
	 * @param activity parent activty
	 * @param contract the contact string
	 * **/
	
	public RCS_Wrapper_Send(ChatView hold_activity){
		mActivity=hold_activity;
			
		// Instanciate the composing manager
		composingManager = new IsComposingManager();
		
		// Instanciate messaging API
        messagingApi = new MessagingApi(mActivity.getApplicationContext());
        messagingApi.addApiEventListener(this);
        messagingApi.addImsEventListener(this);
        messagingApi.connectApi();
        
        // Instanciate contacts API
        //contactsApi = new ContactsApi(getApplicationContext());
		
        
     // Update background flag
    	isInBackground = false;
    	
    	// Mark all messages that were received while we were in background as "displayed" 
    	for (int i=0;i<imReceivedInBackgroundToBeDisplayed.size();i++){
    		InstantMessage msg = imReceivedInBackgroundToBeDisplayed.get(i);
    		markMessageAsDisplayed(msg);
    	}
    	imReceivedInBackgroundToBeDisplayed.clear();
    	
    	// Mark all messages that were received while we were in background as "read" 
    	for (int i=0;i<imReceivedInBackgroundToBeRead.size();i++){
    		InstantMessage msg = imReceivedInBackgroundToBeRead.get(i);
    		markMessageAsDisplayed(msg);
    	}
    	imReceivedInBackgroundToBeRead.clear();
    	
	}
	
	
	
	/**
	 * 
	 * load the conversition from the DB, regarding to this user
	 * @param contact contact string
	 * **/
	public void LoadHistory(String contact){
		
		
	}

	  /***
     * Send message
     * 
     * @param msg Message
     */
    public void sendMessage(final String msg) {
        // Test if the session has been created or not
		if (chatSession == null) {
			// Initiate the chat session in background
	        Thread thread = new Thread() {
	        	public void run() {
	            	try {
            			chatSession = messagingApi.initiateOne2OneChatSession(participants.get(0), msg);
	            		chatSession.addSessionListener(chatSessionListener);
	            	} catch(Exception e) {
	            		Log.d(TAG, e.toString());
	            		handler.post(new Runnable(){
	            			public void run(){
	            				Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_invitation_failed));		
	            			}
	            		});
	            	}
	        	}
	        };
	        thread.start();

	        // Display a progress dialog
	    /*    progressDialog = Utils.showProgressDialog(mActivity, mActivity.getString(R.string.label_command_in_progress));
	        progressDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					Toast.makeText(mActivity, mActivity.getString(R.string.label_chat_initiation_canceled), Toast.LENGTH_SHORT).show();
					quitSession();
				}
			});*/
        } else {
        	try {
    			// Send the text to remote
    	    	chatSession.sendMessage(msg);
    	    	
    	        // Warn the composing manager that the message was sent
    			composingManager.messageWasSent();
    			
    			//store the msg
    			//switch to the chatview
    		/*	com.orange.labs.hackizard.common.Message db_msg = new com.orange.labs.hackizard.common.Message();
    			db_msg.setOut(true);
    			db_msg.setMessage(msg);
    			db_msg.setContact(participants.get(0));
    			Date now = new Date();
    			db_msg.setTimestamp(now.getTime());
    			
    			msgDB.add(db_msg);*/
    	    } catch(Exception e) {
    	    	Utils.showMessage(mActivity, mActivity.getString(R.string.label_send_im_failed));
    	    }
        }    	
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
    
    
    
    public void Quit_MSG(){
    	
    	  // Remove session listener
        if (chatSession != null) {
        	try {
        		chatSession.removeSessionListener(chatSessionListener);
        	} catch(Exception e) {
        	}
        }

        // Remove delivery listener
    	try {
    		messagingApi.removeMessageDeliveryListener(deliveryListener);
    	} catch(Exception e) {
    	}

        // Disconnect messaging API
        messagingApi.removeApiEventListener(this);
        messagingApi.removeImsEventListener(this);
        messagingApi.disconnectApi();
    }
    
    
    /**
     * Quit the session
     */
    public void quitSession() {
		// Stop session
        Thread thread = new Thread() {
        	public void run() {
            	try {
                    if (chatSession != null) {
                		chatSession.removeSessionListener(chatSessionListener);
                		chatSession.cancelSession();
                    }
            	} catch(Exception e) {
            	}
            	chatSession = null;
        	}
        };
        thread.start();
         
    }
    
    
	/**
	 * Display received message
	 * 
	 * @param msg Instant message
	 */
    private void displayReceivedMessage(InstantMessage msg) {
		String contact = msg.getRemote();
		
		Log.d(TAG, "Receive msg from"+contact + ": "+msg.getTextMessage());
		
		//TODO display message and store in the DB
		Message message = Message.obtain();
		message.arg1=MSG_SUCCESS;
		message.obj=msg.getTextMessage();
		((OnMessageReceivedListener)mActivity).onMessageReceived(message);
		
		/*
		 * switch to the chatview 
		com.orange.labs.hackizard.common.Message db_msg = new com.orange.labs.hackizard.common.Message();
		db_msg.setOut(false);
		db_msg.setMessage(msg.getTextMessage());
		db_msg.setContact(contact);
		db_msg.setTimestamp(msg.getDate().getTime());
		
		msgDB.add(db_msg);*/
		
    }
    
    
    public static interface OnMessageReceivedListener {
        void onMessageReceived(Message message);
    }
    
    
    /**
     * Add a notif in the message history
     * 
     * @param notif Notification
     */
    private void addNotifHistory(String notif) {
		//TODO save the message in the DB
    	Log.d(TAG, "Receive notif="+notif);
    }
    
    
    /**
     * Mark a message as "displayed"
     * 
     * @param msg Message
     */
    private void markMessageAsDisplayed(InstantMessage msg){
        if (RcsSettings.getInstance().isImDisplayedNotificationActivated()) {
            try {
                chatSession.setMessageDeliveryStatus(msg.getMessageId(), ImdnDocument.DELIVERY_STATUS_DISPLAYED);
            } catch(RemoteException e) {
                // Nothing to do
            }
        }
    }

    /**
     * Mark a message as "read"
     */
    private void markMessageAsRead(InstantMessage msg){
    	EventsLogApi events = new EventsLogApi(mActivity);
    	events.markChatMessageAsRead(msg.getMessageId(), true);
    }
    
    
    
    /**
     * Chat message delivery event listener
     */
    private IMessageDeliveryListener deliveryListener = new IMessageDeliveryListener.Stub() {
    	// Message delivery status
    	public void handleMessageDeliveryStatus(final String contact, String msgId, final String status) {
    		if (contact.indexOf(participants.get(0)) != -1) {
				handler.post(new Runnable(){
					public void run(){
						if (status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_FAILED) ||
								status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_ERROR) ||
									status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_FORBIDDEN)) {
							addNotifHistory(mActivity.getString(R.string.label_receive_delivery_status_failed));
							//TODO The message is not delivered
							
						} else
						if (status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_DISPLAYED)) {
							addNotifHistory(mActivity.getString(R.string.label_receive_delivery_status_displayed));
						} else
						if (status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_DELIVERED)) {
							addNotifHistory(mActivity.getString(R.string.label_receive_delivery_status_delivered));
							//TODO the message is delivered
						}
					}
				});
    		}
    	}

		@Override
		public void handleFileDeliveryStatus(String arg0, String arg1)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}
    };
    
    
    /**
  	 * Utility class to handle is_typing timers (see RFC3994)  
  	 */
  	private class IsComposingManager{

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

          public IsComposingManager() {
              idleTimeOut = 60 * 1000;
          }

  		// Clock handler class
  		private class ClockHandler extends Handler {
  			public void handleMessage(Message msg){
  				switch(msg.what){
  					case IS_STARTING_COMPOSING :{
  						// Send a typing status "active"
  						// TODO
  						//mActivity.setTypingStatus(true);
  	
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
  						// TODO
  						//mActivity.setTypingStatus(true);
  	
  						// In ACTIVE_STATE_REFRESH we will need to send an active status message refresh
  						handler.sendEmptyMessageDelayed(ACTIVE_MESSAGE_NEEDS_REFRESH, ACTIVE_STATE_REFRESH);
  						break;
  					}
  					case IS_IDLE :{
  						// End of typing
  						composingManager.hasNoActivity();
  	
  						// Send a typing status "idle"
  						//TODO
  						//mActivity.setTypingStatus(false);
  	
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
     * Chat session event listener
     */
    protected IChatEventListener chatSessionListener = new IChatEventListener.Stub() {
		// Session is started
		public void handleSessionStarted() {
			handler.post(new Runnable() { 
				public void run() {
					// Hide progress dialog
					//hideProgressDialog();
					//TODO change the image color
				}
			});
		}
	
		// Session has been aborted
		public void handleSessionAborted(int reason) {
			handler.post(new Runnable(){
				public void run(){
					// Session aborted
					Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_chat_aborted));
				}
			});
		}
	    
		// Session has been terminated by remote
		public void handleSessionTerminatedByRemote() {
			handler.post(new Runnable(){
				public void run(){
					Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_chat_terminated_by_remote));
				}
			});
		}
		
		// New text message received
		public void handleReceiveMessage(final InstantMessage msg) {
			
			Log.d(TAG, "Receive msg="+msg.getRemote());
			
			/*if (msg.isImdnDisplayedRequested()) {
				if (!isInBackground) {
					// We received the message, mark it as displayed if the view is not in background
					markMessageAsDisplayed(msg);
				} else {
					// We save this message and will mark it as displayed when the activity resumes
					imReceivedInBackgroundToBeDisplayed.add(msg);
				}
			} else {
				if (!isInBackground) {
					// We received the message, mark it as read if the view is not in background
					markMessageAsRead(msg);
				} else {
					// We save this message and will mark it as read when the activity resumes
					imReceivedInBackgroundToBeRead.add(msg);
				}
			}*/
			
			handler.post(new Runnable() { 
				public void run() {
					displayReceivedMessage(msg);
				}
			});
		}		
				
		// Chat error
		public void handleImError(final int error) {
			handler.post(new Runnable() {
				public void run() {
					// Display error
					if (error == ChatError.SESSION_INITIATION_DECLINED) {
						Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_invitation_declined));
					} else {
						Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_chat_failed, error));
					}					
				}
			});
		}	
		
		// Is composing event
		public void handleIsComposingEvent(String contact, final boolean isComposing) {
			final String number = PhoneUtils.extractNumberFromUri(contact);
			handler.post(new Runnable() {
				public void run(){
					//TODO handle the composing event 
					
					
					/*TextView view = (TextView)findViewById(R.id.isComposingText);
					if (isComposing) {
						view.setText(number	+ " " + getString(R.string.label_contact_is_composing));
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.GONE);
					}*/
				}
			});
		}

		// Conference event
	    public void handleConferenceEvent(final String contact, final String contactDisplayname, final String state) {
			handler.post(new Runnable() {
				public void run(){
					
				}
			});
		}
	    
		// Message delivery status
		public void handleMessageDeliveryStatus(final String msgId, final String status) {
			handler.post(new Runnable(){
				public void run(){
					if (status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_FAILED) ||
							status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_ERROR) ||
								status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_FORBIDDEN)) {
						addNotifHistory(mActivity.getString(R.string.label_receive_delivery_status_failed));
					} else
					if (status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_DISPLAYED)) {
						addNotifHistory(mActivity.getString(R.string.label_receive_delivery_status_displayed));
					} else
					if (status.equalsIgnoreCase(ImdnDocument.DELIVERY_STATUS_DELIVERED)) {
						addNotifHistory(mActivity.getString(R.string.label_receive_delivery_status_delivered));
					}
				}
			});
		}
		
		// Request to add participant is successful
		public void handleAddParticipantSuccessful() {
			handler.post(new Runnable() {
				public void run(){
					addNotifHistory(mActivity.getString(R.string.label_add_participant_success));
				}
			});
		}
	    
		// Request to add participant has failed
		public void handleAddParticipantFailed(String reason) {
			handler.post(new Runnable() {
				public void run(){
					addNotifHistory(mActivity.getString(R.string.label_add_participant_failed));
				}
			});
		}
		
		// New geoloc message received
		public void handleReceiveGeoloc(final GeolocMessage geoloc) {
			//TODO we are not handle the geoloc message now
			
			/*if (geoloc.isImdnDisplayedRequested()) {
				if (!isInBackground) {
					// We received the message, mark it as displayed if the view is not in background
					markMessageAsDisplayed(geoloc);
				} else {
					// We save this message and will mark it as displayed when the activity resumes
					imReceivedInBackgroundToBeDisplayed.add(geoloc);
				}
			} else {
				if (!isInBackground) {
					// We received the message, mark it as read if the view is not in background
					markMessageAsRead(geoloc);
				} else {
					// We save this message and will mark it as read when the activity resumes
					imReceivedInBackgroundToBeRead.add(geoloc);
				}
			}
			
			handler.post(new Runnable() { 
				public void run() {
					displayReceivedMessage(geoloc);
				}
			});			*/
		}
    };



    /**
     * Client is connected to the IMS
     */
    @Override
	public void handleImsConnected() {
    	
    	Log.d(TAG, "Connection to the IMS");
	}

    /**
     * Client is disconnected from the IMS
     * 
     * @param reason Disconnection reason
     */
    @Override
	public void handleImsDisconnected(int reason) {
    	// IMS has been disconnected
		handler.post(new Runnable(){
			public void run(){
				Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_ims_disconnected));
			}
		});
	}



    /**
     * API connected
     */
    public void handleApiConnected() {
		handler.post(new Runnable() { 
			public void run() {
	    		try {
	    			// Add delivery listener 
	    			messagingApi.addMessageDeliveryListener(deliveryListener);
	    			
	    			// Test if there is an existing session
	    	        String sessionId = mActivity.getIntent().getStringExtra("sessionId");
	    			if (sessionId != null) {
	    				// Existing session
	    				
	    				// Get session
						chatSession = messagingApi.getChatSession(sessionId);

		    			// Register to receive session events
						if (chatSession == null) {
			    			Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_session_has_expired));
			    			return;
						}

						
		    			
		    			// Add session listener event
						chatSession.addSessionListener(chatSessionListener);
						
			            // Set list of participants
						participants = new ArrayList<String>(chatSession.getParticipants());
	    			} else {
	    				// New session
	    				
		    			// Set list of participants
		    	        participants = mActivity.getIntent().getStringArrayListExtra("participants");
		    	        if (participants == null) {
		    	            participants = new ArrayList<String>();
		    	            Contact friend = mActivity.getIntent().getExtras().getParcelable("friend");
		    	        	participants.add(friend.getPhoneNumber());
		    	        }
		    	        
		    	        // Init session
		    			initSession();
	    			}
	    		} catch(Exception e) {
	    			Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_api_failed));
	    		}
			}
		});
    }


    /**
     * Init session
     */
    public void initSession()
    {
    
    }

	@Override
	public void handleApiDisabled() {
		// TODO Auto-generated method stub
		
	}



	 /**
     * API disconnected
     */
	@Override
    public void handleApiDisconnected() {
		handler.post(new Runnable(){
			public void run(){
				Utils.showMessageAndExit(mActivity, mActivity.getString(R.string.label_api_disconnected));
			}
		});
    }  
}

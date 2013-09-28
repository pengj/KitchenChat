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

package com.joyn.kitchenchat.network;


import org.gsma.joyn.chat.ChatIntent;
import org.gsma.joyn.chat.ChatMessage;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.ui.ChatView;
import com.joyn.kitchenchat.ui.SingleChatView;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

/**
 * Chat invitation receiver
 * 
 * @author pengj
 */
public class ChatInvitationReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ChatInvitationReceiver.addSingleChatInvitationNotification(context, intent);
    }
	
	
    /**
     * Add chat notification
     * 
     * @param context Context
     * @param invitation Intent invitation
     */
    public static void addSingleChatInvitationNotification(Context context, Intent invitation) {
    	// Get remote contact
		String contact = invitation.getStringExtra(ChatIntent.EXTRA_CONTACT);

		// Get message		
		ChatMessage firstMessage = invitation.getParcelableExtra(ChatIntent.EXTRA_MESSAGE);		
		
		// Test if we are not already in the chat view
		if (!SingleChatView.isDisplayed()) {
	        // Create notification
			Intent intent = new Intent(invitation);
			intent.setClass(context, ChatView.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        intent.setAction(contact);
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        String notifTitle = context.getString(R.string.title_recv_chat, contact);
	        Notification notif = new Notification(R.drawable.ic_launcher, notifTitle, System.currentTimeMillis());
	        notif.flags = Notification.FLAG_AUTO_CANCEL;
	        String msg = firstMessage.getMessage();
	        notif.setLatestEventInfo(context, notifTitle, msg, contentIntent);
			notif.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    	notif.defaults |= Notification.DEFAULT_VIBRATE;
	        
	        // Send notification
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	        notificationManager.notify(contact, Utils.NOTIF_ID_SINGLE_CHAT, notif);
		}
    }
	
	  /**
     * Remove chat notification
     * 
     * @param context Context
     * @param contact Contact
     */
    public static void removeSingleChatNotification(Context context, String contact) {
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(contact, Utils.NOTIF_ID_SINGLE_CHAT);
    }
}

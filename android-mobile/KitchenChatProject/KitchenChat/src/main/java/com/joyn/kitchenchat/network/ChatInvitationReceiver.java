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

import com.joyn.kitchenchat.ui.Invit_Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Chat invitation receiver
 * 
 * @author pengj
 */
public class ChatInvitationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("RCS-TESt", "Receive a invitation");
		//boolean autoAccept = intent.getBooleanExtra("autoAccept", false);
		boolean autoAccept = false;
        if (autoAccept) {
            // Display chat
            Intent progress = new Intent(intent);
            //FIXME change the ReceiveChat with Receive Activity
            //progress.setClass(context, ReceiveChat.class);
            progress.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            progress.setAction(intent.getStringExtra("sessionId"));
            context.startActivity(progress);
        } else {
            // Display invitation notification
            //FIXME the Receive Activity
        	Log.d("RCS-TESt", "Receive a invitation");
        	Invit_Receiver.addChatInvitationNotification(context, intent);
        }
    }
}

package com.joyn.kitchenchat.ui;

import java.util.List;

import com.joyn.kitchenchat.R;
import com.joyn.kitchenchat.com.Consts;
import com.joyn.kitchenchat.network.Contact;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<Integer> {
	private Context context;
	private List<Integer> values;
	
	public ImageAdapter(Context context, List<Integer> values) {
		super(context, R.layout.image_list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.image_list_item, parent, false);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
		imageView.setImageResource(values.get(position));
		
		imageView.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				onStartChat ();
			}
			
		});
		
		return convertView;
	}
	
	public void onStartChat () {
		if(Consts.KITCHEN)
		{
			Contact friend = new Contact();
			friend.setName("User");
			friend.setPhoneNumber(Consts.MY_PHONE);
			Contact me = new Contact();
			me.setName("Kitchen");
			me.setPhoneNumber(Consts.KITCHEM_PHONE);
			Intent intent = new Intent(context, SingleChatView.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	intent.putExtra(SingleChatView.EXTRA_MODE, SingleChatView.MODE_OUTGOING);
	    	intent.putExtra(SingleChatView.EXTRA_CONTACT, friend.getPhoneNumber());
	    	context.startActivity(intent);	
		}else{
			
			Contact friend = new Contact();
			friend.setName("Kitchen");
			friend.setPhoneNumber(Consts.KITCHEM_PHONE);
			Contact me = new Contact();
			me.setName("Me");
			me.setPhoneNumber(Consts.MY_PHONE);
			Intent intent = new Intent(context, SingleChatView.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	intent.putExtra(SingleChatView.EXTRA_MODE, SingleChatView.MODE_OUTGOING);
	    	intent.putExtra(SingleChatView.EXTRA_CONTACT, friend.getPhoneNumber());
	    	context.startActivity(intent);	
			
		}
	}
}

package com.joyn.kitchenchat.ui;

import java.util.List;

import com.joyn.kitchenchat.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
		
		return convertView;
	}
}

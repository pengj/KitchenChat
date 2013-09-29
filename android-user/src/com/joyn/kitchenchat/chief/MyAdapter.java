package com.joyn.kitchenchat.chief;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.joyn.kitchenchat.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MyAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	ArrayList<HashMap<String, Integer>> itemsList;
	ImageView backcolor;
	ImageView backItem;
	int myposition;
	int count =0;
	int color =0;
	int count2 = 0;
	Map<Integer,ChangeColorItem> changeColor;
	public MyAdapter(Context context,ArrayList<HashMap<String, Integer>> itemsList,Map<Integer,ChangeColorItem> changeColor) {
		// TODO Auto-generated constructor stub
		mContext = context;
		this.changeColor = changeColor;
		this.itemsList = itemsList;
		 mInflater = LayoutInflater.from(mContext);
	}

	public void ViewHolder(){
		
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
			
		return position;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent	) {
		 LinearLayout layoutItem;
		 myposition = position;
		 if (convertView == null) {
			    layoutItem = (LinearLayout) mInflater.inflate(R.layout.item_chef_view, parent, false);
			  } else {
			  	layoutItem = (LinearLayout) convertView;
			  }
		 backcolor = (ImageView)layoutItem.findViewById(R.id.backcolor);
		 backcolor.setImageResource(itemsList.get(position).get("img1"));
		 backItem = (ImageView)layoutItem.findViewById(R.id.backitem);
		 backItem.setImageResource(itemsList.get(position).get("img2"));
		 
		return layoutItem;
	}
	
	

}

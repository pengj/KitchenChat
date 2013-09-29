package com.metaddev.haktcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ChefViewActivity extends Activity {
	ListView lv;
	Bitmap srcBitmap;
	ImageView img;
	int count =0;
	int color =0;
	int count2 = 0;
	Context context;
	 LayoutInflater inflater;
	 ImageView backcolor;
	Map<Integer,ChangeColorItem> changeColor = new HashMap<Integer,ChangeColorItem>();
	ArrayList<ChangeColorItem> colorchages = new ArrayList<ChangeColorItem>();
 	ArrayList<HashMap<String, Integer>> itemsList;
 	 View row;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef_view);
		itemsList = new ArrayList<HashMap<String, Integer>>();
		lv = (ListView)findViewById(R.id.listView1);
		context = this;
		lv.setCacheColorHint(0);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				row = view;
				if(row==null){
					   inflater=getLayoutInflater();
					   row=inflater.inflate(R.layout.item_chef_view, parent, false);
					  }
				 backcolor = (ImageView)row.findViewById(R.id.backcolor);
				  
				
				if(changeColor.get(position).getCount() == 0){
					color = R.drawable.queue_05;
					count2 = changeColor.get(position).getCount()+1;
					changeColor.get(position).setColor(color);
					changeColor.get(position).setCount(count2);
					changeColor.get(position).setPosition(position);
					
				}else if(changeColor.get(position).getCount() == 1){
					color = R.drawable.queue_07;
					count2 = changeColor.get(position).getCount()+1;
					changeColor.get(position).setColor(color);
					changeColor.get(position).setCount(count2);
					changeColor.get(position).setPosition(position);
				}else{
					//view.setBackgroundColor(Color.TRANSPARENT);
					color = R.drawable.queue_03;
					count2 = 0;
					changeColor.get(position).setColor(color);
					changeColor.get(position).setCount(count2);
					changeColor.get(position).setPosition(position);
				}
				backcolor.setImageResource(changeColor.get(position).getColor());
				backcolor.setBackgroundResource(changeColor.get(position).getColor());
				
			}
		});
		addItems();
	}

	public void addItems(){
		 HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("img1", R.drawable.queue_03);
		map.put("img2", R.drawable.queue2_02);
		changeColor.put(0, new ChangeColorItem(count,R.drawable.queue_03,0));
		itemsList.add(map);
		
		 map = new HashMap<String, Integer>();
		 map.put("img1",R.drawable.queue_03);
			map.put("img2", R.drawable.queue2_03);
			changeColor.put(1, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		
		 map = new HashMap<String, Integer>();
		 map.put("img1", R.drawable.queue_03);
			map.put("img2", R.drawable.queue2_04);
			changeColor.put(2, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		
		
		map = new HashMap<String, Integer>();
		map.put("img1", R.drawable.queue_03);
		map.put("img2", R.drawable.queue2_05);
		changeColor.put(3, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		
		
		map = new HashMap<String, Integer>();
		map.put("img1", R.drawable.queue_03);
		map.put("img2", R.drawable.queue2_06);
		changeColor.put(4, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		
		
		map = new HashMap<String, Integer>();
		map.put("img1", R.drawable.queue_03);
		map.put("img2", R.drawable.queue2_02);
		changeColor.put(5, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		
		
		map =new HashMap<String, Integer>();
		map.put("img1", R.drawable.queue_03);
		map.put("img2", R.drawable.queue2_07);
		changeColor.put(6, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		
		
		map = new HashMap<String, Integer>();
		map.put("img1", R.drawable.queue_03);
		map.put("img2", R.drawable.queue2_08);
		changeColor.put(7, new ChangeColorItem(count,R.drawable.queue_03,0));
			itemsList.add(map);
		addItemsToList();
	}
	
	public void addItemsToList(){
		SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), itemsList, R.layout.item_chef_view,
	               new String[] {"img1","img2"}, new int[] {R.id.backcolor, R.id.backitem});
	 
		mSchedule.setViewBinder(new MyViewBinder());
		MyAdapter adapter = new MyAdapter(context,itemsList, changeColor);

		
	        lv.setAdapter(adapter);
	}
	
	public class MyViewBinder implements ViewBinder, android.widget.SimpleAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			// TODO Auto-generated method stub
			
			return false;
		}

		@Override
		public boolean setViewValue(View view, Cursor arg1, int arg2) {
			
			return false;
		}
	    
		

		
	}
	
	

}

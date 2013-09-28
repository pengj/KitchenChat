package com.metaddev.haktcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
	TextView tx;
	int count =0;
	Map<Integer,ChangeColorItem> changeColor = new HashMap<Integer,ChangeColorItem>();
	ArrayList<ChangeColorItem> colorchages = new ArrayList<ChangeColorItem>();
 	ArrayList<HashMap<String, String>> itemsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef_view);
		itemsList = new ArrayList<HashMap<String, String>>();
		lv = (ListView)findViewById(R.id.listView1);
		tx = (TextView)findViewById(R.id.commentText);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int color =0;
				
				
				
				if(changeColor.get(position).getCount() == 0){
					color = Color.BLACK;
					changeColor.get(position).setCount(changeColor.get(position).getCount()+1);
					changeColor.get(position).setColor(color);
				}else if(changeColor.get(position).getCount() == 1){
					color = Color.GREEN;
					changeColor.get(position).setCount(changeColor.get(position).getCount()+1);
					changeColor.get(position).setColor(color);
				}else if(changeColor.get(position).getCount() == 2){
					color = Color.DKGRAY;
					changeColor.get(position).setCount(changeColor.get(position).getCount()+1);
					changeColor.get(position).setColor(color);
				}else{
					//view.setBackgroundColor(Color.TRANSPARENT);
					color = Color.TRANSPARENT;
					changeColor.get(position).setCount(0);
					changeColor.get(position).setColor(color);
					
				}
				view.setBackgroundColor(changeColor.get(position).getColor());
				
			}
		});
		addItems();
	}

	public void addItems(){
		 HashMap<String, String> map = new HashMap<String, String>();
		 map.put("title", "Pasta salad");
		 map.put("alergi1", String.valueOf(R.drawable.images_allergie));
		 map.put("alergi2", String.valueOf(R.drawable.images_allergie));
		 map.put("alergi3", String.valueOf(R.drawable.images_allergie));
		 map.put("commentText", "3");
		 changeColor.put(0, new ChangeColorItem(count, Color.TRANSPARENT,0));
		itemsList.add(map);
		
		 map = new HashMap<String, String>();
		 map.put("title", "Grnnsaker");
		 map.put("alergi1", String.valueOf(R.drawable.images_allergie));
		 map.put("alergi2", String.valueOf(R.drawable.images_allergie));
		 map.put("alergi3", String.valueOf(R.drawable.images_allergie));
		 map.put("commentText", "5");
		 changeColor.put(1, new ChangeColorItem(count, Color.TRANSPARENT,1));
		itemsList.add(map);
		
		 map = new HashMap<String, String>();
		 map.put("title", "Kylling");
		 map.put("alergi1", String.valueOf(R.drawable.images_allergie));
		 map.put("alergi2", String.valueOf(R.drawable.images_allergie));
		 map.put("alergi3", String.valueOf(R.drawable.images_allergie));
		 map.put("commentText", "2");
		 changeColor.put(2, new ChangeColorItem(count, Color.TRANSPARENT,2));
		itemsList.add(map);
		addItemsToList();
	}
	
	public void addItemsToList(){
		SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), itemsList, R.layout.item_chef_view,
	               new String[] {"alergi1", "alergi2", "alergi3","title","commentText"}, new int[] {R.id.alergi1, R.id.alergi2, R.id.alergi3,R.id.title,R.id.commentText});
	 
	        lv.setAdapter(mSchedule);
	}
	
	
	

}

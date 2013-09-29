package com.metaddev.haktcode;

public class ChangeColorItem {
	int count;
	int color;
	int position;
	
	public ChangeColorItem(int count, int color, int position){
		setCount(count);
		setColor(color);
		setPosition(position);
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
}

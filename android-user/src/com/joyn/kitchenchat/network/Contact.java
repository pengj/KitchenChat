package com.joyn.kitchenchat.network;


import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable{

	private String id;
	private String name;
	private String phoneNumber;

	public Contact(){
		
	}
	
	public Contact (Parcel in) {
		readFromParcel (in);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	 @SuppressWarnings("rawtypes")
		public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	        public Contact createFromParcel(Parcel in) {
	            return new Contact(in);
	        }
	 
	        public Contact[] newArray(int size) {
	            return new Contact[size];
	        }
	    };
	    
		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(getName());
			dest.writeString(getPhoneNumber());		
		}
		
		public void readFromParcel (Parcel in) {
			setName (in.readString());
			setPhoneNumber (in.readString());
		}

}

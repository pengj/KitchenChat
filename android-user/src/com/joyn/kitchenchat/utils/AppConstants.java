package com.joyn.kitchenchat.utils;

import android.app.Application;

public class AppConstants extends Application {
	private String userNickname = "Rebecca";

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
}

package com.joyn.kitchenchat.com.joyn.kitchenchat.ui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.joyn.kitchenchat.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
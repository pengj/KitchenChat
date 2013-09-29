package com.metaddev.haktcode;

import utils.IntentIntegrator;
import utils.IntentResult;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QRScannerActivity extends Activity {
	Button bn;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrscanner);
		bn = (Button)findViewById(R.id.button1);
		context = this;
		bn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntentIntegrator integrator = new IntentIntegrator(QRScannerActivity.this);
				integrator.initiateScan();
			}
		});
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
	   if (resultCode == RESULT_OK) {
			// get result from QR-scanner
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);
			
			// if result from scanner is not null, start activity for qr-result
			// result from qr-scan put as an extra
			if (scanResult != null) {
				Intent i = new Intent(this, MenuActivity.class);
				i.putExtra("url", scanResult.getContents().toString());
				
				startActivity(i);
			} else {
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
			}
			
		} else if (resultCode == RESULT_CANCELED) {
            // Handle cancel
            Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            
        }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrscanner, menu);
		return true;
	}

}

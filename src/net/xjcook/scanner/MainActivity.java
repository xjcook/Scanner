package net.xjcook.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanningResult != null) {
			String scanFormat = scanningResult.getFormatName();
			String scanContent = scanningResult.getContents();
			Toast.makeText(this, scanFormat, Toast.LENGTH_LONG).show();
			Toast.makeText(this, scanContent, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "No scan data received!", Toast.LENGTH_LONG).show();
		}
	}

	public void scanClick(View button) {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

}

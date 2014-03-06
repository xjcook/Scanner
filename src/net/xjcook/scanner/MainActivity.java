package net.xjcook.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.xjcook.scanner.TextEditDialogFragment.TextEditDialogListener;

public class MainActivity extends FragmentActivity 
	                      implements TextEditDialogListener {	
	
	public static final String EXTRA_FIRSTNAME = "net.xjcook.scanner.FIRSTNAME";
	public static final String EXTRA_LASTNAME = "net.xjcook.scanner.LASTNAME";
	public static final String EXTRA_ADDRESS = "net.xjcook.ADDRESS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			Toast.makeText(this, scanContent, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "No scan data received!", Toast.LENGTH_LONG).show();
		}
	}

	public void onScanClick(View button) {
//		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
//		scanIntegrator.initiateScan();
		
//		DialogFragment textEditFragment = new TextEditDialogFragment();
//		textEditFragment.show(getSupportFragmentManager(), "text_edit");
		
		DialogFragment textViewFragment = new TextViewDialogFragment();
		
		Bundle args = new Bundle();
		args.putString(EXTRA_FIRSTNAME, "James");
		args.putString(EXTRA_LASTNAME, "Cook");
		args.putString(EXTRA_ADDRESS, "Majerska 9C, 821 07 Bratislava");
		textViewFragment.setArguments(args);
		
		textViewFragment.show(getSupportFragmentManager(), "text_view");
	}

	@Override
	public void onSendClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
		
	}

}

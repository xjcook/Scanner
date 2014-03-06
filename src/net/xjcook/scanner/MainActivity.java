package net.xjcook.scanner;

import android.content.Intent;
import android.os.AsyncTask;
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
	
	public void onScanClick(View button) {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		
		if (scanningResult != null) {
			String barCode = scanningResult.getContents();
			new GetTask().execute(barCode);			
		} else {
			Toast.makeText(this, "No Barcode received!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onSendClick(DialogFragment dialog) {
		Bundle args = dialog.getArguments();
		new PostTask().execute(args);
	}
	
	public class GetTask extends AsyncTask<String, Void, Bundle> {

		@Override
		protected Bundle doInBackground(String... barCodes) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Bundle args) {
			if (args != null) {
				DialogFragment textViewFragment = new TextViewDialogFragment();
				textViewFragment.setArguments(args);
				textViewFragment.show(getSupportFragmentManager(), "text_view");
			} else {
				DialogFragment textEditFragment = new TextEditDialogFragment();
				textEditFragment.show(getSupportFragmentManager(), "text_edit");
			}
		}
		
	}
	
	public class PostTask extends AsyncTask<Bundle, Void, Void> {

		@Override
		protected Void doInBackground(Bundle... argBundles) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(getApplicationContext(), "Sent successfully", 
					Toast.LENGTH_SHORT).show();
		}
		
	}

}

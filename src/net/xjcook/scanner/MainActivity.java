package net.xjcook.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.xjcook.scanner.TextEditDialogFragment.TextEditDialogListener;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends FragmentActivity 
	                      implements TextEditDialogListener {
	
	private static final String SERVER = "http://xj.logeec.com/scanner";
	
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
	
	private class GetTask extends AsyncTask<String, Void, Bundle> {

		@Override
		protected Bundle doInBackground(String... barCode) {			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			Bundle args = null;

			try {
				// Encode url
				URI url = new URI(SERVER + "/request.php?q=" + 
						URLEncoder.encode(barCode[0], "UTF-8"));
				request.setURI(url);
				
				// Execute request
				HttpResponse response = httpClient.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				
				if (statusCode == HttpStatus.SC_OK) {
					InputStream iStream = response.getEntity().getContent();
					DocumentBuilderFactory dbFactory = 
							DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(iStream);			
					
					//doc.getDocumentElement().normalize();
					
					String firstName = doc.getElementById("firstname").getTextContent();
					String lastName = doc.getElementById("lastname").getTextContent();
					String address = doc.getElementById("address").getTextContent();
					
					args = new Bundle();
					args.putString(EXTRA_FIRSTNAME, firstName);
					args.putString(EXTRA_LASTNAME, lastName);
					args.putString(EXTRA_ADDRESS, address);
				}
			} catch (UnsupportedEncodingException e) {
				Log.getStackTraceString(e);
			} catch (URISyntaxException e) {
				Log.getStackTraceString(e);
			} catch (ClientProtocolException e) {
				Log.getStackTraceString(e);
			} catch (IOException e) {
				Log.getStackTraceString(e);
			} catch (ParserConfigurationException e) {
				Log.getStackTraceString(e);
			} catch (SAXException e) {
				Log.getStackTraceString(e);
			}
			
			return args;
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
	
	private class PostTask extends AsyncTask<Bundle, Void, Void> {

		@Override
		protected Void doInBackground(Bundle... args) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			
			try {
				// Encode url
				URI url = new URI(SERVER + "/request.php");
				request.setURI(url);
				
				// Build XML request
				StringEntity sEntity = new StringEntity(
					"<scan>"
						+ "<detail id='firstname'>" 
							+ args[0].getString(EXTRA_FIRSTNAME) 
						+ "</detail>"
						+ "<detail id='lastname'>"
							+ args[0].getString(EXTRA_LASTNAME)
						+ "</detail>"
						+ "<detail id='address'>"
							+ args[0].getString(EXTRA_ADDRESS)
						+ "</detail>"
					+ "</scan>", "UTF-8");
				sEntity.setContentType("text/xml");
				request.setEntity(sEntity);
						
				// Execute request
				HttpResponse response = httpClient.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				
				if (statusCode != HttpStatus.SC_OK) {
					cancel(true);
				} 
			} catch (UnsupportedEncodingException e) {
				Log.getStackTraceString(e);
			} catch (URISyntaxException e) {
				Log.getStackTraceString(e);
			} catch (ClientProtocolException e) {
				Log.getStackTraceString(e);
			} catch (IOException e) {
				Log.getStackTraceString(e);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(getApplicationContext(), "Sent successfully", 
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onCancelled(Void result) {
			Toast.makeText(getApplicationContext(), "Sent unsuccessfully", 
					Toast.LENGTH_LONG).show();
		}
		
	}

}

package net.xjcook.scanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TextViewDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		String barCode = args.getString(MainActivity.EXTRA_BARCODE);
		String firstName = args.getString(MainActivity.EXTRA_FIRSTNAME);
		String lastName = args.getString(MainActivity.EXTRA_LASTNAME);
		String address = args.getString(MainActivity.EXTRA_ADDRESS);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(barCode)
			   .setMessage("Firstname: " + firstName
					       + "\nLastname: " + lastName
						   + "\nAdress: " + address);
		
		return builder.create();
	}

}

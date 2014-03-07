package net.xjcook.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class TextEditDialogFragment extends DialogFragment {

	public interface TextEditDialogListener {
		public void onSendClick(DialogFragment dialog, Bundle args);
	}
	
	View mDialogView;
	TextEditDialogListener mListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Verify that the host activity implements the callback interface
		try {
			mListener = (TextEditDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement TextEditDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mDialogView = inflater.inflate(R.layout.dialog_text_edit, null);

		// Inflate and set the layout for the dialog
		builder.setView(mDialogView)
			.setTitle(R.string.textedit_dialog_title)
			.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Get filled data
					EditText firstNameEdit = (EditText) mDialogView
							.findViewById(R.id.firstname_edit_text);
					EditText lastNameEdit = (EditText) mDialogView
							.findViewById(R.id.lastname_edit_text);
					EditText addressEdit = (EditText) mDialogView
							.findViewById(R.id.address_edit_text);
					
					// Save data
					Bundle args = new Bundle();
					args.putString(MainActivity.EXTRA_FIRSTNAME, 
							firstNameEdit.getText().toString());
					args.putString(MainActivity.EXTRA_LASTNAME, 
							lastNameEdit.getText().toString());
					args.putString(MainActivity.EXTRA_ADDRESS, 
							addressEdit.getText().toString());
					
					mListener.onSendClick(TextEditDialogFragment.this, args);
				}
				
			})
			.setNegativeButton(R.string.cancel, null);
		
		return builder.create();
	}
	
}

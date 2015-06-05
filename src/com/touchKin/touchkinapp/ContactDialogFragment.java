package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.Validation;
import com.touchKin.touckinapp.R;

public class ContactDialogFragment extends DialogFragment implements
		OnClickListener {
	static final int PICK_CONTACT_CIRCLE = 1;
	static final int PICK_CONTACT_KIN = 2;
	Button addContactButton;
	EditText nameBox, phoneBox, nickname;
	ButtonClickListener listener;
	ProgressDialog proDialog;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog
		// layout
		// Bundle mArgs = getArguments();

		View view = inflater.inflate(R.layout.contact_info, null);
		proDialog = new ProgressDialog(getActivity());
		proDialog.setMessage("Sending your request");
		proDialog.setCancelable(false);
		Button addContactButton = (Button) view.findViewById(R.id.addButton);
		nameBox = (EditText) view.findViewById(R.id.name);
		phoneBox = (EditText) view.findViewById(R.id.number);
		nickname = (EditText) view.findViewById(R.id.nickname);
		// nameBox.setText(mArgs.getString("name"));
		// phoneBox.setText(mArgs.getString("number"));
		View headerview = inflater.inflate(R.layout.header_view, null);
		// final TextView title = (TextView) headerview
		// .findViewById(R.id.parentNameTV);
		// title.setText(mArgs.getString("title"));
		nickname.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
						|| (actionId == EditorInfo.IME_ACTION_DONE)) {
					// Toast.makeText(MainActivity.this, "enter press",
					// Toast.LENGTH_LONG).show();
					addParent();
				}
				return false;
			}
		});
		nameBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		phoneBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		builder.setCancelable(false);
		builder.setView(view)
				// Add action buttons
				.setCustomTitle(headerview)
				.setIcon(R.drawable.ic_action_uset)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// sign in the user ...
							}

						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ContactDialogFragment.this.getDialog().cancel();
								DashBoardActivity.isCancel = true;
							}
						});
		final AlertDialog dialog = builder.create();
		dialog.show();
		Button positiveButton = (Button) dialog
				.getButton(Dialog.BUTTON_POSITIVE);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean wantToCloseDialog = false;
				// Do stuff, possibly set wantToCloseDialog to true
				// then...

				if (wantToCloseDialog)
					dismiss();
				addParent();
				// else dialog stays open. Make sure you have an obvious
				// way to close the dialog especially if you set
				// cancellable to false.
			}
		});

		addContactButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchContact(PICK_CONTACT_CIRCLE);
			}
		});
		return dialog;
	}

	private void addCareReciever(String name, String phone, String nickname) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		try {
			showpDialog();
			params.put("mobile", phone);
			params.put("nickname", nickname);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/add-care-receiver", params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("care receiver added response",
								"care reveiver added");
						hidepDialog();
						Toast.makeText(getActivity(), "Request is sent",
								Toast.LENGTH_SHORT).show();
						listener.onButtonClickListner(0, "", true);
						ContactDialogFragment.this.getDialog().cancel();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String json = null;

						NetworkResponse response = error.networkResponse;

						if (response != null && response.data != null) {
							int code = response.statusCode;
							json = new String(response.data);
							// json = trimMessage(json, "message");
							// if (json != null)
							// displayMessage(json, code);

						}

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void fetchContact(int reqCode) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, reqCode);
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT_CIRCLE):
			if (resultCode == Activity.RESULT_OK) {
				Bundle args = new Bundle();
				List<String> contact = getContact(data.getData());
				if (contact.size() > 1) {
					nameBox.setText(contact.get(0));
					// if(contact.get(1).matches("[0-9]+") &&
					// contact.get(1).length() == 9){
					// phoneBox.setText("+91"+contact.get(1));
					// }
					// if(contact.get(1).matches("[0-9]+") &&
					// contact.get(1).length() == 12){
					// phoneBox.setText(contact.get(1));
					// }
					// if(contact.get(1).matches("[0-9]+") &&
					// contact.get(1).length() > 7){
					String number = contact.get(1).substring(1)
							.replaceAll(" ", "");
					if (contact.get(1).startsWith("0")) {
						phoneBox.setText("+91" + number);

					} else if (contact.get(1).startsWith("+91")) {
						phoneBox.setText(number);
					} else {
						phoneBox.setText("+91" + number);
					}

					// }
					// args.putString("number", contact.get(1));
					// args.putString("name", contact.get(0));
					// args.putString("title", "Add care reciever");
					// DialogFragment newFragment = new ContactDialogFragment();
					// newFragment.setArguments(args);
					// newFragment.setCancelable(false);
					// newFragment.show(getSupportFragmentManager(), "TAG");
				}
			}

			break;
		case (PICK_CONTACT_KIN):
			if (resultCode == Activity.RESULT_OK) {
				Bundle args = new Bundle();
				List<String> contact = getContact(data.getData());

				if (contact.size() > 1) {
					args.putString("number", contact.get(1));
					args.putString("name", contact.get(0));
					args.putString("title", "Add Kin");
					DialogFragment newFragment = new ContactDialogFragment();
					newFragment.setArguments(args);
					newFragment.setCancelable(false);
					// newFragment.show(getSupportFragmentManager(), "TAG");
				}
			}

			break;
		}
	}

	List<String> getContact(Uri contactData) {
		// Bundle args = new Bundle();
		String cNumber = null;
		List<String> contact = new ArrayList<String>();
		// Uri contactData = data.getData();
		@SuppressWarnings("deprecation")
		Cursor c = getActivity().managedQuery(contactData, null, null, null,
				null);
		if (c.moveToFirst()) {

			String id = c.getString(c
					.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

			String hasPhone = c
					.getString(c
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone.equalsIgnoreCase("1")) {
				Cursor phones = getActivity().getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				phones.moveToFirst();
				cNumber = phones.getString(phones.getColumnIndex("data1"))
						.trim();
				System.out.println("number is:" + cNumber);
				Log.d("Number", cNumber);
				// args.putString("number", cNumber);

			} else {
				Log.d("Number", "No Number");
			}
			String name = c.getString(c
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Log.d("Name", name);
			contact.add(name);
			// args.putString("name", name);
			// args.putString("title", "Add care reciever");
			if (cNumber != null) {
				// args.putString("number", cNumber);
				contact.add(cNumber);
				// DialogFragment newFragment = new ContactDialogFragment();
				// newFragment.setArguments(args);
				// newFragment.setCancelable(false);
				// newFragment.show(getSupportFragmentManager(), "TAG");
			} else {
				Toast.makeText(getActivity(),
						"Contact does not contain mobile Number",
						Toast.LENGTH_LONG).show();
			}

		}
		return contact;
	}

	public void SetButtonListener(ButtonClickListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	private void showpDialog() {
		if (!proDialog.isShowing())
			proDialog.show();
	}

	private void hidepDialog() {
		if (proDialog.isShowing())
			proDialog.dismiss();
	}

	private void addParent() {
		// TODO Auto-generated method stub
		String phoneNum = phoneBox.getText().toString();
		if (Validation.hasText(nickname) && Validation.hasText(phoneBox)
				&& Validation.hasText(nameBox)) {
			if (!phoneNum.startsWith("+91")) {
				if (!phoneNum.startsWith("0")) {
					phoneNum = "+91" + phoneNum;
				} else {
					phoneNum = "+91" + phoneNum.substring(1);
				}
			}
			addCareReciever(nameBox.getText().toString(), phoneNum.trim(),
					nickname.getText().toString());
		}
	}

}

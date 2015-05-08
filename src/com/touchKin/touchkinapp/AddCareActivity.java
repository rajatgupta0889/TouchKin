package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.AddCirlceAdapter;
import com.touchKin.touchkinapp.model.AddCircleModel;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.Validation;
import com.touchKin.touckinapp.R;

public class AddCareActivity extends ActionBarActivity implements
		ButtonClickListener, OnClickListener {
	private Toolbar toolbar;
	TextView mTitle,notifTv;
	ListView careListView;
	AddCirlceAdapter adapter;
	List<AddCircleModel> circleList;
	ImageButton addContactButton, skip;
	static final int PICK_CONTACT_CIRCLE = 1;
	static final int PICK_CONTACT_KIN = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.care_add_activity);
		init();
		adapter.setCustomButtonListner(this);
		getCareList();
		mTitle.setText("Who do you care for");
		skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AddCareActivity.this,
						BluetoothScan.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
			}
		});
		// back.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(AddCareActivity.this,
		// CircleNotificationActivity.class);
		// Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
		// getApplicationContext(), R.anim.animation,
		// R.anim.animation2).toBundle();
		// startActivity(i, bndlanimation);
		// }
		// });
		addContactButton.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		careListView = (ListView) findViewById(R.id.careListView);
		circleList = new ArrayList<AddCircleModel>();
		adapter = new AddCirlceAdapter(circleList, this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		skip = (ImageButton) findViewById(R.id.skipTV);
		// back = (TextView) findViewById(R.id.back);
		addContactButton = (ImageButton) findViewById(R.id.addButton);
		notifTv = (TextView) findViewById(R.id.notifTV);
	}

	private void getCareList() {
		// TODO Auto-generated method stub
		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/user/care-receivers",
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						if (responseArray.length() > 0) {
							for (int i = 0; i < responseArray.length(); i++) {
								try {
									AddCircleModel item = new AddCircleModel();
									JSONObject careReciever = responseArray
											.getJSONObject(i);
									item.setUserId(careReciever.getString("id"));
									item.setUserName((careReciever
											.getString("nickname")));
									//
									circleList.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							Toast.makeText(AddCareActivity.this, "NO Request",
									Toast.LENGTH_SHORT).show();
							notifTv.setVisibility(View.VISIBLE);

						}
						adapter.notifyDataSetChanged();
						careListView.setAdapter(adapter);

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(AddCareActivity.this,
								error.getMessage(), Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		if (isAccept) {
			Toast.makeText(AddCareActivity.this, "Add Kin Button Clicked",
					Toast.LENGTH_SHORT).show();
			fetchContact(PICK_CONTACT_KIN);

		} else {
			Toast.makeText(AddCareActivity.this, "Remove Button Clicked",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addButton:
			fetchContact(PICK_CONTACT_CIRCLE);
			break;

		default:
			break;
		}
	}

	private void fetchContact(int reqCode) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, reqCode);
	}

	// code
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT_CIRCLE):
			if (resultCode == Activity.RESULT_OK) {
				Bundle args = new Bundle();
				List<String> contact = getContact(data.getData());
				if (contact.size() > 1) {
					args.putString("number", contact.get(1));
					args.putString("name", contact.get(0));
					args.putString("title", "Add care reciever");
					DialogFragment newFragment = new ContactDialogFragment();
					newFragment.setArguments(args);
					newFragment.setCancelable(false);
					newFragment.show(getSupportFragmentManager(), "TAG");
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
					newFragment.show(getSupportFragmentManager(), "TAG");
				}
			}

			break;
		}
	}

	public class ContactDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// Get the layout inflater
			LayoutInflater inflater = getActivity().getLayoutInflater();

			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			Bundle mArgs = getArguments();

			View view = inflater.inflate(R.layout.contact_info, null);
			final EditText nameBox = (EditText) view.findViewById(R.id.name);
			final EditText phoneBox = (EditText) view.findViewById(R.id.number);
			final EditText nickname = (EditText) view
					.findViewById(R.id.nickname);
			nameBox.setText(mArgs.getString("name"));
			phoneBox.setText(mArgs.getString("number"));
			View headerview = inflater.inflate(R.layout.header_view, null);
			final TextView title = (TextView) headerview
					.findViewById(R.id.parentNameTV);
			title.setText(mArgs.getString("title"));
			builder.setCancelable(false);
			builder.setView(view)
					// Add action buttons
					.setCustomTitle(headerview)
					.setIcon(R.drawable.ic_action_uset)
					.setPositiveButton("Add",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// sign in the user ...
								}

							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									ContactDialogFragment.this.getDialog()
											.cancel();
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
					if (Validation.hasText(nickname)) {
						addCareReciever(nameBox.getText().toString(), phoneBox
								.getText().toString(), nickname.getText()
								.toString());
						ContactDialogFragment.this.getDialog().cancel();
					}
					// else dialog stays open. Make sure you have an obvious
					// way to close the dialog especially if you set
					// cancellable to false.
				}
			});

			return dialog;
		}

	}

	private void addCareReciever(String name, String phone, String nickname) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();
		try {
			params.put("mobile", phone);
			params.put("nickname", nickname);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/add-care-receiver", params,
				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					@Override
					public void onResponse(JSONObject response) {
						Toast.makeText(AddCareActivity.this,
								"Your Request is sent", Toast.LENGTH_LONG)
								.show();
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

	List<String> getContact(Uri contactData) {
		// Bundle args = new Bundle();
		String cNumber = null;
		List<String> contact = new ArrayList<String>();
		// Uri contactData = data.getData();
		Cursor c = managedQuery(contactData, null, null, null, null);
		if (c.moveToFirst()) {

			String id = c.getString(c
					.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

			String hasPhone = c
					.getString(c
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone.equalsIgnoreCase("1")) {
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				phones.moveToFirst();
				cNumber = phones.getString(phones.getColumnIndex("data1"));
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
				Toast.makeText(AddCareActivity.this,
						"Contact does not contain mobile Number",
						Toast.LENGTH_LONG).show();
			}

		}
		return contact;
	}
}

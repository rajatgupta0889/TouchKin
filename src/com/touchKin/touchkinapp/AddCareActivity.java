package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.AddCirlceAdapter;
import com.touchKin.touchkinapp.model.AddCircleModel;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touckinapp.R;

public class AddCareActivity extends ActionBarActivity implements
		ButtonClickListener, OnClickListener {
	private Toolbar toolbar;
	TextView mTitle, skip, back;
	ListView careListView;
	AddCirlceAdapter adapter;
	List<AddCircleModel> circleList;
	ImageButton addContactButton;
	static final int PICK_CONTACT = 1;

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
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AddCareActivity.this,
						CircleNotificationActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(i, bndlanimation);
			}
		});
		addContactButton.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		careListView = (ListView) findViewById(R.id.careListView);
		circleList = new ArrayList<AddCircleModel>();
		adapter = new AddCirlceAdapter(circleList, this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		skip = (TextView) findViewById(R.id.skipTV);
		back = (TextView) findViewById(R.id.back);
		addContactButton = (ImageButton) findViewById(R.id.addButton);
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
			Toast.makeText(AddCareActivity.this, "Accept Button Clicked",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(AddCareActivity.this, "Reject Button Clicked",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addButton:
			fetchContact();
			break;

		default:
			break;
		}
	}

	private void fetchContact() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);
	}

	// code
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Bundle args = new Bundle();

				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {

					String id = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

					String hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						phones.moveToFirst();
						String cNumber = phones.getString(phones
								.getColumnIndex("data1"));
						System.out.println("number is:" + cNumber);
						Log.d("Number", cNumber);
						args.putString("number", cNumber);

					} else {
						Log.d("Number", "No Number");
					}
					String name = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					Log.d("Name", name);
					args.putString("name", name);
					DialogFragment newFragment = new ContactDialogFragment();
					newFragment.setArguments(args);
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

			builder.setView(view)
					// Add action buttons
					.setPositiveButton("Ok",
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
			return builder.create();
		}
	}
}

package com.touchKin.touchkinapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.Validation;
import com.touchKin.touckinapp.R;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SignUpActivity extends ActionBarActivity {

	String[] country;
	String[] code;
	Spinner spinner;
	Button otp_button;
	private EditText phone_number;
	private ProgressDialog pDialog;
	String areaCode;
	String phoneNumber;
	private static String TAG = SignUpActivity.class.getSimpleName();
	private Toolbar toolbar;
	TextView mTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_activity);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		country = getResources().getStringArray(R.array.list_name);
		code = getResources().getStringArray(R.array.country_code);
		mTitle.setText("Login");
		Spinner spinner = (Spinner) findViewById(R.id.spinner);

		otp_button = (Button) findViewById(R.id.otp_Genrate_Button);
		phone_number = (EditText) findViewById(R.id.phone_number);
		spinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner, country));
		spinner.setSelection(17);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				int index = parent.getSelectedItemPosition();
				areaCode = code[index];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		otp_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (checkValidation()) {
					// new LongOperation().execute("");
					sendIntent();
				} else {
					Toast.makeText(SignUpActivity.this, " contains error",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		phone_number.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Validation.isPhoneNumber(phone_number, false);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		phone_number.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
						|| (actionId == EditorInfo.IME_ACTION_DONE)) {
					// Toast.makeText(MainActivity.this, "enter press",
					// Toast.LENGTH_LONG).show();
					if (checkValidation()) {
						sendIntent();
					} else {
						Toast.makeText(SignUpActivity.this, " contains error",
								Toast.LENGTH_LONG).show();
					}
				}
				return false;
			}
		});
	}

	/*
	 * private class LongOperation extends AsyncTask<String, Void, String> {
	 * 
	 * @Override protected String doInBackground(String... params) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * 
	 * Log.d("value os", "wow nothing happen"); if (result != null) { Intent i =
	 * new Intent(MainActivity.this, Login.class); Bundle bndlanimation =
	 * ActivityOptions.makeCustomAnimation( getApplicationContext(),
	 * R.anim.animation, R.anim.animation2).toBundle(); startActivity(i,
	 * bndlanimation); } // might want to change "executed" for the returned
	 * string // passed // into onPostExecute() but that is upto you }
	 * 
	 * @Override protected void onPreExecute() { }
	 * 
	 * @Override protected void onProgressUpdate(Void... values) { } }
	 */

	private boolean checkValidation() {
		boolean ret = true;
		if (!Validation.isPhoneNumber(phone_number, true)) {
			ret = false;
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	@SuppressLint("NewApi")
	public void sendIntent() {
		// Intent i = new Intent(SignUpActivity.this,
		// DashBoardActivity.class);
		// Bundle bndlanimation = ActivityOptions
		// .makeCustomAnimation(getApplicationContext(),
		// R.anim.animation, R.anim.animation2)
		// .toBundle();
		// startActivity(i,bndlanimation);
		//

		showpDialog();
		phoneNumber = areaCode + phone_number.getText().toString();
		JSONObject params = new JSONObject();
		try {
			params.put("mobile", phoneNumber);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/user/signup", params,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Intent i = new Intent(SignUpActivity.this,
								OtpRequestActivity.class);
						Bundle bndlanimation = ActivityOptions
								.makeCustomAnimation(getApplicationContext(),
										R.anim.animation, R.anim.animation2)
								.toBundle();
						i.putExtra("phoneNumber", phoneNumber);
						startActivity(i, bndlanimation);

						// Log.d(TAG, response.toString());
						// VolleyLog.v("Response:%n %s",
						// response.toString(4));
						hidepDialog();
						finish();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("Error", "" + error.networkResponse);
						VolleyLog.e("Error: ", error.getMessage());

						hidepDialog();
						NetworkResponse response = error.networkResponse;
//						Log.d("Response", response.data.toString());
						if (response != null && response.data != null) {
							switch (response.statusCode) {
							case 400:
								Intent i = new Intent(SignUpActivity.this,
										OtpRequestActivity.class);
								Bundle bndlanimation = ActivityOptions
										.makeCustomAnimation(
												getApplicationContext(),
												R.anim.animation,
												R.anim.animation2).toBundle();
								i.putExtra("phoneNumber", phoneNumber);
								startActivity(i, bndlanimation);
								finish();
								break;
							}
							// Additional cases
						}
					}

				});

		AppController.getInstance().addToRequestQueue(req);
	}

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
			super(ctx, txtViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView1(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
					false);
			TextView main_text = (TextView) mySpinner
					.findViewById(R.id.text_main_seen);
			main_text.setText(country[position]);

			TextView subSpinner = (TextView) mySpinner
					.findViewById(R.id.sub_text_seen);
			subSpinner.setText(code[position]);

			return mySpinner;
		}

		public View getCustomView1(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.custom_spinner1, parent,
					false);

			TextView subSpinner = (TextView) mySpinner
					.findViewById(R.id.sub_text_seen);
			subSpinner.setText(code[position]);

			return mySpinner;
		}
	}

}

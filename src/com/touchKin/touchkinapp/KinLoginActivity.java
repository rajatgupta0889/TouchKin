package com.touchKin.touchkinapp;

import com.touchKin.touckinapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class KinLoginActivity extends Activity implements OnClickListener {
	EditText kinMobileNo;
	Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kin_login);

	}

	void init() {
		kinMobileNo = (EditText) findViewById(R.id.mobileNoEditText);
		loginButton = (Button) findViewById(R.id.loginButton);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.loginButton:
			userLogin();
			break;

		default:
			break;
		}

	}

	private void userLogin() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, KinVerifyActivity.class);
		startActivity(intent);
	}
}

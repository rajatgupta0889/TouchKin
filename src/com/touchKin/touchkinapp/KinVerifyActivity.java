package com.touchKin.touchkinapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.touchKin.touckinapp.R;

public class KinVerifyActivity extends Activity implements OnClickListener {
	EditText kinVerifyCode;
	Button verifyButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify_number);

	}

	void init() {
		kinVerifyCode = (EditText) findViewById(R.id.verifyEditText);
		verifyButton = (Button) findViewById(R.id.verifyButton);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.loginButton:
			verifyUser();
			break;

		default:
			break;
		}

	}

	private void verifyUser() {
		// TODO Auto-generated method stub

	}

}

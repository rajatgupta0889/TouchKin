package com.touchKin.touchkinapp;

import com.skyfishjy.library.RippleBackground;
import com.touchKin.touckinapp.R;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class BluetoothScan extends ActionBarActivity {

	Button scan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_scan);
		final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
		final Handler handler = new Handler();
		scan = (Button) findViewById(R.id.bluetooth_scan);
		scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rippleBackground.startRippleAnimation();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// foundDevice();
						rippleBackground.stopRippleAnimation();

						Intent i = new Intent(BluetoothScan.this,
								DashBoardActivity.class);
						Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
								getApplicationContext(), R.anim.animation,
								R.anim.animation2).toBundle();
						startActivity(i, bndlanimation);
					}
				}, 6000);
			}
		});
	}

}

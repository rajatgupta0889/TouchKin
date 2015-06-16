package com.touchKin.touchkinapp;

import com.touchKin.touckinapp.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoFullScreen extends ActionBarActivity implements
		OnClickListener {
	Uri videopath;
	VideoView videoscreen;
	Bitmap thumbnail;
//	private ProgressDialog pDialog;
	ProgressBar progressbarofvideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_preview);

//		pDialog = new ProgressDialog(this);
//		pDialog.setMessage("Please wait...");
//		pDialog.setCancelable(false);
		
		progressbarofvideo = (ProgressBar)findViewById(R.id.progressbarofvideo);

		videoscreen = (VideoView) findViewById(R.id.videopreview);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		mTitle.setText("");
		toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NavUtils.navigateUpFromSameTask(VideoFullScreen.this);
			}
		});
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		videopath = (Uri) bundle.get("videopath");
//		showpDialog();

		videoscreen.setVideoURI(videopath);
		videoscreen.setMediaController(new MediaController(this));

		videoscreen.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
//				hidepDialog();
				progressbarofvideo.setVisibility(View.INVISIBLE);
				videoscreen.start();
			}
		});
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

//	private void showpDialog() {
//		if (!pDialog.isShowing())
//			pDialog.show();
//	}
//
//	private void hidepDialog() {
//		if (pDialog.isShowing())
//			pDialog.dismiss();
//	}

}

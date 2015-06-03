package com.touchKin.touchkinapp;

import com.touchKin.touckinapp.R;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.VideoView;

public class VideoFullScreen extends ActionBarActivity implements
		OnClickListener {
	Uri videopath;
	VideoView videoscreen;
	Bitmap thumbnail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_preview);
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

		videoscreen.setVideoURI(videopath);
		videoscreen.setMediaController(new MediaController(this));

		videoscreen.start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}

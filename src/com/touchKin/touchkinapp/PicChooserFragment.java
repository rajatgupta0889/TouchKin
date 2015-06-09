package com.touchKin.touchkinapp;

import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touckinapp.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PicChooserFragment extends DialogFragment implements
		OnClickListener {
	ImageView gallery, camera;
	ButtonClickListener listener;
	private Intent pictureActionIntent = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.profile_pick_chooser, null);
		// proDialog = new ProgressDialog(getActivity());
		// proDialog.setMessage("Sending your request");
		// proDialog.setCancelable(false);
		gallery = (ImageView) view.findViewById(R.id.gallery_choose);
		camera = (ImageView) view.findViewById(R.id.camera_choose);
		View headerview = inflater.inflate(R.layout.header_view_pic_chooser, null);
		// final TextView title = (TextView) headerview
		// .findViewById(R.id.parentNameTV);
		// title.setText(mArgs.getString("title"));
		gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pictureActionIntent = new Intent(Intent.ACTION_PICK,
						null);
				pictureActionIntent.setType("image/*");
				pictureActionIntent.putExtra("return-data", true);
				startActivityForResult(pictureActionIntent, Details.GALLERY_PICTURE);
				PicChooserFragment.this.getDialog().cancel();

			}
		});
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				pictureActionIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(pictureActionIntent, Details.CAMERA_REQUEST);
				PicChooserFragment.this.getDialog().cancel();
			}
		});

		builder.setCancelable(false);
		builder.setView(view)
				// Add action buttons
				.setCustomTitle(headerview)
//				.setIcon(R.drawable.ic_action_uset)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								PicChooserFragment.this.getDialog().cancel();
							}
						});
		final AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void SetButtonListener(ButtonClickListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;

	}

}

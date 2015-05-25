package com.touchKin.touchkinapp.adapter;

import java.net.URI;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.touchKin.touchkinapp.DashBoardActivity;
import com.touchKin.touchkinapp.VideoPlayerManual;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.TouchKinBookModel;
import com.touchKin.touckinapp.R;

public class FlipViewAdapter extends BaseAdapter {
	List<TouchKinBookModel> touckinBook;
	Context context;
	LayoutInflater inflater;
	// List<TouchKinComments> comments;
	// CommentListAdapter adapter;
	TouchKinBookModel touchKinBook;
	ButtonClickListener customListener;
	TextView videoText, videoTime, videoDay, videoSenderName, videoViewCount;
	RoundedImageView userImage, videoSenderImage;
	// EditText commentEditText;
	Button profilepic = null, backbutton, likebutton;
	VideoView videoView = null;
	ImageView imageView;
	Bitmap thumbnail;

	public FlipViewAdapter(List<TouchKinBookModel> touckinBook, Context context) {
		super();
		this.touckinBook = touckinBook;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void setCustomButtonListner(ButtonClickListener listener) {
		this.customListener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return touckinBook.size();
	}

	@Override
	public TouchKinBookModel getItem(int position) {
		// TODO Auto-generated method stub
		return touckinBook.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// private static class ViewHolder {
	// TextView videoText, videoTime, videoDay, videoSenderName,
	// videoViewCount;
	// RoundedImageView userImage, videoSenderImage;
	// // EditText commentEditText;
	// Button profilepic, backbutton, likebutton;
	// VideoView videoView;
	// // ListView commentList;
	// }
	// final ViewHolder viewHolder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.flipview_layout, null);
			// viewHolder = new ViewHolder();
			videoText = (TextView) convertView
					.findViewById(R.id.videoCommentTextView);
			// videoText = (TextView) convertView
			// .findViewById(R.id.videoCommentTextView);
			videoTime = (TextView) convertView
					.findViewById(R.id.videoTimeTextView);
			videoDay = (TextView) convertView
					.findViewById(R.id.videoDayTextView);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			// viewHolder.userImage = (RoundedImageView) convertView
			// .findViewById(R.id.userImage);
			// videoSenderName = (TextView) convertView
			// .findViewById(R.id.videoSenderNameTextView);
			// videoViewCount = (TextView) convertView
			// .findViewById(R.id.videoSeenCountTextView);

			videoSenderImage = (RoundedImageView) convertView
					.findViewById(R.id.senderImage);
			videoView = (VideoView) convertView.findViewById(R.id.videoView);
			profilepic = (Button) convertView.findViewById(R.id.profile_pic);
			// backbutton = (Button) convertView.findViewById(R.id.back_button);
			likebutton = (Button) convertView.findViewById(R.id.like_button);
			// viewHolder.commentEditText = (EditText) convertView
			// .findViewById(R.id.commentEditText);

			// viewHolder.commentList = (ListView) convertView
			// .findViewById(R.id.commentList);

			// convertView.setTag(viewHolder);
		}

		final TouchKinBookModel touchKinBook = getItem(position);
		//
		// thumbnail = ThumbnailUtils.createVideoThumbnail(,
		// Thumbnails.MICRO_KIND);
		// // BitmapDrawable drawable = new BitmapDrawable(thumbnail);
		// Toast.makeText(context, "  " + thumbnail, Toast.LENGTH_LONG).show();
		// // videoView.setBackgroundDrawable(drawable);
		// imageView.setImageBitmap(thumbnail);
		// String url =
		// "https://archive.org/download/ksnn_compilation_master_the_internet/"
		// + touchKinBook.getVideoUrl();
		// Log.d("url", url);

		// MediaController vidControl = new MediaController(context);
		// vidControl.setAnchorView(viewHolder.videoView);
		// viewHolder.videoView.setMediaController(vidControl);

		// Uri videouri = Uri.parse(url);
		Log.d("videouri", "" + touchKinBook.getVideouri());
		// viewHolder.videoView.setVideoURI(videouri);

		profilepic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// //
				// videoView.setVideoURI(getItem(position).getVideouri());
				// videoView.requestFocus();
				// // viewHolder.videoView.setMediaController(new
				// MediaController(
				// // context));
				//
				// // String url =
				// //
				// "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
				// // Toast.makeText(context, String.valueOf(position),
				// // Toast.LENGTH_SHORT).show();
				// if (videoView.isPlaying()) {
				// videoView.stopPlayback();
				// } else {
				// videoView.start();
				//
				// }
				// Toast.makeText(context, "" + position, Toast.LENGTH_LONG)
				// .show();
				// Log.d("videouri", "" + getItem(position).getVideouri());
				Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.video_player);

				// WindowManager.LayoutParams layoutparams = new
				// WindowManager.LayoutParams(
				// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				// layoutparams.copyFrom(dialog.getWindow().getAttributes());
				// dialog.getWindow().setAttributes(layoutparams);
				final VideoView videoplayer = (VideoView) dialog
						.findViewById(R.id.video_player);
				videoplayer.setVideoURI(getItem(position).getVideouri());
				// Intent intent = new Intent(context, VideoPlayerManual.class);
				// intent.putExtra("videouri", getItem(position).getVideouri());
				// context.startActivity(intent);
				final MediaController mediacontroller = new MediaController(
						context);

				videoplayer.setMediaController(mediacontroller);
				mediacontroller.setMediaPlayer(videoplayer);
				// mediacontroller.setPadding(0, 250, 0, 0);
				mediacontroller.setAnchorView(videoplayer);
				dialog.show();
				videoplayer.start();

			}
		});
		// backbutton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// Intent intent = new Intent(context, DashBoardActivity.class);
		// context.startActivity(intent);
		//
		// }
		// });
		likebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(
						context,
						"You like video of your kin "
								+ touchKinBook.getVideoSenderName(),
						Toast.LENGTH_LONG).show();

			}
		});
		videoText.setText(touchKinBook.getVideoText());
		videoTime.setText(touchKinBook.getVideoDate());
		videoDay.setText(touchKinBook.getVideoDay());
		// viewHolder.userImage
		// videoSenderName.setText(touchKinBook.getVideoSenderName());
		// videoViewCount.setText(touchKinBook.getVideoViewCount());

		// viewHolder.videoSenderImage = (RoundedImageView) convertView
		// .findViewById(R.id.senderImage);
		// viewHolder.videoView = (VideoView) convertView
		// .findViewById(R.id.videoView);
		// viewHolder.commentEditText = (EditText) convertView
		// .findViewById(R.id.commentEditText);

		// comments = touchKinBook.getTouchKinComments();
		// adapter = new CommentListAdapter(comments, context);
		// viewHolder.commentList.setAdapter(adapter);
		// Helper.getListViewSize(viewHolder.commentList);
		// viewHolder.commentEditText
		// .setOnEditorActionListener(new OnEditorActionListener() {
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event) {
		// if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
		// || (actionId == EditorInfo.IME_ACTION_DONE)) {
		// // Toast.makeText(MainActivity.this, "enter press",
		// // Toast.LENGTH_LONG).show();
		// sendIntent(viewHolder.commentEditText.getText()
		// .toString(), touchKinBook.getMessageId());
		// }
		// return false;
		// }
		// });
		return convertView;
	}
	// @SuppressLint("NewApi")
	// public void sendIntent(String text, String kbmId) {
	// // Intent i = new Intent(SignUpActivity.this,
	// // DashBoardActivity.class);
	// // Bundle bndlanimation = ActivityOptions
	// // .makeCustomAnimation(getApplicationContext(),
	// // R.anim.animation, R.anim.animation2)
	// // .toBundle();
	// // startActivity(i,bndlanimation);
	// //
	//
	// JSONObject params = new JSONObject();
	// try {
	// params.put("comment", text);
	// params.put("kinbook_message_id", kbmId);
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
	// "http://54.69.183.186:1340/kinbook/comment", params,
	// new Response.Listener<JSONObject>() {
	//
	// @Override
	// public void onResponse(JSONObject response) {
	//
	// Log.d("Response", response.toString());
	//
	// // VolleyLog.v("Response:%n %s",
	// // response.toString(4));
	//
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	//
	// }
	// // Additional cases
	//
	// });
	//
	// AppController.getInstance().addToRequestQueue(req);
	// }
}

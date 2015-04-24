package com.touchKin.touchkinapp.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.touchKin.touchkinapp.OtpRequestActivity;
import com.touchKin.touchkinapp.SignUpActivity;
import com.touchKin.touchkinapp.custom.Helper;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.TouchKinBookModel;
import com.touchKin.touchkinapp.model.TouchKinComments;
import com.touchKin.touckinapp.R;

public class FlipViewAdapter extends BaseAdapter {
	List<TouchKinBookModel> touckinBook;
	Context context;
	LayoutInflater inflater;
	List<TouchKinComments> comments;
	CommentListAdapter adapter;
	TouchKinBookModel touchKinBook;

	public FlipViewAdapter(List<TouchKinBookModel> touckinBook, Context context) {
		super();
		this.touckinBook = touckinBook;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return touckinBook.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return touckinBook.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private static class ViewHolder {
		TextView videoText, videoTime, videoDay, videoSenderName,
				videoViewCount;
		RoundedImageView userImage, videoSenderImage;
		EditText commentEditText;
		VideoView videoView;
		ListView commentList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		touchKinBook = (TouchKinBookModel) getItem(position);
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.flipview_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.videoText = (TextView) convertView
					.findViewById(R.id.videoCommentTextView);
			viewHolder.videoTime = (TextView) convertView
					.findViewById(R.id.videoTimeTextView);
			viewHolder.videoDay = (TextView) convertView
					.findViewById(R.id.videoDayTextView);
			viewHolder.userImage = (RoundedImageView) convertView
					.findViewById(R.id.userImage);
			viewHolder.videoSenderName = (TextView) convertView
					.findViewById(R.id.videoSenderNameTextView);
			viewHolder.videoViewCount = (TextView) convertView
					.findViewById(R.id.videoSeenCountTextView);

			viewHolder.videoSenderImage = (RoundedImageView) convertView
					.findViewById(R.id.senderImage);
			viewHolder.videoView = (VideoView) convertView
					.findViewById(R.id.videoView);
			viewHolder.commentEditText = (EditText) convertView
					.findViewById(R.id.commentEditText);

			viewHolder.commentList = (ListView) convertView
					.findViewById(R.id.commentList);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.videoText.setText(touchKinBook.getVideoText());
		viewHolder.videoTime.setText(touchKinBook.getVideoDate());
		viewHolder.videoDay.setText(touchKinBook.getVideoDay());
		// viewHolder.userImage
		viewHolder.videoSenderName.setText(touchKinBook.getVideoSenderName());
		viewHolder.videoViewCount.setText(touchKinBook.getVideoViewCount());

		// viewHolder.videoSenderImage = (RoundedImageView) convertView
		// .findViewById(R.id.senderImage);
		// viewHolder.videoView = (VideoView) convertView
		// .findViewById(R.id.videoView);
		// viewHolder.commentEditText = (EditText) convertView
		// .findViewById(R.id.commentEditText);

		comments = touchKinBook.getTouchKinComments();
		adapter = new CommentListAdapter(comments, context);
		viewHolder.commentList.setAdapter(adapter);
		Helper.getListViewSize(viewHolder.commentList);
		viewHolder.commentEditText
				.setOnEditorActionListener(new OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
								|| (actionId == EditorInfo.IME_ACTION_DONE)) {
							// Toast.makeText(MainActivity.this, "enter press",
							// Toast.LENGTH_LONG).show();
							sendIntent(viewHolder.commentEditText.getText()
									.toString(), touchKinBook.getMessageId());
						}
						return false;
					}
				});
		return convertView;
	}

	@SuppressLint("NewApi")
	public void sendIntent(String text, String kbmId) {
		// Intent i = new Intent(SignUpActivity.this,
		// DashBoardActivity.class);
		// Bundle bndlanimation = ActivityOptions
		// .makeCustomAnimation(getApplicationContext(),
		// R.anim.animation, R.anim.animation2)
		// .toBundle();
		// startActivity(i,bndlanimation);
		//

		JSONObject params = new JSONObject();
		try {
			params.put("comment", text);
			params.put("kinbook_message_id", kbmId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
				"http://54.69.183.186:1340/kinbook/comment", params,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						Log.d("Response", response.toString());

						// VolleyLog.v("Response:%n %s",
						// response.toString(4));

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
					// Additional cases

				});

		AppController.getInstance().addToRequestQueue(req);
	}
}

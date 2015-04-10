package com.touchKin.touchkinapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.touchKin.touchkinapp.custom.Helper;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.TouchKinBookModel;
import com.touchKin.touchkinapp.model.TouchKinComments;
import com.touchKin.touckinapp.R;

public class FlipViewAdapter extends BaseAdapter {
	List<TouchKinBookModel> touckinBook;
	Context context;
	LayoutInflater inflater;
	List<TouchKinComments> comments;
	CommentListAdapter adapter;

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
				videoViewCount, commentCount;
		RoundedImageView userImage, videoSenderImage;
		EditText commentEditText;
		VideoView videoView;
		ListView commentList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TouchKinBookModel touchKinBook = (TouchKinBookModel) getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.flipview_layout, null);
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
			viewHolder.commentCount = (TextView) convertView
					.findViewById(R.id.commentCountTextView);
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
		viewHolder.commentCount.setText(" " + comments.size() + " Reply");
		Helper.getListViewSize(viewHolder.commentList);
		return convertView;
	}
}

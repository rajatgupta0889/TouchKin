package com.touchKin.touchkinapp.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.ParentSendTouchModel;
import com.touchKin.touckinapp.R;

public class AddParentToSendTouchAdapter extends BaseAdapter {
	List<ParentSendTouchModel> parentList;
	Context context;
	static LayoutInflater inflater = null;

	public AddParentToSendTouchAdapter(List<ParentSendTouchModel> parentList,
			Context context) {
		super();
		this.parentList = parentList;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private static class ViewHolder {
		TextView parentName;
		RoundedImageView parentImage;
		CheckBox isSelected;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return parentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return parentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ParentSendTouchModel parentItem = (ParentSendTouchModel) getItem(position);
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.add_parent_to_send_touch,
					null);
			viewHolder = new ViewHolder();
			viewHolder.parentName = (TextView) convertView
					.findViewById(R.id.parentNameTextView);

			viewHolder.parentImage = (RoundedImageView) convertView
					.findViewById(R.id.parentImageView);
			viewHolder.isSelected = (CheckBox) convertView
					.findViewById(R.id.isSelectedCB);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.d("UserName", parentItem.getUserName());
		viewHolder.parentName.setText(parentItem.getUserName());

		viewHolder.parentImage.setImageDrawable(context.getResources()
				.getDrawable(R.drawable.mom));
		viewHolder.isSelected.setChecked(parentItem.getIsSelected());
		return convertView;
	}
}

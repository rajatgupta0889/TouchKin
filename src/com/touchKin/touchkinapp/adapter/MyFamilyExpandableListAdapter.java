package com.touchKin.touchkinapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.touchKin.touchkinapp.model.ExpandableListGroupItem;
import com.touchKin.touckinapp.R;

public class MyFamilyExpandableListAdapter extends BaseExpandableListAdapter {

	Context context;
	List<ExpandableListGroupItem> groupList;
	LayoutInflater inflater;
	List<Group> groups = new ArrayList<Group>();

	public MyFamilyExpandableListAdapter(Context context,
			List<ExpandableListGroupItem> groupList) {
		super();
		this.context = context;
		this.groupList = groupList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition).travelItems.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition).travelItems.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		TextView text = null;

		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.expandable_group_item, parent, false);
		}

		text = (TextView) view.findViewById(R.id.userName);

		// StringBuilder title = new StringBuilder();
		// if (groupPosition == 0) {
		// title.append(context.getString(R.string.future_travel_list_header));
		// } else {
		// title.append(context.getString(R.string.past_travel_list_header));
		// }
		// title.append(" (");
		// //title.append(groups.get(groupPosition).travelItems.size());
		// title.append(")");

		// text.setText(title.toString());

		/*
		 * if this is not the first group (future travel) show the arrow image
		 * and change state if necessary
		 */

		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = convertView;
		TextView text = null;

		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.expandable_group_item, parent, false);
		}
		return null;
		
		
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public static class Group {

		ArrayList<ExpandableListGroupItem> travelItems = new ArrayList<ExpandableListGroupItem>();
	}

	/*
	 * setup travel plans and past trips into groups
	 */
	public void setupTrips(List<ExpandableListGroupItem> list,
			List<ExpandableListGroupItem> list1) {
		groups.clear();

		Group g1 = new Group();
		g1.travelItems.clear();
		g1.travelItems = new ArrayList<ExpandableListGroupItem>(list1);

		groups.add(g1);

		Group g2 = new Group();

		g2.travelItems.clear();
		g2.travelItems = new ArrayList<ExpandableListGroupItem>(list);

		groups.add(g2);

		notifyDataSetChanged();
	}

}

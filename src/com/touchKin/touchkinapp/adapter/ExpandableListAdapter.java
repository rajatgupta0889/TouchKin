package com.touchKin.touchkinapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.touchKin.touchkinapp.adapter.ExpandableListAdapter.Group.Type;
import com.touchKin.touchkinapp.adapter.ExpandableListAdapter.GroupChild.TypeChild;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.ExpandableListGroupItem;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touchkinapp.model.RequestModel;
import com.touchKin.touckinapp.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	/*
	 * view types
	 */
	private static final int PAST_TRAVEL_VIEW = 1;
	private static final int FUTURE_TRAVEL_VIEW = 0;

	/*
	 * data
	 */
	private Context context = null;
	ArrayList<Group> groups = new ArrayList<Group>();
	ArrayList<GroupChild> childGroups = new ArrayList<GroupChild>();

	public ExpandableListAdapter(Context context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseExpandableListAdapter#getChildType(int, int)
	 */
	@Override
	public int getChildType(int groupPosition, int childPosition) {
		int type = -1;
		if (groups.size() == 2 && groupPosition == 1) {
			type = PAST_TRAVEL_VIEW;
		} else {
			type = FUTURE_TRAVEL_VIEW;
		}

		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseExpandableListAdapter#getChildTypeCount()
	 */
	@Override
	public int getChildTypeCount() {
		// Past and Future Travel Plans
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// get the type of the group this child belongs
		Type viewType = groups.get(groupPosition).type;
		Log.d("Positon", " child" + childPosition + " Parent" + groupPosition);
		GroupChild child = (GroupChild) getChild(groupPosition, childPosition);

		View view = convertView;

		// if the type is future travel, use the future travel layout
		if (viewType == Type.CONN) {
			TypeChild childViewType = child.typeChild;
			if (childViewType == TypeChild.ME) {
				// if (view == null) {
				view = LayoutInflater.from(context).inflate(
						R.layout.expand_list_first_child, parent, false);

				LinearLayout linearLayout = (LinearLayout) view
						.findViewById(R.id.futureTravelLineItemLayout);
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (child._listDataChild != null) {
					for (int i = 0; i < child._listDataChild.size(); i++) {

						View view1 = inflater.inflate(R.layout.image_item,
								linearLayout, false);
						final RoundedImageView tv = (RoundedImageView) view1
								.findViewById(R.id.parentImage);
						tv.setId(tv.getId() + i);
						tv.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Toast.makeText(context,
										"click id " + tv.getId(),
										Toast.LENGTH_SHORT).show();
							}
						});
						linearLayout.addView(view1);
					}
				}
				if (child.connReq != null) {
					LinearLayout linearLayout1 = (LinearLayout) view
							.findViewById(R.id.futureTravel);
					for (int i = 0; i < child.connReq.size(); i++) {

						View view1 = inflater.inflate(
								R.layout.connection_req_item, linearLayout,
								false);
						final RoundedImageView tv = (RoundedImageView) view1
								.findViewById(R.id.parentImage);
						tv.setId(tv.getId() + i);
						tv.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Toast.makeText(context,
										"click id " + tv.getId(),
										Toast.LENGTH_SHORT).show();
							}
						});
						linearLayout1.addView(view1);

					}

					// holder.title = (TextView) view
					// .findViewById(R.id.futureTravelTitle);
					// holder.departure = (TextView) view
					// .findViewById(R.id.futureTravelDeparture);
					// holder.destination = (TextView) view
					// .findViewById(R.id.futureTravelDestination);
					// holder.date = (TextView) view
					// .findViewById(R.id.futureTravelDate);
					// holder.time = (TextView) view
					// .findViewById(R.id.futureTravelTime);

				}
				//
				// FutureTravelViewHolder holder = (FutureTravelViewHolder) view
				// .getTag();
				//
				// TravelItem currentItem = (TravelItem) getChild(groupPosition,
				// childPosition);

				// holder.title.setText(currentItem.getTitle());
				// holder.departure.setText(currentItem.getDeparture());
				// holder.destination.setText(currentItem.getDestination());
				// holder.date.setText(currentItem.getDate());
				// holder.time.setText(currentItem.getTime());
			} else {
				// if the type is past, use the past travel layout

				view = LayoutInflater.from(context).inflate(
						R.layout.expand_list_first_child, parent, false);

				//
				// PastTravelViewHolder holder = new PastTravelViewHolder();
				// holder.title = (TextView) view
				// .findViewById(R.id.pastTravelTitle);
				// holder.departure = (TextView) view
				// .findViewById(R.id.pastTravelDeparture);
				// holder.destination = (TextView) view
				// .findViewById(R.id.pastTravelDestination);
				// holder.date = (TextView)
				// view.findViewById(R.id.pastTravelDate);
				//
				// view.setTag(holder);
				LinearLayout linearLayout = (LinearLayout) view
						.findViewById(R.id.futureTravelLineItemLayout);
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (child._listDataChild != null) {
					for (int i = 0; i < child._listDataChild.size(); i++) {

						View view1 = inflater.inflate(R.layout.image_item,
								linearLayout, false);
						final RoundedImageView tv = (RoundedImageView) view1
								.findViewById(R.id.parentImage);
						tv.setId(tv.getId() + i);
						tv.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Toast.makeText(context,
										"click id " + tv.getId(),
										Toast.LENGTH_SHORT).show();
							}
						});
						linearLayout.addView(view1);
					}

				}
			}
			//
			// TravelItem currentItem = (TravelItem) getChild(groupPosition,
			// childPosition);
			//
			// holder.title.setText(currentItem.getTitle());
			// holder.departure.setText(currentItem.getDeparture());
			// holder.destination.setText(currentItem.getDestination());
			// holder.date.setText(currentItem.getDate());

		} else {
			return null;
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		Group grp = groups.get(groupPosition);
		if (grp.child != null)
			return 1;
		else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View view = convertView;
		TextView name = null;
		TextView kinCount = null;
		ImageView image = null;

		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.expandable_group_item, parent, false);
		}
		Group grp = groups.get(groupPosition);
		name = (TextView) view.findViewById(R.id.userName);
		kinCount = (TextView) view.findViewById(R.id.kinInfo);
		image = (ImageView) view.findViewById(R.id.drawer);
		if (grp.type == Type.CONN) {

			// title.append(" (");
			// title.append(groups.get(groupPosition).travelItems.size());
			// title.append(")");
			//
			// text.setText(title.toString());

			/*
			 * if this is not the first group (future travel) show the arrow
			 * image and change state if necessary
			 */

			int imageResourceId = isExpanded ? R.drawable.list_open
					: R.drawable.list_close;
			image.setImageResource(imageResourceId);

		} else {
			int imageResourceId = R.drawable.info;
			image.setImageResource(imageResourceId);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/*
	 * setup travel plans and past trips into groups
	 */
	public void setupTrips(
			HashMap<String, ArrayList<ParentListModel>> careGiver,
			ArrayList<RequestModel> requests,
			ArrayList<ExpandableListGroupItem> CareReciever,
			ArrayList<ExpandableListGroupItem> pendingReq) {
		groups.clear();
		childGroups.clear();
		if (CareReciever != null) {

			for (ExpandableListGroupItem item : CareReciever) {
				Group group = new Group();
				group.type = Type.CONN;
				GroupChild child = new GroupChild();
				group.groupMemebr = item;
				child._listDataChild = careGiver.get(item.getUserId());
				child.typeChild = TypeChild.CR;
				group.child = child;
				groups.add(group);
			}

		}
		if (requests != null) {
			Group temp = groups.get(0);
			temp.child.connReq = requests;
			temp.child.typeChild = TypeChild.ME;

		}
		if (pendingReq != null) {

			for (ExpandableListGroupItem item : pendingReq) {
				Group group = new Group();
				group.type = Type.REQ;
				group.groupMemebr = item;
				groups.add(group);
			}
		}

		notifyDataSetChanged();
	}

	/*
	 * Holder for the Past view type
	 */
	class MyLaoutHolder {
		TextView name;
		TextView kinCount;
		RoundedImageView userImage;
		ImageView icon;
		RoundedImageView careGiversImage;
		ImageView accept, reject;
		TextView reqText;
		RoundedImageView reqUserImage;
		LinearLayout linearLayout, linearLayout2;

	}

	/*
	 * Holder for the Future view type
	 */
	class CRLayoutHolder {
		TextView title;
		TextView departure;
		TextView destination;
		TextView date;
		TextView time;
	}

	/*
	 * Wrapper for each group that contains the list elements and the type of
	 * travel.
	 */
	public static class Group {
		public enum Type {
			CONN, REQ;
		};

		public Type type;
		public int groupSIze;
		GroupChild child = null;
		ExpandableListGroupItem groupMemebr;
	}

	public static class GroupChild {
		public enum TypeChild {
			ME, CR;
		};

		public TypeChild typeChild;

		private ArrayList<ParentListModel> _listDataChild = null;
		ArrayList<RequestModel> connReq = null;
	}
}
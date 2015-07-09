package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.touchKin.touchkinapp.Interface.ButtonClickListener;
import com.touchKin.touchkinapp.adapter.ExpandableListAdapter;
import com.touchKin.touchkinapp.adapter.ImageAdapter;
import com.touchKin.touchkinapp.adapter.MyAdapter;
import com.touchKin.touchkinapp.adapter.MyAdapter.ViewHolder.IMyViewHolderClicks;
import com.touchKin.touchkinapp.custom.CustomRequest;
import com.touchKin.touchkinapp.custom.HorizontalListView;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touchkinapp.services.DeviceAcivityService;
import com.touchKin.touchkinapp.services.LocationSendingService;
import com.touchKin.touckinapp.R;

public class DashBoardActivity extends ActionBarActivity implements
		AnimationListener, OnItemClickListener, IMyViewHolderClicks,
		ButtonClickListener {
	public FragmentTabHost mTabHost;
	String NAME = "Rajat Gupta ";
	// String EMAIL = "akash.bangad@android4devs.com";
	// int PROFILE = R.drawable.mom;
	String TITLES[] = { "My Family", "My Accounts", "Upgrade", "Terms of Use",
			"Contact Us", "Sign out" };
	private Toolbar toolbar; // Declaring the Toolbar Object

	RecyclerView mRecyclerView; // Declaring RecyclerView
	RecyclerView.Adapter<MyAdapter.ViewHolder> mAdapter; // Declaring Adapte
	// For Recycler View
	RelativeLayout relative;
	RecyclerView.LayoutManager mLayoutManager; // Declaring Layout Manager as a
	TextView mTitle;
	DrawerLayout Drawer; // Declaring DrawerLayout
	RelativeLayout parentRelativeLayout;
	HorizontalListView listview;
	Animation animSlideUp, animSlideDown;
	List<ParentListModel> list, careGiverList;
	private ParentListModel selectedParent;
	ActionBarDrawerToggle mDrawerToggle; // Declaring Action Bar Drawer Toggle
	private ImageAdapter imageAdapter;
	public static Boolean isCancel = true;
	public String userId, userName, phoneNo;
	ButtonClickListener listener;
	JSONObject userObj;
	static Button notifCount;
	String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);

		// mTabHost = new FragmentTabHost(this);
		// mTabHost.setup(this, getSupportFragmentManager(),
		// R.id.menu_settings);
		// lLayout = new MyLinearLayout(this);

		InitView();
		setSupportActionBar(toolbar);
		toolbar.inflateMenu(R.menu.toolbar_menu);
		// toolbar.setOnMenuItemClickListener(new
		// Toolbar.OnMenuItemClickListener() {
		// @Override
		// public boolean onMenuItemClick(MenuItem item) {
		// // Handle the menu item
		// int id = item.getItemId();
		// if (id == R.id.parentIconMenu) {
		//
		// // toggleVissibility();
		// return true;
		// }
		//
		// return true;
		// }
		// });

		mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

		mRecyclerView.setHasFixedSize(true);

		MyAdapter.mListener = DashBoardActivity.this;
		SharedPreferences userPref = getApplicationContext()
				.getSharedPreferences("userPref", 0);

		String user = userPref.getString("user", null);
		try {
			userObj = new JSONObject(user);
			userId = userObj.getString("id");
			userName = userObj.getString("first_name");
			phoneNo = userObj.getString("mobile");
			token = userObj.optString("token");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		fetchParentList();
		mAdapter = new MyAdapter(TITLES, userName, userId,
				DashBoardActivity.this); // Creating
		mRecyclerView.setAdapter(mAdapter); // Setting the adapter to
		// RecyclerView

		mLayoutManager = new LinearLayoutManager(this); // Creating a layout
		// Manager

		mRecyclerView.setLayoutManager(mLayoutManager); // Setting the layout
		Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout); // Drawer
		relative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (parentRelativeLayout.getVisibility() == View.VISIBLE) {
					parentRelativeLayout.startAnimation(animSlideUp);

				}
			}
		});
		// object
		// Assigned
		// to the
		// view
		toolbar.setNavigationIcon(R.drawable.ic_drawer);
		mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, null,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// code here will execute once the drawer is opened( As I dont
				// want anything happened whe drawer is
				// open I am not going to put anything here)

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				// Code here will execute once drawer is closed

			}

		}; // Drawer Toggle Object Made
		Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the
		// Drawer toggle

		toolbar.setTitle("");
		mDrawerToggle.setDrawerIndicatorEnabled(false);
		toolbar.setNavigationOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				Drawer.openDrawer(Gravity.LEFT);

			}
		});
		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleVissibility();

			}
		});

		mDrawerToggle.syncState(); // Finally we set the drawer toggle sync
		// State
		// mTitle.setText("TouchKin");

		listview.setOnItemClickListener(this);
		animSlideUp.setAnimationListener(this);
		animSlideDown.setAnimationListener(this);

		// AlarmManager alarm = (AlarmManager)
		// getSystemService(Context.ALARM_SERVICE);
		// Intent intent1 = new Intent(DashBoardActivity.this,
		// DeviceAcivityService.class);
		// PendingIntent pending = PendingIntent.getService(
		// DashBoardActivity.this, 0, intent1, 0);
		// // alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// // 10000, pending);
		// alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		// AlarmManager.INTERVAL_FIFTEEN_MINUTES,
		// AlarmManager.INTERVAL_FIFTEEN_MINUTES, pending);
		startLocationService();
		startActivityService();
		// startService(intent1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userName == null || userName.isEmpty())
			try {
				userName = userObj.getString("first_name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		View count = menu.findItem(R.id.parentIconMenu).getActionView();
		Log.d("count", count + "");
		TextView notification = (TextView) count.findViewById(R.id.hotlist_hot);
		notification.setText("5");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// Toast.makeText(getApplicationContext(), "HI menu" + id,
		// Toast.LENGTH_SHORT).show();
		//
		// if (id == R.id.parentIconMenu) {
		// // Not implemented here
		//
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	private void setTabColor(FragmentTabHost mTabHost) {
		for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); i++) {
			this.mTabHost
					.getTabWidget()
					.getChildAt(i)
					.setBackgroundColor(getResources().getColor(R.color.tab_bg)); // unselected

		}
		this.mTabHost
				.getTabWidget()
				.getChildAt(this.mTabHost.getCurrentTab())
				.setBackgroundColor(
						getResources().getColor(R.color.tab_selected));

	}

	private void InitView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		Bundle b = new Bundle();
		b.putString("key", "Fake");
		mTabHost.addTab(
				mTabHost.newTabSpec("DashBoard").setIndicator("Dash Board"),
				Fragment1.class, b);

		mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
		// Added tab for mydashboard
		b = new Bundle();
		b.putString("key", "MyDashboard");
		mTabHost.addTab(
				mTabHost.newTabSpec("MyDashBoard").setIndicator(
						" My Dash Board"), Fragment2.class, b);
		mTabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
		mTabHost.setCurrentTab(1);
		b = new Bundle();
		b.putString("key", "TouchKin");
		mTabHost.addTab(
				setIndicator(this, mTabHost.newTabSpec("KinBook"),
						R.color.tab_bg, "Kinbook", R.drawable.kinbook),
				TouchKinBookFragment.class, b);

		b = new Bundle();
		b.putString("key", "Messages");
		mTabHost.addTab(
				setIndicator(this, mTabHost.newTabSpec("Message"),
						R.color.tab_bg, "Messages", R.drawable.message),
				MessagesFragment.class, b);

		b = new Bundle();
		b.putString("key", "Settings");
		mTabHost.addTab(
				setIndicator(this, mTabHost.newTabSpec("Settings"),
						R.color.tab_bg, "Settings", R.drawable.settings),
				TouchKinBookFragment.class, b);

		b = new Bundle();
		b.putString("key", "ER Plan");
		mTabHost.addTab(
				setIndicator(this, mTabHost.newTabSpec("ER"), R.color.tab_bg,
						"ER Plan", R.drawable.er), TouchKinBookFragment.class,
				b);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				setTabColor(mTabHost);

			}

		});

		listview = (HorizontalListView) findViewById(R.id.parentListView);

		parentRelativeLayout = (RelativeLayout) findViewById(R.id.parentListLayoutDashboard);
		animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		animSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
		relative = (RelativeLayout) findViewById(R.id.relativeDashboard);
	}

	private TabSpec setIndicator(Context ctx, TabSpec spec, int resid,
			String string, int genresIcon) {
		View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
		v.setBackgroundResource(resid);
		TextView tv = (TextView) v.findViewById(R.id.txt_tabtxt);
		ImageView img = (ImageView) v.findViewById(R.id.img_tabtxt);

		tv.setText(string);
		img.setBackgroundResource(genresIcon);

		return spec.setIndicator(v);
	}

	private void toggleVissibility() {
		// TODO Auto-generated method stub
		if (parentRelativeLayout.getVisibility() == View.VISIBLE) {
			parentRelativeLayout.startAnimation(animSlideUp);

		} else {
			parentRelativeLayout.setVisibility(View.VISIBLE);
			parentRelativeLayout.startAnimation(animSlideDown);
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animSlideDown) {
			parentRelativeLayout.setVisibility(View.VISIBLE);
			parentRelativeLayout.setClickable(true);

		} else if (animation == animSlideUp) {
			parentRelativeLayout.setVisibility(View.GONE);
			parentRelativeLayout.setClickable(false);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void fetchParentList() {
		list = new ArrayList<ParentListModel>();
		careGiverList = new ArrayList<ParentListModel>();
		JsonObjectRequest req = new JsonObjectRequest(Method.GET,
				"http://54.69.183.186:1340/user/family", null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject responseArray) {
						// TODO Auto-generated method stub
						Log.d("Response Array", " " + responseArray);
						try {
							list.add(new ParentListModel(userId, true, "Me",
									userId, "", userObj.getString("mobile")));
							list.get(0).setReqStatus(true);
							careGiverList.add(new ParentListModel(userId,
									false, "Me", userId, "", userObj
											.getString("mobile")));
							careGiverList.get(0).setReqStatus(true);
							selectedParent = list.get(0);
							JSONArray careRecievers = responseArray
									.getJSONArray("care_receivers");
							int crCount = careRecievers.length();
							for (int i = 0; i < crCount; i++) {
								JSONObject cr;

								cr = careRecievers.getJSONObject(i);

								if (cr != null) {
									ParentListModel item = new ParentListModel();
									item.setParentId(cr.getString("id"));
									item.setParentName(cr.optString("nickname"));
									item.setMobilenumber(cr.optString("mobile"));
									if (cr.has("care_receiver_status")
											&& cr.getString(
													"care_receiver_status")
													.equalsIgnoreCase("pending")) {
										item.setReqStatus(false);
									} else {
										item.setReqStatus(true);
									}
									item.setIsSelected(false);
									list.add(item);
								}
							}
							JSONArray careGivers = responseArray
									.getJSONArray("care_givers");
							int cgCount = careGivers.length();
							for (int i = 0; i < cgCount; i++) {
								JSONObject cg = careGivers.getJSONObject(i);
								if (cg != null) {
									ParentListModel item = new ParentListModel();
									item.setParentId(cg.getString("id"));
									item.setParentName(cg
											.optString("first_name"));
									if (cg.has("care_receiver_status")
											&& cg.getString(
													"care_receiver_status")
													.equalsIgnoreCase("pending")) {
										item.setReqStatus(false);
									} else {
										item.setReqStatus(true);
									}
									item.setMobilenumber(cg.optString("mobile"));
									careGiverList.add(item);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// else {
						// setMenuTitle(null);
						// }
						if (list.size() > 1) {
							Collections.sort(list,
									new Comparator<ParentListModel>() {
										@Override
										public int compare(ParentListModel lhs,
												ParentListModel rhs) {
											// TODO Auto-generated method stub
											return rhs.getReqStatus()
													.compareTo(
															lhs.getReqStatus());
										}
									});
						}
						list.add(new ParentListModel("", false, "", "", "", ""));
						imageAdapter = new ImageAdapter(DashBoardActivity.this,
								list);
						// if (selectedParent == null) {
						// selectedParent = list.get(0);
						// }
						if (listener != null)
							listener.onButtonClickListner(0, null, false);
						setMenuTitle(selectedParent);
						listview.setAdapter(imageAdapter);

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(DashBoardActivity.this,
								error.getMessage(), Toast.LENGTH_SHORT).show();

					}

				}) {
			public java.util.Map<String, String> getHeaders()
					throws com.android.volley.AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", "Bearer " + token);
				return headers;

			};
		};
		AppController.getInstance().addToRequestQueue(req);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		ParentListModel item = list.get(position);

		if (!item.getParentId().equals("")) {
			listview.setAdapter(imageAdapter);
			selectedParent = item;
			mTabHost.setVisibility(View.VISIBLE);
			for (ParentListModel data : list) {

				if (data.equals(item)) {
					data.setIsSelected(true);
				} else {
					data.setIsSelected(false);
				}
			}
			setMenuTitle(item);
			if (item.getParentId().equalsIgnoreCase(userId)) {
				mTabHost.setCurrentTab(1);
			} else {

				if (mTabHost.getCurrentTab() != 0) {
					mTabHost.setCurrentTab(0);
					getSupportFragmentManager().executePendingTransactions();
				}
				((Fragment1) getSupportFragmentManager().findFragmentByTag(
						"DashBoard")).notifyFrag();
			}

		} else {
			DialogFragment newFragment = new ContactDialogFragment();
			newFragment.setCancelable(true);
			Bundle args = new Bundle();
			args.putInt("num", ExpandableListAdapter.ADD_CR);
			args.putString("mobile", phoneNo);
			args.putString("token", token);
			newFragment.setArguments(args);
			newFragment.show(getSupportFragmentManager(), "TAG");
			((ContactDialogFragment) newFragment).SetButtonListener(this);
		}
		toggleVissibility();

	}

	public void setMenuTitle(ParentListModel item) {

		if (item != null) {
			mTitle.setText(item.getParentName());

		} else {
			mTitle.setText("Add");
		}
		mTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.ic_action_down, 0);

	}

	public ParentListModel getSelectedParent() {
		return selectedParent;
	}

	@Override
	public void onItemTouch(int caller) {
		// TODO Auto-generated method stub
		Toast.makeText(DashBoardActivity.this, "View " + caller,
				Toast.LENGTH_SHORT).show();

		if (caller == 6) {

			SharedPreferences userPref = getApplicationContext()
					.getSharedPreferences("userPref", 0);
			Editor edit = userPref.edit();
			edit.putString("user", null);
			edit.apply();
			Intent intent = new Intent(this, SignUpActivity.class);
			startActivity(intent);
			finish();

		}
		if (caller == 1) {
			Intent intent = new Intent(DashBoardActivity.this, MyFamily.class);
			intent.putExtra("isLoggedIn", true);

			startActivity(intent);
		}

	}

	public void setCustomButtonListner(ButtonClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onImageTouch() {
		// TODO Auto-generated method stub
		Toast.makeText(DashBoardActivity.this, "View image ",
				Toast.LENGTH_SHORT).show();
		Intent i = new Intent(DashBoardActivity.this, Details.class);
		i.putExtra("isLoggedin", true);
		startActivity(i);
	}

	public FragmentTabHost getTabHost() {
		return mTabHost;
	}

	@Override
	public void onButtonClickListner(int position, String value,
			Boolean isAccept) {
		// TODO Auto-generated method stub
		fetchParentList();
	}

	public void startLocationService() {
		Intent mServiceIntent = new Intent(DashBoardActivity.this,
				LocationSendingService.class);
		mServiceIntent.putExtra("token", token);
		DashBoardActivity.this.startService(mServiceIntent);
		Log.d("Service run: %s", "Service is started");
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(DashBoardActivity.this,
				LocationSendingService.class);
		intent.putExtra("token", token);
		// Intent intent = new Intent("com.my.package.MY_UNIQUE_ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				DashBoardActivity.this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// AlarmManager alarmManager = (AlarmManager)
		// applicationContext.getSystemService(Context.ALARM_SERVICE);
		// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		// cal.getTimeInMillis(), 1000 * 60, pendingIntent);
		// PendingIntent pintent = PendingIntent.getService(
		// applicationContext, 0, intent, 0);
		boolean alarmUp = (PendingIntent.getBroadcast(DashBoardActivity.this,
				0, intent, PendingIntent.FLAG_NO_CREATE) != null);

		if (alarmUp) {
			Log.d("myTag", "Alarm is already active");
		} else {
			AlarmManager alarm = (AlarmManager) DashBoardActivity.this
					.getSystemService(Context.ALARM_SERVICE);
			// Start service every hour
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					3600 * 1000, pendingIntent);
		}
	}

	public void startActivityService() {
		Intent mServiceIntent = new Intent(DashBoardActivity.this,
				DeviceAcivityService.class);
		mServiceIntent.putExtra("token", token);
		DashBoardActivity.this.startService(mServiceIntent);

		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(DashBoardActivity.this,
				DeviceAcivityService.class);
		intent.putExtra("token", token);
		// Intent intent = new Intent("com.my.package.MY_UNIQUE_ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				DashBoardActivity.this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// AlarmManager alarmManager = (AlarmManager)
		// applicationContext.getSystemService(Context.ALARM_SERVICE);
		// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		// cal.getTimeInMillis(), 1000 * 60, pendingIntent);
		// PendingIntent pintent = PendingIntent.getService(
		// applicationContext, 0, intent, 0);
		boolean alarmUp = (PendingIntent.getBroadcast(DashBoardActivity.this,
				0, intent, PendingIntent.FLAG_NO_CREATE) != null);

		if (alarmUp) {
			Log.d("myTag", "Alarm is already active");
		} else {
			AlarmManager alarm = (AlarmManager) DashBoardActivity.this
					.getSystemService(Context.ALARM_SERVICE);
			// Start service every hour
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					3600 * 1000, pendingIntent);
		}
	}

	public List<ParentListModel> getParentList() {
		if (careGiverList.size() > 1) {
			Collections.sort(careGiverList, new Comparator<ParentListModel>() {
				@Override
				public int compare(ParentListModel lhs, ParentListModel rhs) {
					// TODO Auto-generated method stub
					return rhs.getReqStatus().compareTo(lhs.getReqStatus());
				}
			});
		}
		return careGiverList;
	}

	public String getToken() {
		return token;
	}
}

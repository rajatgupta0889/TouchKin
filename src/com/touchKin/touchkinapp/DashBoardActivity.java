package com.touchKin.touchkinapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.touchKin.touchkinapp.adapter.ImageAdapter;
import com.touchKin.touchkinapp.adapter.MyAdapter;
import com.touchKin.touchkinapp.custom.HorizontalListView;
import com.touchKin.touchkinapp.model.AppController;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touckinapp.R;

@SuppressLint("NewApi")
public class DashBoardActivity extends ActionBarActivity implements
		AnimationListener, OnItemClickListener {
	private FragmentTabHost mTabHost;
	String NAME = "Rajat Gupta ";
	String EMAIL = "akash.bangad@android4devs.com";
	int PROFILE = R.drawable.mom;
	String TITLES[] = { "My Family", "My Accounts", "Upgrade", "Terms of Use",
			"Contact Us" };
	private Toolbar toolbar; // Declaring the Toolbar Object

	RecyclerView mRecyclerView; // Declaring RecyclerView
	RecyclerView.Adapter mAdapter; // Declaring Adapter For Recycler View
	RecyclerView.LayoutManager mLayoutManager; // Declaring Layout Manager as a
	TextView mTitle;
	DrawerLayout Drawer; // Declaring DrawerLayout
	RelativeLayout parentRelativeLayout;
	HorizontalListView listview;
	Animation animSlideUp, animSlideDown;
	List<ParentListModel> list;
	private ParentListModel selectedParent;
	ActionBarDrawerToggle mDrawerToggle; // Declaring Action Bar Drawer Toggle
	private ImageAdapter imageAdapter;
	private Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);

		// mTabHost = new FragmentTabHost(this);
		// mTabHost.setup(this, getSupportFragmentManager(),
		// R.id.menu_settings);
		// lLayout = new MyLinearLayout(this);

		InitView();
		// Configuration config = getResources().getConfiguration();
		// Toast.makeText(this, config.screenHeightDp+" "+config.screenWidthDp,
		// Toast.LENGTH_LONG).show();

		setSupportActionBar(toolbar);
		// SharedPreferences pref =
		// getApplicationContext().getSharedPreferences(
		// "loginPref", 0);
		//
		// Editor edit = pref.edit();
		// edit.putString("mobile", null);
		// edit.putString("otp", null);
		// edit.apply();
		// Log.d("mobile", "" + pref.getString("mobile", null));
		// Log.d("otp", "" + pref.getString("mobile", null));

		toolbar.inflateMenu(R.menu.toolbar_menu);
		// toolbar.setTitle("TouchKin");
		// toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		//
		// @Override
		// public boolean onMenuItemClick(MenuItem arg0) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "HI",
		// Toast.LENGTH_SHORT).show();
		// return true;
		// }
		// });
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Handle the menu item
				int id = item.getItemId();
				Toast.makeText(getApplicationContext(), "HI" + id,
						Toast.LENGTH_SHORT).show();
				if (id == R.id.parentNameMenu) {
					// Not implemented here
					toggleVissibility();
					return true;
				}
				if (id == R.id.parentIconMenu) {
					// Not implemented here
					toggleVissibility();
					return true;
				}
				// switch (id) {
				// case R.:
				//
				// break;
				//
				// default:
				// break;
				// }

				return true;
			}
		});

		// toolbar.in
		mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);// Assigning
																		// the
																		// RecyclerView
																		// Object
																		// to
																		// the
																		// xml
																		// View

		mRecyclerView.setHasFixedSize(true); // Letting the system know that the
		// list objects are of fixed
		// size
		MyAdapter.context = getApplicationContext();
		mAdapter = new MyAdapter(TITLES, NAME, PROFILE); // Creating

		mRecyclerView.setAdapter(mAdapter); // Setting the adapter to
		// RecyclerView

		mLayoutManager = new LinearLayoutManager(this); // Creating a layout
		// Manager

		mRecyclerView.setLayoutManager(mLayoutManager); // Setting the layout
		// Manager

		Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout); // Drawer

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

		mDrawerToggle.syncState(); // Finally we set the drawer toggle sync
		// State
		mTitle.setText("TouchKin");

		list = new ArrayList<ParentListModel>();

		// list.add(new ParentListModel("", true, "Mom", "1", "1"));
		// list.add(new ParentListModel("", false, "Dad", "2", "2"));
		// list.add(new ParentListModel("", false, "Uncle", "3", "3"));
		// list.add(new ParentListModel("", false, "Aunt", "4", "4"));
		// list.add(new ParentListModel("", false, "GM", "5", "5"));
		getParentList();

		listview.setOnItemClickListener(this);
		animSlideUp.setAnimationListener(this);
		animSlideDown.setAnimationListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		this.menu = menu;
		if (selectedParent != null) {
			setMenuTitle(selectedParent);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Toast.makeText(getApplicationContext(), "HI menu" + id,
				Toast.LENGTH_SHORT).show();
		if (id == R.id.parentNameMenu) {
			// Not implemented here
			toggleVissibility();
			return true;
		}
		if (id == R.id.parentIconMenu) {
			// Not implemented here
			toggleVissibility();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setTabColor(FragmentTabHost mTabHost) {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); i++) {
			this.mTabHost
					.getTabWidget()
					.getChildAt(i)
					.setBackgroundColor(getResources().getColor(R.color.tab_bg)); // unselected
			// TextView tv = (TextView)
			// this.mTabHost.getTabWidget().getChildAt(i)
			// .findViewById(android.R.id.title);
			// tv.setTextColor(Color.WHITE);
		}
		this.mTabHost
				.getTabWidget()
				.getChildAt(this.mTabHost.getCurrentTab())
				.setBackgroundColor(
						getResources().getColor(R.color.tab_selected)); // 2nd
																		// tab
																		// TextView
																		// tv =
																		// (TextView)
																		// this.mTabHost.getTabWidget().findViewById(
		// android.R.id.title);
		// tv.setTextColor(Color.WHITE); // selected

	}

	private void InitView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		Bundle b = new Bundle();
		b.putString("key", "Fake");
		mTabHost.addTab(
				mTabHost.newTabSpec("DashBoard").setIndicator("Dash Board"),
				Fragment1.class, b);

		mTabHost.setCurrentTab(0);
		Resources res = getResources();
		b = new Bundle();
		b.putString("key", "TouchKin");
		mTabHost.addTab(
				setIndicator(this, mTabHost.newTabSpec("KinBook"),
						R.color.tab_bg, "Kinbook", R.drawable.kinbook),
				TouchKinBookFragment.class, b);

		mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
		//

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

			parentRelativeLayout.setVisibility(View.VISIBLE);
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
		} else if (animation == animSlideUp) {
			parentRelativeLayout.setVisibility(View.INVISIBLE);
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

	public void getParentList() {

		JsonArrayRequest req = new JsonArrayRequest(
				"http://54.69.183.186:1340/user/care-receivers",
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray responseArray) {
						// TODO Auto-generated method stub
						// Log.d("Response Array", " " + responseArray);
						if (responseArray.length() > 0) {
							for (int i = 0; i < responseArray.length(); i++) {
								try {
									JSONObject obj = responseArray
											.getJSONObject(i);

									ParentListModel item = new ParentListModel();
									item.setParentId(obj.getString("id"));
									item.setParentName(obj.getString("nickname"));
//									item.setParentUserId(obj.getJSONObject(
//											"user").getString("id"));
									if (i == 0) {
										item.setIsSelected(true);
										selectedParent = item;
									} else {
										item.setIsSelected(false);
									}
									list.add(item);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							setMenuTitle(null);
						}
						list.add(new ParentListModel("", false, "", "", ""));
						imageAdapter = new ImageAdapter(DashBoardActivity.this,
								list);
						if (list.size() > 1) {
							selectedParent = list.get(0);
							setMenuTitle(selectedParent);
						}
						listview.setAdapter(imageAdapter);

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Toast.makeText(DashBoardActivity.this,
								error.getMessage(), Toast.LENGTH_SHORT).show();

					}

				});

		AppController.getInstance().addToRequestQueue(req);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		ParentListModel item = list.get(position);
		if (!item.getParentId().equals("")) {
			for (ParentListModel data : list) {

				if (data.equals(item)) {
					data.setIsSelected(true);
				} else {
					data.setIsSelected(false);
				}
			}
			setMenuTitle(item);
			listview.setAdapter(imageAdapter);
			mTabHost.setCurrentTab(0);
			selectedParent = item;
		} else {

		}
		toggleVissibility();

	}

	public void setMenuTitle(ParentListModel item) {
		MenuItem parentMenu = menu.findItem(R.id.parentNameMenu);
		if (item != null) {
			parentMenu.setTitle(item.getParentName());
		} else {
			parentMenu.setTitle("Add");
			MenuItem iconMenu = menu.findItem(R.id.parentIconMenu);
		}
	}

	public ParentListModel getSelectedParent() {
		return selectedParent;
	}

}

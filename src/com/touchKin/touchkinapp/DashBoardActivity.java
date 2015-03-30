package com.touchKin.touchkinapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.touchKin.touchkinapp.adapter.MyAdapter;
import com.touchKin.touckinapp.R;

public class DashBoardActivity extends ActionBarActivity {
	private FragmentTabHost mTabHost;
	String NAME = "Akash Bangad";
	String EMAIL = "akash.bangad@android4devs.com";
	int PROFILE = R.drawable.mom;
	String TITLES[] = { "My Family", "My Accounts", "Upgrade", "Terms of Use",
			"Contact Us" };
	private Toolbar toolbar; // Declaring the Toolbar Object

	RecyclerView mRecyclerView; // Declaring RecyclerView
	RecyclerView.Adapter mAdapter; // Declaring Adapter For Recycler View
	RecyclerView.LayoutManager mLayoutManager; // Declaring Layout Manager as a
												// linear layout manager
	DrawerLayout Drawer; // Declaring DrawerLayout

	ActionBarDrawerToggle mDrawerToggle; // Declaring Action Bar Drawer Toggle

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);

		// mTabHost = new FragmentTabHost(this);
		// mTabHost.setup(this, getSupportFragmentManager(),
		// R.id.menu_settings);
		// lLayout = new MyLinearLayout(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				setTabColor(mTabHost);

			}

		});

		Bundle b = new Bundle();
		b.putString("key", "Fake");
		mTabHost.addTab(
				mTabHost.newTabSpec("DashBoard").setIndicator("Dash Board"),
				Fragment1.class, b);
		
		mTabHost.setCurrentTab(0);
		
		b = new Bundle();
		b.putString("key", "DashBoard");
		mTabHost.addTab(
				mTabHost.newTabSpec("DashBoard").setIndicator("Dash Board"),
				Fragment1.class, b);
		mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
		//
		mTabHost.setBackgroundColor(getResources().getColor(R.color.tab_bg));
		b = new Bundle();
		b.putString("key", "Activity");
		mTabHost.addTab(mTabHost.newTabSpec("Activity")
				.setIndicator("Activity"), Fragment3.class, b);
		mTabHost.setBackgroundColor(getResources().getColor(R.color.tab_bg));
		b = new Bundle();
		b.putString("key", "Settings");
		mTabHost.addTab(mTabHost.newTabSpec("settings")
				.setIndicator("Settings"), Fragment3.class, b);
		mTabHost.setBackgroundColor(getResources().getColor(R.color.tab_bg));
		b = new Bundle();
		b.putString("key", "ER Plan");
		mTabHost.addTab(mTabHost.newTabSpec("er Plan").setIndicator("ER Plan"),
				Fragment2.class, b);
		mTabHost.setBackgroundColor(getResources().getColor(R.color.tab_bg));
		toolbar = (Toolbar) findViewById(R.id.tool_bar);

		setSupportActionBar(toolbar);
		// toolbar.inflateMenu(R.menu.dash_board);
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
		// toolbar.setOnMenuItemClickListener(new
		// Toolbar.OnMenuItemClickListener() {
		// @Override
		// public boolean onMenuItemClick(MenuItem item) {
		// // Handle the menu item
		// Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_LONG)
		// .show();
		//
		// return true;
		// }
		// });

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
		mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar,
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
		mDrawerToggle.syncState(); // Finally we set the drawer toggle sync
		// State

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.parentName) {
		// return true;
		// }
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
}

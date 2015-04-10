package com.touchKin.touchkinapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.touchKin.touckinapp.R;

public class DashBoardActivityFragment extends Fragment {
	// newInstance constructor for creating fragment with arguments
	public static DashBoardActivityFragment newInstance(int page, String title) {
		DashBoardActivityFragment dashBoardActivityFragment = new DashBoardActivityFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		dashBoardActivityFragment.setArguments(args);
		return dashBoardActivityFragment;
	}

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dashboard_activity_screen,
				container, false);
		final Resources resources = getResources();

		final PieGraph pg = (PieGraph) view.findViewById(R.id.piegraph);
		PieSlice slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		slice.setTitle("first");
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(1);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_done));
		slice.setValue(1);
		pg.addSlice(slice);

		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.daily_prog_left));
		slice.setValue(15);
		pg.addSlice(slice);
		return view;
	}
}

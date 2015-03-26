package com.touchKin.touchkinapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.touchKin.touckinapp.R;

public class TouchFragment extends Fragment {

	// newInstance constructor for creating fragment with arguments
	public static TouchFragment newInstance(int page, String title) {
		TouchFragment touchFragment = new TouchFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		touchFragment.setArguments(args);
		return touchFragment;
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
		View view = inflater.inflate(R.layout.dashboard_touch_screen,
				container, false);
		final Resources resources = getResources();
		final PieGraph pg = (PieGraph) view.findViewById(R.id.piegraph);
		PieSlice slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.text_orange));
		slice.setValue(1);
		slice.setTitle("first");
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.text_grey));
		slice.setValue(1);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.text_orange));
		slice.setValue(1);
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.text_orange));
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.text_grey));
		slice.setValue(1);
		pg.addSlice(slice);
		slice.setValue(26);
		pg.addSlice(slice);
		return view;
	}
}

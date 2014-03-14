package com.antares.slidedrawer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antares.slidedrawer.R;

public class SettingsFragment extends Fragment {

	public SettingsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings, container, false);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		return rootView;
	}
}

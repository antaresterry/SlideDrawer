package com.antares.slidedrawer.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.antares.slidedrawer.HomeActivity;
import com.antares.slidedrawer.NewTaskActivity;
import com.antares.slidedrawer.R;

public class InboxFragment extends Fragment {

	private ListView lvHome;
	private List<String> tasks;
	private ArrayAdapter<String> homeAdapter;

	public InboxFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		tasks = new ArrayList<String>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
				|| (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			// on a large screen device ...
			menu.findItem(R.id.action_new)
					.setVisible(!HomeActivity.sidebarOpen);
		} else {
			menu.findItem(R.id.action_new).setVisible(
					!(HomeActivity.drawerOpen || HomeActivity.sidebarOpen));
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			Intent intent = new Intent(getActivity(), NewTaskActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.inbox, container, false);
		lvHome = (ListView) rootView.findViewById(R.id.lvInbox);
		homeAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.task_list_item, tasks);
		lvHome.setAdapter(homeAdapter);
		lvHome.setEmptyView(rootView.findViewById(R.id.emptyInbox));
		return rootView;
	}

}

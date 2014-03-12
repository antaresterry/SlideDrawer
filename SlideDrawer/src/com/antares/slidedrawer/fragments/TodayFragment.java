package com.antares.slidedrawer.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.antares.slidedrawer.HomeActivity;
import com.antares.slidedrawer.NewTaskActivity;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.utils.Utils;

public class TodayFragment extends Fragment {

	private ListView lvToday;
	private List<String> tasks;
	private Dialog newDialog;
	private ArrayAdapter<String> todayAdapter;

	public TodayFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		tasks = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		int date = c.get(Calendar.DATE);
		int day = c.get(Calendar.DAY_OF_WEEK);
		int month = c.get(Calendar.MONTH);
		tasks.add("Today " + Utils.getDay(day) + " " + Utils.getMonth(month)
				+ " " + date);
		newDialog = new Dialog(getActivity());
		newDialog.setContentView(R.layout.new_dialog);
		((Button) newDialog.findViewById(R.id.buttonOk))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (((EditText) newDialog.findViewById(R.id.etTask))
								.getText().length() > 0) {
							tasks.add(((EditText) newDialog
									.findViewById(R.id.etTask)).getText()
									.toString());
							((EditText) newDialog.findViewById(R.id.etTask))
									.setText(null);
							todayAdapter.notifyDataSetChanged();
						}
						newDialog.dismiss();
					}

				});
		newDialog.setTitle("New Task");
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
		View rootView = inflater.inflate(R.layout.today, container, false);
		lvToday = (ListView) rootView.findViewById(R.id.lvToday);
		todayAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.task_list_item, tasks);
		lvToday.setAdapter(todayAdapter);
		lvToday.setEmptyView(rootView.findViewById(R.id.emptyToday));
		return rootView;
	}

}

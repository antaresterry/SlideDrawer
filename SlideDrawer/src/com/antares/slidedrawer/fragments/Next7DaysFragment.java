package com.antares.slidedrawer.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.antares.slidedrawer.HomeActivity;
import com.antares.slidedrawer.NewTaskActivity;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.adapter.TaskAdapter;
import com.antares.slidedrawer.data.ChildItem;
import com.antares.slidedrawer.utils.Utils;

import de.timroes.swipetodismiss.SwipeDismissList;
import de.timroes.swipetodismiss.SwipeDismissList.UndoMode;

public class Next7DaysFragment extends Fragment {

	private ListView lvToday;
	private ListView lvTomorrow;
	private ListView lv3rdDay;
	private ListView lv4thDay;
	private ListView lv5thDay;
	private ListView lv6thDay;
	private ListView lv7thDay;
	private List<ChildItem> tasksToday;
	private List<ChildItem> tasksTomorrow;
	private List<ChildItem> tasks3rdDay;
	private List<ChildItem> tasks4thDay;
	private List<ChildItem> tasks5thDay;
	private List<ChildItem> tasks6thDay;
	private List<ChildItem> tasks7thDay;
	private Dialog newDialog;
	private TaskAdapter todayAdapter;
	private TaskAdapter tomorrowAdapter;
	private TaskAdapter thirdDayAdapter;
	private TaskAdapter fourthDayAdapter;
	private TaskAdapter fifthDayAdapter;
	private TaskAdapter sixthDayAdapter;
	private TaskAdapter seventhDayAdapter;
	private View panelHeaderToday;
	private View panelHeaderTomorrow;
	private View panelHeader3rdDay;
	private View panelHeader4thDay;
	private View panelHeader5thDay;
	private View panelHeader6thDay;
	private View panelHeader7thDay;

	public Next7DaysFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		tasksToday = new ArrayList<ChildItem>();
		tasksTomorrow = new ArrayList<ChildItem>();
		tasks3rdDay = new ArrayList<ChildItem>();
		tasks4thDay = new ArrayList<ChildItem>();
		tasks5thDay = new ArrayList<ChildItem>();
		tasks6thDay = new ArrayList<ChildItem>();
		tasks7thDay = new ArrayList<ChildItem>();
		Calendar c = Calendar.getInstance();
		int date = c.get(Calendar.DATE);
		int day = c.get(Calendar.DAY_OF_WEEK);
		int month = c.get(Calendar.MONTH);
		int dayOfMonth = c.getActualMaximum(Calendar.DATE);
		panelHeaderToday = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		panelHeaderTomorrow = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		panelHeader3rdDay = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		panelHeader4thDay = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		panelHeader5thDay = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		panelHeader6thDay = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		panelHeader7thDay = getActivity().getLayoutInflater().inflate(
				R.layout.panel_header_task, null);
		tasksToday.add(new ChildItem("Test 1", "Today", "Inbox"));
		if (tasksToday.size() > 0) {
			((TextView) panelHeaderToday.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeaderToday.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeaderToday.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeaderToday.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		if (tasksTomorrow.size() > 0) {
			((TextView) panelHeaderTomorrow.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeaderTomorrow.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeaderTomorrow.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeaderTomorrow.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		if (tasks3rdDay.size() > 0) {
			((TextView) panelHeader3rdDay.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeader3rdDay.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeader3rdDay.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeader3rdDay.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		if (tasks4thDay.size() > 0) {
			((TextView) panelHeader4thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeader4thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeader4thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeader4thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		if (tasks5thDay.size() > 0) {
			((TextView) panelHeader5thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeader5thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeader5thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeader5thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		if (tasks6thDay.size() > 0) {
			((TextView) panelHeader6thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeader6thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeader6thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeader6thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		if (tasks7thDay.size() > 0) {
			((TextView) panelHeader7thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.BLACK);
			((TextView) panelHeader7thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.DKGRAY);
		} else {
			((TextView) panelHeader7thDay.findViewById(R.id.tvDay))
					.setTextColor(Color.DKGRAY);
			((TextView) panelHeader7thDay.findViewById(R.id.tvDate))
					.setTextColor(Color.GRAY);
		}
		((TextView) panelHeaderToday.findViewById(R.id.tvDay)).setText("Today");
		((TextView) panelHeaderToday.findViewById(R.id.tvDate)).setText(Utils
				.getDay(day) + " " + Utils.getMonth(month) + " " + date);
		((TextView) panelHeaderTomorrow.findViewById(R.id.tvDay))
				.setText("Tomorrow");
		((TextView) panelHeaderTomorrow.findViewById(R.id.tvDate))
				.setText(Utils
						.getDay((day + 1) > Calendar.SATURDAY ? ((day + 1) - 7)
								: (day + 1))
						+ " "
						+ Utils.getMonth(((date + 1) > dayOfMonth) ? (((month + 1) > Calendar.DECEMBER) ? ((month) - Calendar.DECEMBER)
								: (month + 1))
								: month)
						+ " "
						+ (((date + 1) > dayOfMonth) ? ((date + 1) - dayOfMonth)
								: (date + 1)));
		((TextView) panelHeader3rdDay.findViewById(R.id.tvDay)).setText(Utils
				.getLongDay((day + 2) > Calendar.SATURDAY ? ((day + 2) - 7)
						: (day + 2)));
		((TextView) panelHeader3rdDay.findViewById(R.id.tvDate))
				.setText(Utils
						.getMonth(((date + 2) > dayOfMonth) ? (((month + 1) > Calendar.DECEMBER) ? ((month) - Calendar.DECEMBER)
								: (month + 1))
								: month)
						+ " "
						+ (((date + 2) > dayOfMonth) ? ((date + 2) - dayOfMonth)
								: (date + 2)));
		((TextView) panelHeader4thDay.findViewById(R.id.tvDay)).setText(Utils
				.getLongDay((day + 3) > Calendar.SATURDAY ? ((day + 3) - 7)
						: (day + 3)));
		((TextView) panelHeader4thDay.findViewById(R.id.tvDate))
				.setText(Utils
						.getMonth(((date + 3) > dayOfMonth) ? (((month + 1) > Calendar.DECEMBER) ? ((month) - Calendar.DECEMBER)
								: (month + 1))
								: month)
						+ " "
						+ (((date + 3) > dayOfMonth) ? ((date + 3) - dayOfMonth)
								: (date + 3)));
		((TextView) panelHeader5thDay.findViewById(R.id.tvDay)).setText(Utils
				.getLongDay((day + 4) > Calendar.SATURDAY ? ((day + 4) - 7)
						: (day + 4)));
		((TextView) panelHeader5thDay.findViewById(R.id.tvDate))
				.setText(Utils
						.getMonth(((date + 4) > dayOfMonth) ? (((month + 1) > Calendar.DECEMBER) ? ((month) - Calendar.DECEMBER)
								: (month + 1))
								: month)
						+ " "
						+ (((date + 4) > dayOfMonth) ? ((date + 4) - dayOfMonth)
								: (date + 4)));
		((TextView) panelHeader6thDay.findViewById(R.id.tvDay)).setText(Utils
				.getLongDay((day + 5) > Calendar.SATURDAY ? ((day + 5) - 7)
						: (day + 5)));
		((TextView) panelHeader6thDay.findViewById(R.id.tvDate))
				.setText(Utils
						.getMonth(((date + 5) > dayOfMonth) ? (((month + 1) > Calendar.DECEMBER) ? ((month) - Calendar.DECEMBER)
								: (month + 1))
								: month)
						+ " "
						+ (((date + 5) > dayOfMonth) ? ((date + 5) - dayOfMonth)
								: (date + 5)));
		((TextView) panelHeader7thDay.findViewById(R.id.tvDay)).setText(Utils
				.getLongDay((day + 6) > Calendar.SATURDAY ? ((day + 6) - 7)
						: (day + 6)));
		((TextView) panelHeader7thDay.findViewById(R.id.tvDate))
				.setText(Utils
						.getMonth(((date + 6) > dayOfMonth) ? (((month + 1) > Calendar.DECEMBER) ? ((month) - Calendar.DECEMBER)
								: (month + 1))
								: month)
						+ " "
						+ (((date + 6) > dayOfMonth) ? ((date + 6) - dayOfMonth)
								: (date + 6)));
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
		View rootView = inflater.inflate(R.layout.next7days, container, false);
		lvToday = (ListView) rootView.findViewById(R.id.lvToday);
		lvTomorrow = (ListView) rootView.findViewById(R.id.lvTomorrow);
		lv3rdDay = (ListView) rootView.findViewById(R.id.lv3rdDay);
		lv4thDay = (ListView) rootView.findViewById(R.id.lv4thDay);
		lv5thDay = (ListView) rootView.findViewById(R.id.lv5thDay);
		lv6thDay = (ListView) rootView.findViewById(R.id.lv6thDay);
		lv7thDay = (ListView) rootView.findViewById(R.id.lv7thDay);
		todayAdapter = new TaskAdapter(tasksToday, getActivity());
		tomorrowAdapter = new TaskAdapter(tasksTomorrow, getActivity());
		thirdDayAdapter = new TaskAdapter(tasks3rdDay, getActivity());
		fourthDayAdapter = new TaskAdapter(tasks4thDay, getActivity());
		fifthDayAdapter = new TaskAdapter(tasks5thDay, getActivity());
		sixthDayAdapter = new TaskAdapter(tasks6thDay, getActivity());
		seventhDayAdapter = new TaskAdapter(tasks7thDay, getActivity());
		lvToday.setAdapter(todayAdapter);
		lvTomorrow.setAdapter(tomorrowAdapter);
		lv3rdDay.setAdapter(thirdDayAdapter);
		lv4thDay.setAdapter(fourthDayAdapter);
		lv5thDay.setAdapter(fifthDayAdapter);
		lv6thDay.setAdapter(sixthDayAdapter);
		lv7thDay.setAdapter(seventhDayAdapter);
		lvToday.addHeaderView(panelHeaderToday);
		lvTomorrow.addHeaderView(panelHeaderTomorrow);
		lv3rdDay.addHeaderView(panelHeader3rdDay);
		lv4thDay.addHeaderView(panelHeader4thDay);
		lv5thDay.addHeaderView(panelHeader5thDay);
		lv6thDay.addHeaderView(panelHeader6thDay);
		lv7thDay.addHeaderView(panelHeader7thDay);
		rootView.findViewById(R.id.emptyNext7Days).setVisibility(View.GONE);
		SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {
			public SwipeDismissList.Undoable onDismiss(AbsListView listView,
					final int position) {
				// Delete the item from your adapter (sample code):
				final ChildItem itemToDelete = todayAdapter
						.getItem(position - 1);
				todayAdapter.remove(itemToDelete);
				return new SwipeDismissList.Undoable() {
					public void undo() {
						// Return the item at its previous position again
						todayAdapter.insert(itemToDelete, position - 1);
					}
				};
			}
		};
		UndoMode mode = SwipeDismissList.UndoMode.SINGLE_UNDO;
		SwipeDismissList swipeList = new SwipeDismissList(lvToday, callback,
				mode);
		return rootView;
	}

}

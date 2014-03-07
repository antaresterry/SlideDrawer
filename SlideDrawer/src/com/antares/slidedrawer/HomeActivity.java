package com.antares.slidedrawer;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.antares.slidedrawer.adapter.DrawerAdapter;
import com.antares.slidedrawer.data.DrawerItem;
import com.antares.slidedrawer.fragments.AboutFragment;
import com.antares.slidedrawer.fragments.HomeFragment;
import com.antares.slidedrawer.fragments.SettingsFragment;
import com.antares.slidedrawer.utils.UrlComposer;
import com.antares.slidedrawer.utils.VolleySingleton;

public class HomeActivity extends ActionBarActivity {

	private String[] drawerTitles;
	private Integer[] drawerIcons;
	private List<DrawerItem> drawerItems;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private CharSequence title;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence drawerTitle;
	private DrawerAdapter drawerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide);
		drawerTitles = getResources().getStringArray(R.array.menu_drawer);
		drawerIcons = new Integer[] { R.drawable.social_group,
				R.drawable.action_settings, R.drawable.action_about };
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerItems = new ArrayList<DrawerItem>();
		for (int i = 0; i < drawerTitles.length; i++) {
			drawerItems.add(i, new DrawerItem(drawerTitles[i], drawerIcons[i]));
		}
		View panelTop = getLayoutInflater().inflate(R.layout.panel_top, null);
		((NetworkImageView) panelTop.findViewById(R.id.ivAvatar)).setImageUrl(
				UrlComposer.composeUrlBlogAvatar(
						Constants.userInfo.response.user.base_hostname, 128),
				VolleySingleton.getInstance(this).getImageLoader());
		((TextView) panelTop.findViewById(R.id.tvBlogName))
				.setText(Constants.userInfo.response.user.blogs[Constants.userInfo.response.user.primary].title);
		((TextView) panelTop.findViewById(R.id.tvBlogDescription))
				.setText("Followers: "
						+ Constants.userInfo.response.user.blogs[Constants.userInfo.response.user.primary].followers);

		drawerList.addHeaderView(panelTop, null, false);
		// Set the adapter for the list view
		drawerAdapter = new DrawerAdapter(drawerItems, this);
		drawerList.setAdapter(drawerAdapter);
		// Set the list's click listener
		drawerList.setOnItemClickListener(new DrawerItemClickListener());

		title = getTitle();
		drawerTitle = getResources().getString(R.string.drawer_title);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(title);
				supportInvalidateOptionsMenu();
				// creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(drawerTitle);
				supportInvalidateOptionsMenu();
				// creates call to onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(drawerToggle);
		drawerList.setItemChecked(1, true);
		selectItem(1);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		menu.findItem(R.id.action_exit).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(
				@SuppressWarnings("rawtypes") AdapterView parent, View view,
				int position, long id) {
			if (position > 0)
				selectItem(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment;
		FragmentManager fragmentManager;
		switch (position) {
		case 1:
			fragment = new HomeFragment();

			// Insert the fragment by replacing any existing fragment
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position - 1]);
			drawerLayout.closeDrawer(drawerList);
			break;
		case 2:
			fragment = new SettingsFragment();

			// Insert the fragment by replacing any existing fragment
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position - 1]);
			drawerLayout.closeDrawer(drawerList);
			break;
		case 3:
			fragment = new AboutFragment();

			// Insert the fragment by replacing any existing fragment
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position - 1]);
			drawerLayout.closeDrawer(drawerList);
			break;
		default:
			break;
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		this.title = title;
		getSupportActionBar().setTitle(this.title);
	}

	public void onClick(View vi) {
		int viewId = vi.getId();
		switch (viewId) {
		case R.id.logoutButton:
			SharedPreferences oAuth = this.getSharedPreferences(
					Constants.PREF_NAME, 0);
			Editor editor = oAuth.edit();
			editor.clear().commit();
			finish();
			break;
		default:
			break;
		}
	}
}

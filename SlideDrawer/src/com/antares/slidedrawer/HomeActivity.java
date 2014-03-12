package com.antares.slidedrawer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.antares.slidedrawer.adapter.ExpandableDrawerAdapter;
import com.antares.slidedrawer.data.DrawerChildItem;
import com.antares.slidedrawer.data.DrawerItem;
import com.antares.slidedrawer.fragments.InboxFragment;
import com.antares.slidedrawer.fragments.Next7DaysFragment;
import com.antares.slidedrawer.fragments.TodayFragment;

public class HomeActivity extends ActionBarActivity {

	private String[] drawerTitles;
	private Integer[] drawerIcons;
	private List<DrawerItem> drawerItems;
	private DrawerLayout drawerLayout;
	private LinearLayout drawerFragment;
	private ExpandableListView drawerList;
	private CharSequence title;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence drawerTitle;
	private ExpandableDrawerAdapter drawerAdapter;
	private Animation fadeIn, fadeOut;
	private DrawerLayout sidebarLayout;
	private ActionBarDrawerToggle sidebarToggle;
	private ListView sidebarList;
	private ArrayAdapter<String> sidebarAdapter;
	private View sidebarView;
	private String[] childTitles;
	private Integer[] childIcons;
	private int prevPos = 0;
	public static boolean sidebarOpen = false;
	public static boolean drawerOpen = false;
	public static boolean largeScreen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
				|| (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
			largeScreen = true;
		else
			largeScreen = false;
		setContentView(R.layout.slide);
		fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		drawerTitles = getResources().getStringArray(R.array.menu_drawer);
		childTitles = getResources().getStringArray(R.array.child_drawer);
		drawerIcons = new Integer[] { R.drawable.content_read,
				R.drawable.content_event, R.drawable.content_event,
				R.drawable.content_event, R.drawable.content_event,
				R.drawable.action_about };
		childIcons = new Integer[] { R.drawable.navigation_accept,
				R.drawable.navigation_accept, R.drawable.navigation_accept,
				R.drawable.navigation_accept, R.drawable.navigation_accept,
				R.drawable.navigation_accept, R.drawable.navigation_accept,
				R.drawable.navigation_accept };
		drawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		sidebarView = findViewById(R.id.right_drawer);
		sidebarList = (ListView) findViewById(R.id.lvNotification);
		sidebarList.setEmptyView(findViewById(R.id.emptyNotification));
		sidebarAdapter = new ArrayAdapter<String>(this,
				R.layout.task_list_item, new ArrayList<String>());
		sidebarList.setAdapter(sidebarAdapter);
		drawerItems = new ArrayList<DrawerItem>();
		for (int i = 0; i < drawerTitles.length; i++) {
			if (i < (drawerTitles.length - 1))
				drawerItems.add(i, new DrawerItem(drawerTitles[i],
						drawerIcons[i], null));
			else {
				List<DrawerChildItem> drawerChildItems = new ArrayList<DrawerChildItem>();
				for (int j = 0; j < childTitles.length; j++) {
					drawerChildItems.add(new DrawerChildItem(childTitles[j],
							childIcons[j]));
				}
				drawerItems.add(i, new DrawerItem(drawerTitles[i],
						drawerIcons[i], drawerChildItems));

			}
		}
		// Set the adapter for the list view
		drawerAdapter = new ExpandableDrawerAdapter(drawerItems, this);
		drawerList.setAdapter(drawerAdapter);
		// Set the list's click listener
		drawerList.setOnGroupClickListener(new DrawerItemClickListener());

		sidebarLayout = (DrawerLayout) findViewById(R.id.sidebar_layout);
		sidebarToggle = new ActionBarDrawerToggle(this, sidebarLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			float mPreviousOffset = 0f;

			/**
			 * Called when a drawer has settled in a completely closed state.
			 */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				sidebarOpen = false;
				if (largeScreen) {

				} else
					drawerLayout
							.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				supportInvalidateOptionsMenu();
				// creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				sidebarOpen = true;
				if (largeScreen) {

				} else
					drawerLayout
							.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				supportInvalidateOptionsMenu();
				// creates call to onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				if (slideOffset > mPreviousOffset && !sidebarOpen) {
					sidebarOpen = true;
					supportInvalidateOptionsMenu();
				} else if (mPreviousOffset > slideOffset && slideOffset <= 0f
						&& sidebarOpen) {
					sidebarOpen = false;
					supportInvalidateOptionsMenu();
				}
				mPreviousOffset = slideOffset;
			}
		};

		// Set the drawer toggle as the DrawerListener
		sidebarLayout.setDrawerListener(sidebarToggle);
		if (largeScreen) {
			// on a large screen device ...
			drawerFragment = (LinearLayout) findViewById(R.id.drawer_layout);
		} else {
			drawerTitle = getString(R.string.drawer_title);
			drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
					R.drawable.ic_drawer, R.string.drawer_open,
					R.string.drawer_close) {
				float mPreviousOffset = 0f;

				/**
				 * Called when a drawer has settled in a completely closed
				 * state.
				 */
				public void onDrawerClosed(View view) {
					super.onDrawerClosed(view);
					drawerOpen = false;
					getSupportActionBar().setTitle(title);
					sidebarLayout
							.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
					supportInvalidateOptionsMenu();
					// creates call to onPrepareOptionsMenu()
				}

				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					drawerOpen = true;
					getSupportActionBar().setTitle(drawerTitle);
					sidebarLayout
							.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					supportInvalidateOptionsMenu();
					// creates call to onPrepareOptionsMenu()
				}

				@Override
				public void onDrawerSlide(View arg0, float slideOffset) {
					super.onDrawerSlide(arg0, slideOffset);
					if (slideOffset > mPreviousOffset && !drawerOpen) {
						drawerOpen = true;
						supportInvalidateOptionsMenu();
					} else if (mPreviousOffset > slideOffset
							&& slideOffset <= 0f && drawerOpen) {
						drawerOpen = false;
						supportInvalidateOptionsMenu();
					}
					mPreviousOffset = slideOffset;

				}
			};

			// Set the drawer toggle as the DrawerListener
			drawerLayout.setDrawerListener(drawerToggle);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		getOverflowMenu();
		selectItem(0);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (largeScreen) {

		} else
			drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (largeScreen) {

		} else
			drawerToggle.onConfigurationChanged(newConfig);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		if (largeScreen) {
			menu.findItem(R.id.action_exit).setVisible(!sidebarOpen);
		} else {
			menu.findItem(R.id.action_exit).setVisible(
					!(drawerOpen || sidebarOpen));
			menu.findItem(R.id.action_notification).setVisible(!drawerOpen);
		}
		if (sidebarOpen) {
			menu.findItem(R.id.action_notification).setIcon(
					R.drawable.navigation_cancel);
			MenuItemCompat.setShowAsAction(
					menu.findItem(R.id.action_notification),
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		} else {
			menu.findItem(R.id.action_notification).setIcon(
					R.drawable.make_available_offline);
			MenuItemCompat.setShowAsAction(
					menu.findItem(R.id.action_notification),
					MenuItemCompat.SHOW_AS_ACTION_NEVER);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private class DrawerItemClickListener implements
			ExpandableListView.OnGroupClickListener {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			if (groupPosition < 3) {
				if (groupPosition != prevPos) {
					selectItem(groupPosition);
					prevPos = groupPosition;
				}
			}
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (largeScreen) {

		} else {
			if (drawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_exit:
			finish();
			return true;
		case R.id.action_notification:
			boolean sidebarOpen = sidebarLayout.isDrawerOpen(sidebarView);
			if (sidebarOpen) {
				sidebarLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				sidebarLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			} else {
				sidebarLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
				sidebarLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		Animation animation = null;

		switch (4) {
		case 1:
			animation = new TranslateAnimation(getResources()
					.getDisplayMetrics().widthPixels / 2, 0, 0, 0);
			break;

		case 2:
			animation = new TranslateAnimation(0, 0, getResources()
					.getDisplayMetrics().heightPixels, 0);
			break;

		case 3:
			animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0,
					(float) 1.0);
			break;

		case 4:
			animation = fadeIn;
			break;
		case 5:
			// non animation
			animation = new Animation() {
			};
			break;
		}

		animation.setDuration(750);
		findViewById(R.id.content_frame).startAnimation(animation);
		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment;
		FragmentManager fragmentManager;
		switch (position) {
		case 0:
			fragment = new InboxFragment();

			// Insert the fragment by replacing any existing fragment
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position]);
			if (largeScreen) {

			} else
				drawerLayout.closeDrawer(drawerList);
			break;
		case 1:
			fragment = new TodayFragment();

			// Insert the fragment by replacing any existing fragment
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position]);
			if (largeScreen) {

			} else
				drawerLayout.closeDrawer(drawerList);
			break;
		case 2:
			fragment = new Next7DaysFragment();

			// Insert the fragment by replacing any existing fragment
			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			drawerList.setItemChecked(position, true);
			setTitle(drawerTitles[position]);
			if (largeScreen) {

			} else
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
}

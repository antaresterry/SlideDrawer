package com.antares.slidedrawer.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.antares.slidedrawer.Constants;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.adapter.DashboardAdapter;
import com.antares.slidedrawer.data.DashboardPostsVO;
import com.antares.slidedrawer.utils.UrlComposer;
import com.google.gson.Gson;

public class HomeFragment extends Fragment implements OnClickListener,
		OnRefreshListener, OnScrollListener, OnNavigationListener {

	private static final int REQUEST_DASHBOARD = 0;
	private static final int POST_LIKE = 1;
	private static final int POST_REBLOG = 2;
	private static final int REQUEST_DASHBOARD_LOAD_MORE = 3;
	private static OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			Constants.TUMBLR_CONSUMER_KEY, Constants.TUMBLR_CONSUMER_SECRET);
	private static Set<DashboardPostsVO> dashboardPosts;
	private static List<DashboardPostsVO> dashboardPostss;
	private static ListView lvDashboard;
	private static DashboardAdapter dashboardAdapter;
	private static String lastId = "";
	private PullToRefreshLayout mPullToRefreshLayout;
	private View panelFooter;
	private int threshold = 0;
	private static boolean isLoading = false;
	private String type = null;

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home, container, false);
		isLoading = false;
		// Now find the PullToRefreshLayout to setup
		mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.ptr_layout);
		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);
		// ((ActionBarActivity)
		// getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] dropdownValues = getResources().getStringArray(
				R.array.type);

		// Specify a SpinnerAdapter to populate the dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				((ActionBarActivity) getActivity()).getSupportActionBar()
						.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				dropdownValues);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set up the dropdown list navigation in the action bar.
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setListNavigationCallbacks(adapter, this);

		// use getActionBar().getThemedContext() to ensure
		// that the text color is always appropriate for the action bar
		// background rather than the activity background.
		dashboardPosts = new HashSet<DashboardPostsVO>();
		dashboardPostss = new ArrayList<DashboardPostsVO>();
		lvDashboard = (ListView) rootView.findViewById(R.id.lvDashboard);
		dashboardAdapter = new DashboardAdapter(dashboardPostss, getActivity(),
				this);
		lvDashboard.setAdapter(dashboardAdapter);
		panelFooter = inflater.inflate(R.layout.panel_footer, null);
		lvDashboard.addFooterView(panelFooter);
		lvDashboard.setOnScrollListener(this);
		SharedPreferences oAuth = getActivity().getSharedPreferences(
				Constants.PREF_NAME, 0);
		consumer.setTokenWithSecret(
				oAuth.getString(Constants.PREF_PARAM_TOKEN, null),
				oAuth.getString(Constants.PREF_PARAM_TOKEN_SECRET, null));
		// Request User Dashboard
		new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
				.execute(UrlComposer.composeUrlUserDashboard(type,
						(Boolean) true, (Boolean) true));
		rootView.findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
		lvDashboard.setVisibility(View.GONE);
		return rootView;
	}

	@SuppressLint("NewApi")
	public void onTaskSuccess(String result, int id) {
		switch (id) {
		case REQUEST_DASHBOARD:
			dashboardPosts.clear();
			dashboardPostss.clear();
			Log.v("JSON", result);
			try {
				JSONObject json = new JSONObject(result);
				JSONObject meta = json.getJSONObject("meta");
				Toast.makeText(getActivity(), meta.getString("msg"),
						Toast.LENGTH_SHORT).show();
				JSONObject response = json.getJSONObject("response");
				JSONArray posts = response.getJSONArray("posts");
				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = posts.getJSONObject(i);
					Gson gson = new Gson();
					DashboardPostsVO dashboardPost = gson.fromJson(
							post.toString(), DashboardPostsVO.class);
					if (dashboardPosts.add(dashboardPost))
						dashboardPostss.add(dashboardPost);
				}
				dashboardAdapter.notifyDataSetChanged();
				getActivity().findViewById(R.id.pbHome)
						.setVisibility(View.GONE);
				lvDashboard.setVisibility(View.VISIBLE);
				int lastPos = dashboardAdapter.getItemPosition(lastId);
				if (lastPos >= 0) {
					if (android.os.Build.VERSION.SDK_INT >= 11) {
						lvDashboard.smoothScrollToPositionFromTop(lastPos, 0);
						lvDashboard.setSelection(lastPos);
					} else {
						lvDashboard.setSelectionFromTop(lastPos, 0);
					}
				}
				// Notify PullToRefreshLayout that the refresh has finished
				mPullToRefreshLayout.setRefreshComplete();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case POST_LIKE:
			Log.v("JSON", result);
			try {
				JSONObject json = new JSONObject(result);
				JSONObject meta = json.getJSONObject("meta");
				Toast.makeText(getActivity(), meta.getString("msg"),
						Toast.LENGTH_SHORT).show();
				if (meta.getString("msg").equals("OK")) {
					// Request User Dashboard
					new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
							.execute(UrlComposer.composeUrlUserDashboard(type,
									(Boolean) true, (Boolean) true));
					getActivity().findViewById(R.id.pbHome).setVisibility(
							View.VISIBLE);
					lvDashboard.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case POST_REBLOG:
			Log.v("JSON", result);
			try {
				JSONObject json = new JSONObject(result);
				JSONObject meta = json.getJSONObject("meta");
				Toast.makeText(getActivity(), meta.getString("msg"),
						Toast.LENGTH_SHORT).show();
				if (meta.getString("msg").equals("OK")) {
					// Request User Dashboard
					new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
							.execute(UrlComposer.composeUrlUserDashboard(type,
									(Boolean) true, (Boolean) true));
					getActivity().findViewById(R.id.pbHome).setVisibility(
							View.VISIBLE);
					lvDashboard.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case REQUEST_DASHBOARD_LOAD_MORE:
			Log.v("JSON", result);
			try {
				JSONObject json = new JSONObject(result);
				JSONObject meta = json.getJSONObject("meta");
				Toast.makeText(getActivity(), meta.getString("msg"),
						Toast.LENGTH_SHORT).show();
				JSONObject response = json.getJSONObject("response");
				JSONArray posts = response.getJSONArray("posts");
				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = posts.getJSONObject(i);
					Gson gson = new Gson();
					DashboardPostsVO dashboardPost = gson.fromJson(
							post.toString(), DashboardPostsVO.class);
					if (dashboardPosts.add(dashboardPost))
						dashboardPostss.add(dashboardPost);
				}
				dashboardAdapter.notifyDataSetChanged();
				isLoading = false;
				Log.v("Load More", ((Boolean) isLoading).toString());
				// lvDashboard.setAdapter(dashboardAdapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	public class TumblrRequestLoader extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("unused")
		private Activity context;
		private int id;

		public TumblrRequestLoader(Activity context, int id) {
			super();
			this.context = context;
			this.id = id;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				// create an HTTP request to a protected resource
				HttpGet request = new HttpGet(params[0]);

				consumer.sign(request);

				// send the request
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(request);
				return EntityUtils.toString(response.getEntity());

			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
			if (result != null) {
				onTaskSuccess(result, id);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
		}
	}

	@Override
	public void onClick(View v) {
		String id = "";
		String reblogKey = "";
		switch (v.getId()) {
		case R.id.ivLike:
			id = dashboardAdapter.getItem(lvDashboard.getPositionForView(v)).id;
			lastId = dashboardAdapter
					.getItem(lvDashboard.getPositionForView(v)).id;
			reblogKey = dashboardAdapter.getItem(lvDashboard
					.getPositionForView(v)).reblog_key;
			Log.v("POST",
					id
							+ " "
							+ reblogKey
							+ " "
							+ dashboardAdapter.getItem(lvDashboard
									.getPositionForView(v)).blog_name);
			new TumblrPostLoader(getActivity(), POST_LIKE).execute(
					UrlComposer.composeUrlUserLike(), id, reblogKey);
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case R.id.ivReblog:
			id = dashboardAdapter.getItem(lvDashboard.getPositionForView(v)).id;
			lastId = dashboardAdapter
					.getItem(lvDashboard.getPositionForView(v)).id;
			reblogKey = dashboardAdapter.getItem(lvDashboard
					.getPositionForView(v)).reblog_key;
			Log.v("POST",
					id
							+ " "
							+ reblogKey
							+ " "
							+ dashboardAdapter.getItem(lvDashboard
									.getPositionForView(v)).blog_name);
			new TumblrPostLoader(getActivity(), POST_REBLOG)
					.execute(
							UrlComposer
									.composeUrlBlogPostReblog(Constants.userInfo.response.user.base_hostname),
							id, reblogKey);
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case R.id.tvTitle:
			break;
		default:
			break;
		}
	}

	public class TumblrPostLoader extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("unused")
		private Activity context;
		private int id;

		public TumblrPostLoader(Activity context, int id) {
			super();
			this.context = context;
			this.id = id;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				// create an HTTP request to a protected resource
				HttpPost request = new HttpPost(params[0]);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("id", params[1]));
				nameValuePairs.add(new BasicNameValuePair("reblog_key",
						params[2]));
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				consumer.sign(request);

				// send the request
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(request);
				return EntityUtils.toString(response.getEntity());

			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
			if (result != null) {
				onTaskSuccess(result, id);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
		lvDashboard.setVisibility(View.GONE);
		// Request User Dashboard
		new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
				.execute(UrlComposer.composeUrlUserDashboard(type,
						(Boolean) true, (Boolean) true));
		getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
		lvDashboard.setVisibility(View.GONE);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// leave this empty
	}

	@Override
	public void onScrollStateChanged(AbsListView listView, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (listView.getLastVisiblePosition() >= listView.getCount() - 1
					- threshold) {
				String lastOffset = dashboardAdapter.getCount() + "";
				if (!isLoading) {
					isLoading = true;
					Log.v("Load More", ((Boolean) isLoading).toString());
					loadElements(lastOffset);
				}
			}
		}
	}

	private void loadElements(String lastOffset) {
		// Request User Dashboard
		new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD_LOAD_MORE)
				.execute(UrlComposer.composeUrlUserDashboard(type,
						(Boolean) true, (Boolean) true, lastOffset));
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		switch (position) {
		case 0:
			type = null;
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 1:
			type = "text";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 2:
			type = "quote";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 3:
			type = "link";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 4:
			type = "answer";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 5:
			type = "chat";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 6:
			type = "audio";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 7:
			type = "video";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		case 8:
			type = "photo";
			new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
					.execute(UrlComposer.composeUrlUserDashboard(type,
							(Boolean) true, (Boolean) true));
			getActivity().findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
			lvDashboard.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		return false;
	}

}

package com.antares.slidedrawer.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.antares.slidedrawer.Constants;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.adapter.DashboardAdapter;
import com.antares.slidedrawer.data.DashboardPostsVO;
import com.antares.slidedrawer.utils.UrlComposer;
import com.google.gson.Gson;

public class HomeFragment extends Fragment implements OnClickListener {

	private static final int REQUEST_DASHBOARD = 0;
	private static final int POST_LIKE = 1;
	private static final int POST_REBLOG = 2;
	private static OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			Constants.TUMBLR_CONSUMER_KEY, Constants.TUMBLR_CONSUMER_SECRET);
	private SparseArray<DashboardPostsVO> dashboardPosts;
	private ListView lvDashboard;
	private DashboardAdapter dashboardAdapter;

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home, container, false);

		dashboardPosts = new SparseArray<DashboardPostsVO>();
		lvDashboard = (ListView) rootView.findViewById(R.id.lvDashboard);
		SharedPreferences oAuth = getActivity().getSharedPreferences(
				Constants.PREF_NAME, 0);
		consumer.setTokenWithSecret(
				oAuth.getString(Constants.PREF_PARAM_TOKEN, null),
				oAuth.getString(Constants.PREF_PARAM_TOKEN_SECRET, null));
		// Request User Dashboard
		new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
				.execute(UrlComposer.composeUrlUserDashboard("text"));
		return rootView;
	}

	public void onTaskSuccess(String result, int id) {
		switch (id) {
		case REQUEST_DASHBOARD:
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
					dashboardPosts.put(i, dashboardPost);
				}
				dashboardAdapter = new DashboardAdapter(dashboardPosts,
						getActivity(), this);
				lvDashboard.setAdapter(dashboardAdapter);
				getActivity().findViewById(R.id.pbHome)
						.setVisibility(View.GONE);
				lvDashboard.setVisibility(View.VISIBLE);
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
							.execute(UrlComposer
									.composeUrlUserDashboard("text"));
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
							.execute(UrlComposer
									.composeUrlUserDashboard("text"));
				}
			} catch (JSONException e) {
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
		case R.id.tvLike:
			id = dashboardAdapter.getItem(lvDashboard.getPositionForView(v)).id;
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
			break;
		case R.id.tvReblog:
			id = dashboardAdapter.getItem(lvDashboard.getPositionForView(v)).id;
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
}

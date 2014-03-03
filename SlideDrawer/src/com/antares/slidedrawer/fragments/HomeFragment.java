package com.antares.slidedrawer.fragments;

import java.io.IOException;
import java.net.MalformedURLException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.antares.slidedrawer.Constants;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.adapter.DashboardAdapter;
import com.antares.slidedrawer.data.DashboardPostsVO;
import com.antares.slidedrawer.utils.UrlComposer;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {

	private static final int REQUEST_DASHBOARD = 0;
	private static OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			Constants.TUMBLR_CONSUMER_KEY, Constants.TUMBLR_CONSUMER_SECRET);
	private SparseArray<DashboardPostsVO> dashboardPosts;
	private ListView lvDashboard;
	private DashboardAdapter dashboardAdapter;
	private static LruCache<String, Bitmap> mMemoryCache;

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home, container, false);

		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize);

		dashboardPosts = new SparseArray<DashboardPostsVO>();
		lvDashboard = (ListView) rootView.findViewById(R.id.lvDashboard);
		SharedPreferences oAuth = getActivity().getSharedPreferences(
				Constants.PREF_NAME, 0);
		consumer.setTokenWithSecret(
				oAuth.getString(Constants.PREF_PARAM_TOKEN, null),
				oAuth.getString(Constants.PREF_PARAM_TOKEN_SECRET, null));
		// Request User Info
		new TumblrRequestLoader(getActivity(), REQUEST_DASHBOARD)
				.execute(UrlComposer.composeUrlUserDashboard("text"));
		return rootView;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
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
				RequestQueue rq = Volley.newRequestQueue(getActivity());
				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = posts.getJSONObject(i);
					Gson gson = new Gson();
					DashboardPostsVO dashboardPost = gson.fromJson(
							post.toString(), DashboardPostsVO.class);
					final String blogName = dashboardPost.blog_name;
					dashboardPosts.put(dashboardPost.id, dashboardPost);
					ImageRequest ir = new ImageRequest(
							UrlComposer
									.composeUrlBlogAvatar(
											dashboardPost.blog_name
													+ ".tumblr.com", 64),
							new Response.Listener<Bitmap>() {

								@Override
								public void onResponse(Bitmap response) {
									addBitmapToMemoryCache(blogName, response);
									dashboardAdapter.notifyDataSetChanged();
								}
							}, 0, 0, null, null);
					rq.add(ir);
				}
				rq.start();
				dashboardAdapter = new DashboardAdapter(dashboardPosts,
						getActivity(), mMemoryCache);
				lvDashboard.setAdapter(dashboardAdapter);
				getActivity().findViewById(R.id.pbHome)
						.setVisibility(View.GONE);
				lvDashboard.setVisibility(View.VISIBLE);
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
}

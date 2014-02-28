package com.antares.slidedrawer.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antares.slidedrawer.Constants;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.UserInfoVO;
import com.antares.slidedrawer.utils.StringUtils;
import com.antares.slidedrawer.utils.UrlComposer;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {
	private static final int REQUEST_TOKEN = 0;
	private static final int REQUEST_ACCESS_TOKEN = 1;
	private static final int REQUEST_USER_INFO = 2;
	private static final int REQUEST_BLOG_AVATAR = 3;
	private static final String TAG = "HomeFragment";
	private static OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			Constants.TUMBLR_CONSUMER_KEY, Constants.TUMBLR_CONSUMER_SECRET);
	private static OAuthProvider provider = new CommonsHttpOAuthProvider(
			Constants.REQUEST_TOKEN_URL, Constants.ACCESS_TOKEN_URL,
			Constants.AUTH_URL);

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home, container, false);
		String downloadPath = getActivity().getCacheDir().getAbsolutePath();
		File file = new File(downloadPath, "Avatar.PNG");
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			((ImageView) rootView.findViewById(R.id.ivAvatar))
					.setImageBitmap(bitmap);
		}
		SharedPreferences oAuth = getActivity().getSharedPreferences(
				Constants.PREF_NAME, 0);
		// To get the oauth token after the user has granted
		// permissions
		Uri uri = getActivity().getIntent().getData();
		if (uri != null) {
			// On Request Authorization Success
			String token = uri.getQueryParameter("oauth_token");
			String verifier = uri.getQueryParameter("oauth_verifier");
			Log.v(TAG, "Request Token:" + token);
			Log.v(TAG, "Verifier:" + verifier);

			new TumblrRequestAccessTokenLoader(getActivity()).execute(verifier);
		} else {
			if (oAuth.getString(Constants.PREF_PARAM_TOKEN, null) == null)
				new TumblrRequestTokenLoader(getActivity()).execute();
			else {
				consumer.setTokenWithSecret(oAuth.getString(
						Constants.PREF_PARAM_TOKEN, null), oAuth.getString(
						Constants.PREF_PARAM_TOKEN_SECRET, null));
				// Request User Info
				new TumblrRequestLoader(getActivity(), REQUEST_USER_INFO)
						.execute(UrlComposer.composeUrlUserInfo());
			}
		}
		return rootView;
	}

	public void onRequestTokenSuccess(String result, int id) {
		switch (id) {
		case REQUEST_TOKEN:
			// Request Authorization
			startActivity(new Intent("android.intent.action.VIEW",
					Uri.parse(result)));
			break;
		case REQUEST_ACCESS_TOKEN:
			// Request User Info
			new TumblrRequestLoader(getActivity(), REQUEST_USER_INFO)
					.execute(UrlComposer.composeUrlUserInfo());
			break;
		default:
			break;
		}
	}

	public void onTaskSuccess(String result, int id) {
		switch (id) {
		case REQUEST_USER_INFO:
			Log.v("JSON", result);
			try {
				JSONObject json = new JSONObject(result);
				JSONObject meta = json.getJSONObject("meta");
				JSONObject response = json.getJSONObject("response");
				JSONObject user = response.getJSONObject("user");
				Gson gson = new Gson();
				Constants.userInfo = gson.fromJson(user.toString(),
						UserInfoVO.class);
				Toast.makeText(getActivity(), meta.getString("msg"),
						Toast.LENGTH_LONG).show();
				Constants.userInfo.primary = 0;
				for (int i = 0; i < Constants.userInfo.blogs.length; i++) {
					if (Constants.userInfo.blogs[i].primary)
						Constants.userInfo.primary = i;
				}
				((TextView) getActivity().findViewById(R.id.tvBlogName))
						.setText(Constants.userInfo.blogs[Constants.userInfo.primary].title);
				((TextView) getActivity().findViewById(R.id.tvBlogDescription))
						.setText("Followers: "
								+ Constants.userInfo.blogs[Constants.userInfo.primary].followers);
				Constants.userInfo.base_hostname = StringUtils
						.getBaseHostname(Constants.userInfo.blogs[Constants.userInfo.primary].url);

				// Request Blog Avatar
				new TumblrRequestLoader(getActivity(), REQUEST_BLOG_AVATAR)
						.execute(UrlComposer.composeUrlBlogAvatarWithSize(
								Constants.userInfo.base_hostname, 128));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	public class TumblrRequestTokenLoader extends
			AsyncTask<String, Integer, String> {
		private Activity context;

		public TumblrRequestTokenLoader(Activity context) {
			super();
			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String authUrl = provider.retrieveRequestToken(consumer,
						Constants.CALLBACK_URL);
				Log.v(TAG, "Token:" + consumer.getToken());
				Log.v(TAG, "Token Secret:" + consumer.getTokenSecret());
				return authUrl;
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
			if (result != null)
				onRequestTokenSuccess(result, REQUEST_TOKEN);
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

	public class TumblrRequestAccessTokenLoader extends
			AsyncTask<String, Integer, String> {
		private Activity context;

		public TumblrRequestAccessTokenLoader(Activity context) {
			super();
			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				provider.retrieveAccessToken(consumer, params[0]);
				SharedPreferences oAuth = getActivity().getSharedPreferences(
						Constants.PREF_NAME, 0);
				Editor editor = oAuth.edit();
				editor.putString(Constants.PREF_PARAM_TOKEN,
						consumer.getToken());
				editor.putString(Constants.PREF_PARAM_TOKEN_SECRET,
						consumer.getTokenSecret());
				editor.commit();
				Log.v(TAG, "Token:" + consumer.getToken());
				Log.v(TAG, "Token Secret:" + consumer.getTokenSecret());
				return "Success";

			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
			if (result != null)
				onRequestTokenSuccess(result, REQUEST_ACCESS_TOKEN);
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

	public class TumblrRequestLoader extends AsyncTask<String, Integer, String> {
		private Activity context;
		private int id;
		private Bitmap bitmap;

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
				if (id == REQUEST_USER_INFO)
					return EntityUtils.toString(response.getEntity());
				else if (id == REQUEST_BLOG_AVATAR) {
					final int statusCode = response.getStatusLine()
							.getStatusCode();
					if (statusCode != HttpStatus.SC_OK) {
						Log.v(TAG, "Error " + statusCode
								+ " while retrieving bitmap from " + params[0]);
						return null;
					}

					final HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream inputStream = null;
						try {
							inputStream = entity.getContent();
							bitmap = BitmapFactory.decodeStream(inputStream);
							return "Success";
						} finally {
							if (inputStream != null) {
								inputStream.close();
							}
							entity.consumeContent();
						}
					}
				}

			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
			if (result != null) {
				if (id == REQUEST_BLOG_AVATAR) {

					String downloadPath = context.getCacheDir()
							.getAbsolutePath();
					File file = new File(downloadPath, "Avatar.PNG");
					try {
						FileOutputStream outStream = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100,
								outStream);
						outStream.flush();
						outStream.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					((ImageView) context.findViewById(R.id.ivAvatar))
							.setImageBitmap(bitmap);
				} else
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

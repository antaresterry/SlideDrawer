package com.antares.slidedrawer;

import java.io.IOException;
import java.net.MalformedURLException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpResponse;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.antares.slidedrawer.data.UserInfoVO;
import com.antares.slidedrawer.utils.StringUtils;
import com.antares.slidedrawer.utils.UrlComposer;
import com.google.gson.Gson;

public class SplashActivity extends Activity {
	private static final int REQUEST_TOKEN = 0;
	private static final int REQUEST_ACCESS_TOKEN = 1;
	private static final int REQUEST_USER_INFO = 2;
	private static final String TAG = "HomeFragment";
	private static OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			Constants.TUMBLR_CONSUMER_KEY, Constants.TUMBLR_CONSUMER_SECRET);
	private static OAuthProvider provider = new CommonsHttpOAuthProvider(
			Constants.REQUEST_TOKEN_URL, Constants.ACCESS_TOKEN_URL,
			Constants.AUTH_URL);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		SharedPreferences oAuth = this.getSharedPreferences(
				Constants.PREF_NAME, 0);
		// To get the oauth token after the user has granted
		// permissions
		Uri uri = this.getIntent().getData();
		if (uri != null) {
			// On Request Authorization Success
			String token = uri.getQueryParameter("oauth_token");
			String verifier = uri.getQueryParameter("oauth_verifier");
			Log.v(TAG, "Request Token:" + token);
			Log.v(TAG, "Verifier:" + verifier);

			new TumblrRequestAccessTokenLoader(this).execute(verifier);
		} else {
			if (oAuth.getString(Constants.PREF_PARAM_TOKEN, null) == null) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						findViewById(R.id.panelLoading)
								.setVisibility(View.GONE);
						findViewById(R.id.panelTumblr).setVisibility(
								View.VISIBLE);
					}
				}, 2000);

			} else {
				consumer.setTokenWithSecret(oAuth.getString(
						Constants.PREF_PARAM_TOKEN, null), oAuth.getString(
						Constants.PREF_PARAM_TOKEN_SECRET, null));
				// Request User Info
				new TumblrRequestLoader(this, REQUEST_USER_INFO)
						.execute(UrlComposer.composeUrlUserInfo());
			}
		}
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
		super.onBackPressed();
	}

	public void onClick(View vi) {
		int viewId = vi.getId();
		switch (viewId) {
		case R.id.connectButton:
			new TumblrRequestTokenLoader(this).execute();
			break;
		default:
			break;
		}
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
			new TumblrRequestLoader(this, REQUEST_USER_INFO)
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
				Gson gson = new Gson();
				JSONObject json = new JSONObject(result);
				Constants.userInfo = gson.fromJson(json.toString(),
						UserInfoVO.class);
				Toast.makeText(this, Constants.userInfo.meta.msg,
						Toast.LENGTH_LONG).show();
				Constants.userInfo.response.user.primary = 0;
				for (int i = 0; i < Constants.userInfo.response.user.blogs.length; i++) {
					if (Constants.userInfo.response.user.blogs[i].primary)
						Constants.userInfo.response.user.primary = i;
				}
				Constants.userInfo.response.user.base_hostname = StringUtils
						.getBaseHostname(Constants.userInfo.response.user.blogs[Constants.userInfo.response.user.primary].url);

				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				finish();
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
		@SuppressWarnings("unused")
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
		@SuppressWarnings("unused")
		private Activity context;

		public TumblrRequestAccessTokenLoader(Activity context) {
			super();
			this.context = context;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				provider.retrieveAccessToken(consumer, params[0]);
				SharedPreferences oAuth = SplashActivity.this
						.getSharedPreferences(Constants.PREF_NAME, 0);
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
			if (result != null)
				onTaskSuccess(result, id);
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

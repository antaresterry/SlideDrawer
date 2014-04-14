package com.antares.slidedrawer.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.antares.slidedrawer.Constants;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.DashboardPostsVO;
import com.antares.slidedrawer.utils.AnimatedImageView;
import com.antares.slidedrawer.utils.MemoryCacheSingleton;
import com.antares.slidedrawer.utils.UrlComposer;
import com.antares.slidedrawer.utils.VolleySingleton;

public class DashboardAdapter extends BaseAdapter {
	private List<DashboardPostsVO> dashboardPosts;
	private Activity context;
	private Fragment fragment;

	public DashboardAdapter(List<DashboardPostsVO> dashboardPosts,
			Activity context, Fragment fragment) {
		super();
		this.dashboardPosts = dashboardPosts;
		this.context = context;
		this.fragment = fragment;
	}

	@Override
	public int getCount() {
		return dashboardPosts.size();
	}

	@Override
	public DashboardPostsVO getItem(int position) {
		return dashboardPosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getItemPosition(String id) {
		for (int i = 0; i < getCount(); i++) {
			if (getItem(i).id.equals(id))
				return i;
		}
		return -1;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = context.getLayoutInflater().inflate(
					R.layout.dashboard_list_item, null);

		TextView tvBlogName = (TextView) convertView
				.findViewById(R.id.tvBlogName);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		ImageView ivLike = (ImageView) convertView.findViewById(R.id.ivLike);
		ImageView ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
		ImageView ivReblog = (ImageView) convertView
				.findViewById(R.id.ivReblog);
		TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
		TextView tvNotes = (TextView) convertView.findViewById(R.id.tvNotes);
		TextView tvTags = (TextView) convertView.findViewById(R.id.tvTags);
		TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		LinearLayout llPhotos = (LinearLayout) convertView
				.findViewById(R.id.llPhotos);
		llPhotos.setVisibility(View.GONE);
		// TextView tvBodyImage = (TextView)
		// convertView.findViewById(R.id.tvBodyImage);
		tvBlogName.setText(getItem(position).blog_name);
		if (getItem(position).reblogged_from_name != null)
			tvBlogName.setText(tvBlogName.getText() + "\nreblogged from "
					+ getItem(position).reblogged_from_name);
		if (getItem(position).type.equals("text")) {
			if (getItem(position).title == null)
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(getItem(position).title);
			String body = getItem(position).body;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html
					.fromHtml(body, new ImageGet(tvBody, body), null));
		} else if (getItem(position).type.equals("quote")) {
			tvTitle.setText(Html.fromHtml("\"" + getItem(position).text + "\""));
			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml(getItem(position).source));
		} else if (getItem(position).type.equals("link")) {
			if (getItem(position).title == null)
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(getItem(position).title);
			String description = getItem(position).description;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml("<a href=" + getItem(position).url
					+ ">" + getItem(position).url + "</a>" + "\n"
					+ getItem(position).description, new ImageGet(tvBody,
					description), null));
		} else if (getItem(position).type.equals("chat")) {
			if (getItem(position).title == null)
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(getItem(position).title);
			tvBody.setText(Html.fromHtml(getItem(position).body));
		} else if (getItem(position).type.equals("photo")) {
			tvTitle.setText("[Photo]");
			llPhotos.setVisibility(View.VISIBLE);
			llPhotos.removeAllViewsInLayout();
			for (int i = 0; i < getItem(position).photos.length; i++) {
				if (!getItem(position).photos[i].original_size.url
						.contains(".gif")) {
					NetworkImageView ivPhoto = new NetworkImageView(context);
					ivPhoto.setImageUrl(
							getItem(position).photos[i].original_size.url,
							VolleySingleton.getInstance(context)
									.getImageLoader());
					llPhotos.addView(ivPhoto);
				} else {
					new TumblrImageLoader(context, llPhotos)
							.execute(getItem(position).photos[i].original_size.url);
				}
			}
			if (getItem(position).caption == null)
				tvBody.setVisibility(View.GONE);
			else
				tvBody.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.VISIBLE);
			String caption = getItem(position).caption;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml(caption,
					new ImageGet(tvBody, caption), null));
		} else if (getItem(position).type.equals("audio")) {
			tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
			tvTitle.setText(Html.fromHtml("<a href="
					+ getItem(position).short_url + ">[Audio]</a>"));
			if (getItem(position).caption == null)
				tvBody.setVisibility(View.GONE);
			else
				tvBody.setVisibility(View.VISIBLE);
			String caption = getItem(position).caption;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml(caption,
					new ImageGet(tvBody, caption), null));
		} else if (getItem(position).type.equals("video")) {
			tvTitle.setText("[Video]");
			tvTitle.setVisibility(View.VISIBLE);
			if (getItem(position).caption == null)
				tvBody.setVisibility(View.GONE);
			else
				tvBody.setVisibility(View.VISIBLE);
			String caption = getItem(position).caption;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml(caption,
					new ImageGet(tvBody, caption), null));
		} else if (getItem(position).type.equals("answer")) {
			tvTitle.setText(Html.fromHtml("<a href="
					+ getItem(position).asking_url + ">"
					+ getItem(position).asking_name + "</a> asked:\n"
					+ getItem(position).question));
			tvTitle.setVisibility(View.VISIBLE);
			String answer = getItem(position).answer;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml(answer, new ImageGet(tvBody, answer),
					null));
		}
		tvTags.setText("");
		if (getItem(position).tags.length > 0) {
			for (int i = 0; i < getItem(position).tags.length; i++)
				tvTags.setText(tvTags.getText() + "#"
						+ getItem(position).tags[i] + " ");
			tvTags.setVisibility(View.VISIBLE);
		} else
			tvTags.setVisibility(View.GONE);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss ZZZ");
		dateFormat.setTimeZone(TimeZone.getDefault());
		Date convertedDate = new Date();
		Calendar cal = Calendar.getInstance();
		try {
			convertedDate = dateFormat.parse(getItem(position).date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(convertedDate);
		tvDate.setText(String.format("%02d:%02d",
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
		NetworkImageView ivAvatar = (NetworkImageView) convertView
				.findViewById(R.id.ivAvatar);
		ivAvatar.setImageUrl(
				UrlComposer.composeUrlBlogAvatar(getItem(position).blog_name
						+ ".tumblr.com", 64),
				VolleySingleton.getInstance(context).getImageLoader());
		ivLike.setOnClickListener((OnClickListener) fragment);
		ivReblog.setOnClickListener((OnClickListener) fragment);
		ivEdit.setOnClickListener((OnClickListener) fragment);
		tvNotes.setText(getItem(position).note_count + " Notes");
		if (getItem(position).blog_name
				.equals(Constants.userInfo.response.user.blogs[Constants.userInfo.response.user.primary].name)) {
			ivLike.setVisibility(View.GONE);
			ivEdit.setVisibility(View.VISIBLE);
		} else {
			ivLike.setVisibility(View.VISIBLE);
			ivEdit.setVisibility(View.GONE);
		}
		if (getItem(position).liked) {
			ivLike.setImageResource(R.drawable.dashboard_post_control_like_selected);
			ivLike.setClickable(false);
		} else {
			ivLike.setImageResource(R.drawable.dashboard_post_control_like);
			ivLike.setClickable(true);
		}

		return convertView;
	}

	public Drawable getBitmapFromCache(String url) {
		if (getBitmapFromMemCache(url) != null) {
			Drawable d = new BitmapDrawable(context.getResources(),
					getBitmapFromMemCache(url));
			d.setBounds(0, 0, getBitmapFromMemCache(url).getWidth(),
					getBitmapFromMemCache(url).getHeight());
			return d;
		} else
			return null;

	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			MemoryCacheSingleton.getInstance().put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return MemoryCacheSingleton.getInstance().get(key);
	}

	public class BitmapListener implements Response.Listener<Bitmap> {
		private String source;
		private TextView tvBody;
		private String body;

		public BitmapListener(String source, TextView tvBody, String body) {
			super();
			this.source = source;
			this.tvBody = tvBody;
			this.body = body;
		}

		@Override
		public void onResponse(Bitmap response) {
			addBitmapToMemoryCache(source, response);
			tvBody.setText(Html.fromHtml(body, new ImageGetter() {

				@Override
				public Drawable getDrawable(String source) {
					return getBitmapFromCache(source);
				}
			}, null));

		}

	}

	public class ImageGet implements ImageGetter {
		private TextView tvBody;
		private String body;

		public ImageGet(TextView tvBody, String body) {
			super();
			this.tvBody = tvBody;
			this.body = body;
		}

		@Override
		public Drawable getDrawable(String source) {
			ImageRequest ir = new ImageRequest(source, new BitmapListener(
					source, tvBody, body), 0, 0, null, null);
			if (getBitmapFromMemCache(source) == null) {
				VolleySingleton.getInstance(context).getRequestQueue().add(ir);
				VolleySingleton.getInstance(context).getRequestQueue().start();
			}
			return getBitmapFromCache(source);
		}

	}

	public class TumblrImageLoader extends AsyncTask<String, Integer, Movie> {

		private Activity context;
		private LinearLayout llPhotos;

		public TumblrImageLoader(Activity context, LinearLayout llPhotos) {
			super();
			this.context = context;
			this.llPhotos = llPhotos;
		}

		@Override
		protected Movie doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				URLConnection conn = url.openConnection();
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				bis.mark(conn.getContentLength());
				Movie movie = Movie.decodeStream(bis);
				bis.close();
				return movie;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Movie result) {
			//
			if (result != null) {
				Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
				final AnimatedImageView view = new AnimatedImageView(context,
						result, result.width(), result.height());
				llPhotos.addView(view);
				final Handler handler = new Handler();
				new Thread() {
					@Override
					public void run() {
						// ... setup the movie (using the code from above)
						// ... create and display the custom view, passing the
						// movie

						while (!Thread.currentThread().isInterrupted()) {
							handler.post(new Runnable() {
								public void run() {
									view.invalidate();
								}
							});
							try {
								Thread.sleep(50); // yields 20 fps
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
					}
				}.start();
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

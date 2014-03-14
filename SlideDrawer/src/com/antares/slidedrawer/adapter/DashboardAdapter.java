package com.antares.slidedrawer.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.DashboardPostsVO;
import com.antares.slidedrawer.utils.MemoryCacheSingleton;
import com.antares.slidedrawer.utils.UrlComposer;
import com.antares.slidedrawer.utils.VolleySingleton;

public class DashboardAdapter extends BaseAdapter {
	private SparseArray<DashboardPostsVO> dashboardPosts;
	private Activity context;
	private Fragment fragment;

	public DashboardAdapter(SparseArray<DashboardPostsVO> dashboardPosts,
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
		return dashboardPosts.get(dashboardPosts.keyAt(position));
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = context.getLayoutInflater().inflate(
					R.layout.dashboard_list_item, null);
		TextView tvBlogName = (TextView) convertView
				.findViewById(R.id.tvBlogName);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView tvLike = (TextView) convertView.findViewById(R.id.tvLike);
		TextView tvReblog = (TextView) convertView.findViewById(R.id.tvReblog);
		TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
		TextView tvNotes = (TextView) convertView.findViewById(R.id.tvNotes);
		TextView tvTags = (TextView) convertView.findViewById(R.id.tvTags);
		final TextView tvBody = (TextView) convertView
				.findViewById(R.id.tvBody);
		// TextView tvBodyImage = (TextView)
		// convertView.findViewById(R.id.tvBodyImage);
		tvBlogName.setText(getItem(position).blog_name);
		if (getItem(position).type.equals("text")) {
			if (getItem(position).title == null)
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(getItem(position).title);
			final String body = getItem(position).body;
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml(body, new ImageGetter() {

				@Override
				public Drawable getDrawable(final String source) {
					ImageRequest ir = new ImageRequest(source,
							new Response.Listener<Bitmap>() {

								@Override
								public void onResponse(Bitmap response) {
									addBitmapToMemoryCache(source, response);
									tvBody.setText(Html.fromHtml(
											getItem(position).body,
											new ImageGetter() {

												@Override
												public Drawable getDrawable(
														String source) {
													return getBitmapFromCache(source);
												}
											}, null));

								}
							}, 0, 0, null, null);
					if (getBitmapFromMemCache(source) == null) {
						VolleySingleton.getInstance(context).getRequestQueue()
								.add(ir);
						VolleySingleton.getInstance(context).getRequestQueue()
								.start();
					}
					return getBitmapFromCache(source);
				}

			}, null));
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
			tvBody.setMovementMethod(LinkMovementMethod.getInstance());
			tvBody.setText(Html.fromHtml("<a href=" + getItem(position).url
					+ ">" + getItem(position).url + "</a>" + "\n"
					+ getItem(position).description, new ImageGetter() {

				@Override
				public Drawable getDrawable(final String source) {
					ImageRequest ir = new ImageRequest(source,
							new Response.Listener<Bitmap>() {

								@Override
								public void onResponse(Bitmap response) {
									addBitmapToMemoryCache(source, response);
									tvBody.setText(Html.fromHtml(
											getItem(position).description,
											new ImageGetter() {

												@Override
												public Drawable getDrawable(
														String source) {
													return getBitmapFromCache(source);
												}
											}, null));

								}
							}, 0, 0, null, null);
					if (getBitmapFromMemCache(source) == null) {
						VolleySingleton.getInstance(context).getRequestQueue()
								.add(ir);
						VolleySingleton.getInstance(context).getRequestQueue()
								.start();
					}
					return getBitmapFromCache(source);
				}

			}, null));
		} else if (getItem(position).type.equals("chat")) {
			if (getItem(position).title == null)
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(getItem(position).title);
			tvBody.setText(Html.fromHtml(getItem(position).body));
		} else if (getItem(position).type.equals("photo")) {
			tvTitle.setText("[Photo]");
			if (getItem(position).caption == null)
				tvBody.setVisibility(View.GONE);
			else
				tvBody.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.VISIBLE);
			tvBody.setText(Html.fromHtml(getItem(position).caption,
					new ImageGetter() {

						@Override
						public Drawable getDrawable(final String source) {
							ImageRequest ir = new ImageRequest(source,
									new Response.Listener<Bitmap>() {

										@Override
										public void onResponse(Bitmap response) {
											addBitmapToMemoryCache(source,
													response);
											tvBody.setText(Html.fromHtml(
													getItem(position).caption,
													new ImageGetter() {

														@Override
														public Drawable getDrawable(
																String source) {
															return getBitmapFromCache(source);
														}
													}, null));

										}
									}, 0, 0, null, null);
							if (getBitmapFromMemCache(source) == null) {
								VolleySingleton.getInstance(context)
										.getRequestQueue().add(ir);
								VolleySingleton.getInstance(context)
										.getRequestQueue().start();
							}
							return getBitmapFromCache(source);
						}

					}, null));
		} else if (getItem(position).type.equals("audio")) {
			tvTitle.setText("[Audio]");
			tvTitle.setVisibility(View.VISIBLE);
			if (getItem(position).caption == null)
				tvBody.setVisibility(View.GONE);
			else
				tvBody.setVisibility(View.VISIBLE);
			tvBody.setText(Html.fromHtml(getItem(position).caption,
					new ImageGetter() {

						@Override
						public Drawable getDrawable(final String source) {
							ImageRequest ir = new ImageRequest(source,
									new Response.Listener<Bitmap>() {

										@Override
										public void onResponse(Bitmap response) {
											addBitmapToMemoryCache(source,
													response);
											tvBody.setText(Html.fromHtml(
													getItem(position).caption,
													new ImageGetter() {

														@Override
														public Drawable getDrawable(
																String source) {
															return getBitmapFromCache(source);
														}
													}, null));

										}
									}, 0, 0, null, null);
							if (getBitmapFromMemCache(source) == null) {
								VolleySingleton.getInstance(context)
										.getRequestQueue().add(ir);
								VolleySingleton.getInstance(context)
										.getRequestQueue().start();
							}
							return getBitmapFromCache(source);
						}

					}, null));
		} else if (getItem(position).type.equals("video")) {
			tvTitle.setText("[Video]");
			tvTitle.setVisibility(View.VISIBLE);
			if (getItem(position).caption == null)
				tvBody.setVisibility(View.GONE);
			else
				tvBody.setVisibility(View.VISIBLE);
			tvBody.setText(Html.fromHtml(getItem(position).caption,
					new ImageGetter() {

						@Override
						public Drawable getDrawable(final String source) {
							ImageRequest ir = new ImageRequest(source,
									new Response.Listener<Bitmap>() {

										@Override
										public void onResponse(Bitmap response) {
											addBitmapToMemoryCache(source,
													response);
											tvBody.setText(Html.fromHtml(
													getItem(position).caption,
													new ImageGetter() {

														@Override
														public Drawable getDrawable(
																String source) {
															return getBitmapFromCache(source);
														}
													}, null));

										}
									}, 0, 0, null, null);
							if (getBitmapFromMemCache(source) == null) {
								VolleySingleton.getInstance(context)
										.getRequestQueue().add(ir);
								VolleySingleton.getInstance(context)
										.getRequestQueue().start();
							}
							return getBitmapFromCache(source);
						}

					}, null));
		} else if (getItem(position).type.equals("answer")) {
			tvTitle.setText(Html.fromHtml("<a href="
					+ getItem(position).asking_url + ">"
					+ getItem(position).asking_name + "</a> asked:\n"
					+ getItem(position).question));
			tvTitle.setVisibility(View.VISIBLE);
			tvBody.setText(Html.fromHtml(getItem(position).answer,
					new ImageGetter() {

						@Override
						public Drawable getDrawable(final String source) {
							ImageRequest ir = new ImageRequest(source,
									new Response.Listener<Bitmap>() {

										@Override
										public void onResponse(Bitmap response) {
											addBitmapToMemoryCache(source,
													response);
											tvBody.setText(Html.fromHtml(
													getItem(position).answer,
													new ImageGetter() {

														@Override
														public Drawable getDrawable(
																String source) {
															return getBitmapFromCache(source);
														}
													}, null));

										}
									}, 0, 0, null, null);
							if (getBitmapFromMemCache(source) == null) {
								VolleySingleton.getInstance(context)
										.getRequestQueue().add(ir);
								VolleySingleton.getInstance(context)
										.getRequestQueue().start();
							}
							return getBitmapFromCache(source);
						}

					}, null));
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
		tvLike.setOnClickListener((OnClickListener) fragment);
		tvReblog.setOnClickListener((OnClickListener) fragment);
		tvNotes.setText(getItem(position).note_count + " Notes");
		if (getItem(position).liked) {
			tvLike.setText("Liked");
			tvLike.setClickable(false);
		} else {
			tvLike.setText("Like");
			tvLike.setClickable(true);
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
}

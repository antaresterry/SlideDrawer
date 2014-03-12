package com.antares.slidedrawer.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.SparseArray;
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
		final TextView tvBody = (TextView) convertView
				.findViewById(R.id.tvBody);
		// TextView tvBodyImage = (TextView)
		// convertView.findViewById(R.id.tvBodyImage);
		if (getItem(position).type.equals("text")) {
			tvBlogName.setText(getItem(position).blog_name);
			tvTitle.setText(getItem(position).title);
			final String body = getItem(position).body;
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
		}
		if (getItem(position).title == null)
			tvTitle.setVisibility(View.GONE);
		else
			tvTitle.setVisibility(View.VISIBLE);
		tvDate.setText(getItem(position).date);
		NetworkImageView ivAvatar = (NetworkImageView) convertView
				.findViewById(R.id.ivAvatar);
		ivAvatar.setImageUrl(
				UrlComposer.composeUrlBlogAvatar(getItem(position).blog_name
						+ ".tumblr.com", 64),
				VolleySingleton.getInstance(context).getImageLoader());
		tvLike.setOnClickListener((OnClickListener) fragment);
		tvReblog.setOnClickListener((OnClickListener) fragment);
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

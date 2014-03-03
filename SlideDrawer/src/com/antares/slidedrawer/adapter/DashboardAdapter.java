package com.antares.slidedrawer.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.util.LruCache;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.DashboardPostsVO;

public class DashboardAdapter extends BaseAdapter {
	private SparseArray<DashboardPostsVO> dashboardPosts;
	private Activity context;
	private LruCache<String, Bitmap> mMemoryCache;

	public DashboardAdapter(SparseArray<DashboardPostsVO> dashboardPosts,
			Activity context, LruCache<String, Bitmap> mMemoryCache) {
		super();
		this.dashboardPosts = dashboardPosts;
		this.context = context;
		this.mMemoryCache = mMemoryCache;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = context.getLayoutInflater().inflate(
					R.layout.dashboard_list_item, null);
		TextView tvBlogName = (TextView) convertView
				.findViewById(R.id.tvBlogName);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		WebView wvBody = (WebView) convertView.findViewById(R.id.wvBody);
		if (dashboardPosts.get(dashboardPosts.keyAt(position)).type
				.equals("text")) {
			tvBlogName.setText(dashboardPosts.get(dashboardPosts
					.keyAt(position)).blog_name);
			tvTitle.setText(dashboardPosts.get(dashboardPosts.keyAt(position)).title);
			wvBody.loadData(
					dashboardPosts.get(dashboardPosts.keyAt(position)).body,
					"text/html; charset=UTF-8", null);
			wvBody.setBackgroundColor(Color.TRANSPARENT);
		}
		if (dashboardPosts.get(dashboardPosts.keyAt(position)).title == null)
			tvTitle.setVisibility(View.GONE);
		else
			tvTitle.setVisibility(View.VISIBLE);
		Bitmap bitmap = getBitmapFromMemCache(dashboardPosts.get(dashboardPosts
				.keyAt(position)).blog_name);
		if (bitmap != null) {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			BitmapShader shader;
			shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(shader);

			RectF rect = new RectF(0.0f, 0.0f, bitmap.getWidth(),
					bitmap.getHeight());

			// rect contains the bounds of the shape
			// radius is the radius in pixels of the rounded corners
			// paint contains the shader that will texture the shape
			canvas.drawRoundRect(rect, 10, 10, paint);
			bitmap = output;
		}
		ImageView ivAvatar = (ImageView) convertView
				.findViewById(R.id.ivAvatar);
		ivAvatar.setImageBitmap(bitmap);
		return convertView;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

}

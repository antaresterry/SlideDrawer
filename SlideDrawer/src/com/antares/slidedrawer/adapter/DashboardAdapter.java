package com.antares.slidedrawer.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.DashboardPostsVO;
import com.antares.slidedrawer.utils.UrlComposer;
import com.antares.slidedrawer.utils.VolleySingleton;

public class DashboardAdapter extends BaseAdapter {
	private SparseArray<DashboardPostsVO> dashboardPosts;
	private Activity context;

	public DashboardAdapter(SparseArray<DashboardPostsVO> dashboardPosts,
			Activity context) {
		super();
		this.dashboardPosts = dashboardPosts;
		this.context = context;
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
		final TextView tvBody = (TextView) convertView
				.findViewById(R.id.tvBody);
		// TextView tvBodyImage = (TextView)
		// convertView.findViewById(R.id.tvBodyImage);
		if (dashboardPosts.get(dashboardPosts.keyAt(position)).type
				.equals("text")) {
			tvBlogName.setText(dashboardPosts.get(dashboardPosts
					.keyAt(position)).blog_name);
			tvTitle.setText(dashboardPosts.get(dashboardPosts.keyAt(position)).title);
			final String body = dashboardPosts.get(dashboardPosts
					.keyAt(position)).body;
			tvBody.setText(Html.fromHtml(body));
		}
		if (dashboardPosts.get(dashboardPosts.keyAt(position)).title == null)
			tvTitle.setVisibility(View.GONE);
		else
			tvTitle.setVisibility(View.VISIBLE);
		NetworkImageView ivAvatar = (NetworkImageView) convertView
				.findViewById(R.id.ivAvatar);
		ivAvatar.setImageUrl(UrlComposer.composeUrlBlogAvatar(
				dashboardPosts.get(dashboardPosts.keyAt(position)).blog_name
						+ ".tumblr.com", 64),
				VolleySingleton.getInstance(context).getImageLoader());
		return convertView;
	}
}

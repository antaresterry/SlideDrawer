package com.antares.slidedrawer.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.DrawerItem;

public class DrawerAdapter extends BaseAdapter {
	private List<DrawerItem> drawerItems;
	private Activity context;

	public DrawerAdapter(List<DrawerItem> drawerItems, Activity context) {
		super();
		this.drawerItems = drawerItems;
		this.context = context;
	}

	@Override
	public int getCount() {
		return drawerItems.size();
	}

	@Override
	public DrawerItem getItem(int position) {
		return drawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = context.getLayoutInflater().inflate(
					R.layout.drawer_list_item, null);
		TextView tvDrawer = (TextView) convertView.findViewById(R.id.tvDrawer);
		tvDrawer.setText(drawerItems.get(position).getDrawerTitle());
		Drawable icon = context.getResources().getDrawable(
				drawerItems.get(position).getDrawerIcon());
		tvDrawer.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		return convertView;
	}
}

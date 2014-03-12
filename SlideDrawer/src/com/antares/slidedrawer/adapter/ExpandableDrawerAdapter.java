package com.antares.slidedrawer.adapter;

import java.util.List;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.DrawerItem;

public class ExpandableDrawerAdapter extends BaseExpandableListAdapter {

	private List<DrawerItem> drawerItems;
	public LayoutInflater inflater;
	private Activity context;
	private Animation fadeIn;
	private Animation fadeOut;

	public ExpandableDrawerAdapter(List<DrawerItem> drawerItems,
			Activity context) {
		super();
		this.drawerItems = drawerItems;
		this.context = context;
		inflater = context.getLayoutInflater();
		fadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
		fadeOut = AnimationUtils.loadAnimation(context, R.anim.fadeout);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (drawerItems.get(groupPosition).getDrawerChildItems() != null)
			return drawerItems.get(groupPosition).getDrawerChildItems()
					.get(childPosition);
		else
			return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return drawerItems.get(groupPosition).getDrawerChildItems()
				.get(childPosition).getDrawerTitle().hashCode();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_list_child_item,
					null);
		}
		TextView tvDrawer = (TextView) convertView.findViewById(R.id.tvDrawer);
		tvDrawer.setText(drawerItems.get(groupPosition).getDrawerChildItems()
				.get(childPosition).getDrawerTitle());
		try {
			XmlResourceParser parser = context.getResources().getXml(
					R.color.drawer_text);
			ColorStateList colors = ColorStateList.createFromXml(
					context.getResources(), parser);
			tvDrawer.setTextColor(colors);
		} catch (Exception e) {
			// handle exceptions
		}
		Drawable icon = context.getResources().getDrawable(
				drawerItems.get(groupPosition).getDrawerChildItems()
						.get(childPosition).getDrawerIcon());
		tvDrawer.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (drawerItems.get(groupPosition).getDrawerChildItems() != null)
			return drawerItems.get(groupPosition).getDrawerChildItems().size();
		else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return drawerItems.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return drawerItems.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_list_item, null);
		}
		TextView tvDrawer = (TextView) convertView.findViewById(R.id.tvDrawer);
		ImageView ivIndicator = (ImageView) convertView
				.findViewById(R.id.ivIndicator);
		ImageView ivSettings = (ImageView) convertView
				.findViewById(R.id.ivSettings);
		tvDrawer.setText(drawerItems.get(groupPosition).getDrawerTitle());
		try {
			XmlResourceParser parser = context.getResources().getXml(
					R.color.drawer_text);
			ColorStateList colors = ColorStateList.createFromXml(
					context.getResources(), parser);
			tvDrawer.setTextColor(colors);
		} catch (Exception e) {
			// handle exceptions
		}
		if (getChildrenCount(groupPosition) == 0) {
			ivIndicator.startAnimation(fadeOut);
			ivSettings.startAnimation(fadeOut);
			ivIndicator.setVisibility(View.GONE);
			ivSettings.setVisibility(View.GONE);
		} else {
			ivIndicator.startAnimation(fadeIn);
			ivIndicator.setVisibility(View.VISIBLE);
			if (isExpanded) {
				ivSettings.startAnimation(fadeIn);
				ivSettings.setVisibility(View.VISIBLE);
				ivIndicator.setImageResource(R.drawable.navigation_collapse);
				ivSettings.setImageResource(R.drawable.action_settings);
			} else {
				ivSettings.startAnimation(fadeOut);
				ivSettings.setVisibility(View.GONE);
				ivIndicator.setImageResource(R.drawable.navigation_expand);
			}
		}
		Drawable icon = context.getResources().getDrawable(
				drawerItems.get(groupPosition).getDrawerIcon());
		tvDrawer.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}

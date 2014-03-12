package com.antares.slidedrawer.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.antares.slidedrawer.R;
import com.antares.slidedrawer.data.ChildItem;

public class TaskAdapter extends BaseAdapter {
	private List<ChildItem> childItems;
	private Activity context;
	private LayoutInflater inflater;

	public TaskAdapter(List<ChildItem> childItems, Activity context) {
		super();
		this.childItems = childItems;
		this.context = context;
		this.inflater = this.context.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return childItems.size();
	}

	@Override
	public ChildItem getItem(int position) {
		return childItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public class Holder {
		public TextView tvItemTitle;
		public TextView tvItemDay;
		public TextView tvItemCategory;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.task_list_item, parent,
					false);
			holder.tvItemTitle = (TextView) convertView
					.findViewById(R.id.tvItemTitle);
			holder.tvItemDay = (TextView) convertView
					.findViewById(R.id.tvItemDay);
			holder.tvItemCategory = (TextView) convertView
					.findViewById(R.id.tvItemCategory);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tvItemTitle.setText(getItem(position).getChildTitle());
		holder.tvItemDay.setText(getItem(position).getChildDay());
		holder.tvItemCategory.setText(getItem(position).getChildCategory());
		return convertView;
	}

	public void remove(ChildItem itemToDelete) {
		childItems.remove(childItems.indexOf(itemToDelete));
	}

	public void insert(ChildItem itemToDelete, int position) {
		if (childItems.size() > 0)
			childItems.set(position, itemToDelete);
		else
			childItems.add(position, itemToDelete);
		notifyDataSetChanged();
	}
}

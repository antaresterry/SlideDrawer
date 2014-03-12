package com.antares.slidedrawer.data;

import java.util.List;

public class DrawerItem {
	private String drawerTitle;
	private int drawerIcon;
	private List<DrawerChildItem> drawerChildItems; 
	
	public DrawerItem(String drawerTitle, int drawerIcon,
			List<DrawerChildItem> drawerChildItems) {
		super();
		this.drawerTitle = drawerTitle;
		this.drawerIcon = drawerIcon;
		this.drawerChildItems = drawerChildItems;
	}

	public String getDrawerTitle() {
		return drawerTitle;
	}

	public void setDrawerTitle(String drawerTitle) {
		this.drawerTitle = drawerTitle;
	}

	public int getDrawerIcon() {
		return drawerIcon;
	}

	public void setDrawerIcon(int drawerIcon) {
		this.drawerIcon = drawerIcon;
	}

	public List<DrawerChildItem> getDrawerChildItems() {
		return drawerChildItems;
	}

	public void setDrawerChildItems(List<DrawerChildItem> drawerChildItems) {
		this.drawerChildItems = drawerChildItems;
	}

}

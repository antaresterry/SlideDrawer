package com.antares.slidedrawer.data;

public class DrawerChildItem {
	private String drawerTitle;
	private int drawerIcon;

	public DrawerChildItem(String drawerTitle, int drawerIcon) {
		super();
		this.drawerTitle = drawerTitle;
		this.drawerIcon = drawerIcon;
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

}

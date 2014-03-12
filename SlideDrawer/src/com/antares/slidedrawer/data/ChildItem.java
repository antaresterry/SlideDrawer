package com.antares.slidedrawer.data;

public class ChildItem {
	private String childTitle;
	private String childDay;
	private String childCategory;

	public ChildItem(String childTitle, String childDay, String childCategory) {
		super();
		this.childTitle = childTitle;
		this.childDay = childDay;
		this.childCategory = childCategory;
	}

	public String getChildTitle() {
		return childTitle;
	}

	public void setChildTitle(String childTitle) {
		this.childTitle = childTitle;
	}

	public String getChildDay() {
		return childDay;
	}

	public void setChildDay(String childDay) {
		this.childDay = childDay;
	}

	public String getChildCategory() {
		return childCategory;
	}

	public void setChildCategory(String childCategory) {
		this.childCategory = childCategory;
	}

}

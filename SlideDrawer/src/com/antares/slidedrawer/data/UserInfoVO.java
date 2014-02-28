package com.antares.slidedrawer.data;

public class UserInfoVO {
	public String name;
	public int likes;
	public int following;
	public String default_post_format;
	public Blog[] blogs;
	public int primary;
	public String base_hostname;

	public class Blog {
		public String name;
		public String url;
		public int followers;
		public boolean primary;
		public String title;
		public String description;
		public boolean admin;
		public int updated;
		public int posts;
		public int messages;
		public int queue;
		public int drafts;
		public boolean share_likes;
		public boolean ask;
		public boolean ask_anon;
		public String tweet;
		public String facebook;
		public String facebook_opengraph_enabled;
		public String type;
	}

}

package com.antares.slidedrawer.data;

public class DashboardPostsVO {
	public String blog_name;
	public String id;
	public String post_url;
	public String type;
	public Integer timestamp;
	public String date;
	public String format;
	public String reblog_key;
	public String[] tags;
	public Boolean bookmarklet;
	public Boolean mobile;
	public String source_url;
	public String source_title;
	public Boolean liked;
	public String state;
	public Integer total_posts;
	public String title;
	public String body;
	public Long note_count;
	public String text;
	public String source;
	public String url;
	public String description;
	public String caption;
	public String asking_name;
	public String asking_url;
	public String question;
	public String answer;
	public String reblogged_from_id;
	public String reblogged_from_url;
	public String reblogged_from_name;
	public String reblogged_from_title;
	public String reblogged_root_url;
	public String reblogged_root_name;
	public String reblogged_root_title;
	public String artist;
	public String album;
	public String track_name;
	public String album_art;
	public String embed;
	public String plays;
	public String audio_url;
	public String audio_type;
	public String short_url;

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return o instanceof DashboardPostsVO
				&& ((DashboardPostsVO) o).id.equals(this.id);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return id.hashCode();
	}

}

package com.antares.slidedrawer.utils;

import com.antares.slidedrawer.Constants;

public class UrlComposer {

	// Blog Info
	public static String composeUrlBlogInfo(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_INFO + "?"
				+ Constants.REQUEST_PARAM_API_KEY + "="
				+ Constants.TUMBLR_CONSUMER_KEY;

		return composeResult;
	}

	// Blog Avatar
	public static String composeUrlBlogAvatar(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_AVATAR;

		return composeResult;
	}

	// Blog Avatar With Size
	public static String composeUrlBlogAvatarWithSize(String baseHostname,
			int size) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_AVATAR + "/" + size;

		return composeResult;
	}

	// Blog Likes
	public static String composeUrlBlogLikes(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_LIKES + "?"
				+ Constants.REQUEST_PARAM_API_KEY + "="
				+ Constants.TUMBLR_CONSUMER_KEY;

		return composeResult;
	}

	// Blog Followers
	public static String composeUrlBlogFollowers(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_FOLLOWERS;

		return composeResult;
	}

	// Blog Posts
	public static String composeUrlBlogPosts(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POSTS + "?"
				+ Constants.REQUEST_PARAM_API_KEY + "="
				+ Constants.TUMBLR_CONSUMER_KEY;

		return composeResult;
	}

	// Blog Posts
	public static String composeUrlBlogPostsWithType(String baseHostname,
			String type) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POSTS + type + "?"
				+ Constants.REQUEST_PARAM_API_KEY + "="
				+ Constants.TUMBLR_CONSUMER_KEY;

		return composeResult;
	}

	// Blog Posts Queue
	public static String composeUrlBlogPostsQueue(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POSTS
				+ Constants.REQUEST_BLOG_POSTS_QUEUE;

		return composeResult;
	}

	// Blog Posts Drafts
	public static String composeUrlBlogPostsDrafts(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POSTS
				+ Constants.REQUEST_BLOG_POSTS_DRAFT;

		return composeResult;
	}

	// Blog Posts Submissions
	public static String composeUrlBlogPostsSubmissions(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POSTS
				+ Constants.REQUEST_BLOG_POSTS_SUBMISSION;

		return composeResult;
	}

	// Blog Post
	public static String composeUrlBlogPost(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POST;

		return composeResult;
	}

	// Blog Post Edit
	public static String composeUrlBlogPostEdit(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POST
				+ Constants.REQUEST_BLOG_POST_EDIT;

		return composeResult;
	}

	// Blog Post Reblog
	public static String composeUrlBlogPostReblog(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POST
				+ Constants.REQUEST_BLOG_POST_REBLOG;

		return composeResult;
	}

	// Blog Post Delete
	public static String composeUrlBlogPostDelete(String baseHostname) {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_BLOG + "/"
				+ baseHostname + Constants.REQUEST_BLOG_POST
				+ Constants.REQUEST_BLOG_POST_DELETE;

		return composeResult;
	}

	// User Info
	public static String composeUrlUserInfo() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_INFO;

		return composeResult;
	}

	// User Dashboard
	public static String composeUrlUserDashboard() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_DASHBOARD;

		return composeResult;
	}

	// User Likes
	public static String composeUrlUserLikes() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_LIKES;

		return composeResult;
	}

	// User Following
	public static String composeUrlUserFollowing() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_FOLLOWING;

		return composeResult;
	}

	// User Follow
	public static String composeUrlUserFollow() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_FOLLOW;

		return composeResult;
	}

	// User Unfollow
	public static String composeUrlUserUnfollow() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_UNFOLLOW;

		return composeResult;
	}

	// User Like
	public static String composeUrlUserLike() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_LIKE;

		return composeResult;
	}

	// User Unlike
	public static String composeUrlUserUnlike() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_USER
				+ Constants.REQUEST_USER_UNLIKE;

		return composeResult;
	}

	// Tagged
	public static String composeUrlTagged() {
		String composeResult = "";

		composeResult = Constants.URL_REQUEST + Constants.REQUEST_TAGGED;

		return composeResult;
	}
}
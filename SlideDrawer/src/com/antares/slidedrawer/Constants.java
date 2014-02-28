package com.antares.slidedrawer;

import com.antares.slidedrawer.data.UserInfoVO;

public class Constants {
	// Tumblr
	public static final String TUMBLR_CONSUMER_KEY = "5d9XX14alnDaYpsq7Sc73kpSZUR2jwYnK5HniKawTh2jZmhXcK";
	public static final String TUMBLR_CONSUMER_SECRET = "p8EPsC8m7fN7K14NY4CwvpydfLMfRyXgeVDxdIL2pGlMGFmSLJ";
	public static final String TUMBLR_REQUEST_TOKEN_PREF = "Tumblr Request Token";
	public static final String TUMBLR_ACCESS_TOKEN_PREF = "Tumblr Access Token";
	public static final String REQUEST_TOKEN_URL = "https://www.tumblr.com/oauth/request_token";
	public static final String ACCESS_TOKEN_URL = "https://www.tumblr.com/oauth/access_token";
	public static final String AUTH_URL = "https://www.tumblr.com/oauth/authorize";
	public static final String CALLBACK_URL = "slidedrawer://slidedrawer.com/ok";
	public static final String PREF_NAME = "ares";
	public static final String PREF_PARAM_TOKEN = "Token";
	public static final String PREF_PARAM_TOKEN_SECRET = "TokenSecret";
	public static final String PREF_PARAM_TOKEN_VERIFIER = "TokenVerifier";

	// Tumblr API
	public static final String URL_REQUEST = "http://api.tumblr.com/v2";
	public static final String REQUEST_BLOG = "/blog";
	public static final String REQUEST_USER = "/user";
	public static final String REQUEST_TAGGED = "/tagged";
	public static final String REQUEST_PARAM_API_KEY = "api_key";
	public static final String REQUEST_PARAM_SIZE = "size";

	// Tumblr API - Blog
	public static final String REQUEST_BLOG_INFO = "/info";
	public static final String REQUEST_BLOG_AVATAR = "/avatar";
	public static final String REQUEST_BLOG_FOLLOWERS = "/followers";
	public static final String REQUEST_BLOG_LIKES = "/likes";
	public static final String REQUEST_BLOG_POSTS = "/posts";
	public static final String REQUEST_BLOG_POST = "/post";

	// Tumblr API - Blog - Posts
	public static final String REQUEST_BLOG_POSTS_QUEUE = "/queue";
	public static final String REQUEST_BLOG_POSTS_DRAFT = "/draft";
	public static final String REQUEST_BLOG_POSTS_SUBMISSION = "/submission";

	// Tumblr API - Blog - Post
	public static final String REQUEST_BLOG_POST_EDIT = "/edit";
	public static final String REQUEST_BLOG_POST_REBLOG = "/reblog";
	public static final String REQUEST_BLOG_POST_DELETE = "/delete";

	// Tumblr API - User
	public static final String REQUEST_USER_INFO = "/info";
	public static final String REQUEST_USER_DASHBOARD = "/dashboard";
	public static final String REQUEST_USER_LIKES = "/likes";
	public static final String REQUEST_USER_FOLLOWING = "/following";
	public static final String REQUEST_USER_FOLLOW = "/follow";
	public static final String REQUEST_USER_UNFOLLOW = "/unfollow";
	public static final String REQUEST_USER_LIKE = "/like";
	public static final String REQUEST_USER_UNLIKE = "/unlike";

	// From JSON
	public static UserInfoVO userInfo;
}

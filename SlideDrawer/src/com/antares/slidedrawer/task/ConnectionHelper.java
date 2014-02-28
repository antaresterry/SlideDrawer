package com.antares.slidedrawer.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class ConnectionHelper {

	private static int CONNECT_TIME_OUT = 30000;
	private static int READ_TIME_OUT = 30000;

	public static String cookies;

	public static HttpURLConnection getConnection(String uri)
			throws IOException {
		// InputStream result = null;
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setInstanceFollowRedirects(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		if (null != cookies) {
			// conn.addRequestProperty("Set-Cookie", cookies);
			conn.setRequestProperty("Cookie", cookies);
		}
		conn.setConnectTimeout(CONNECT_TIME_OUT);
		conn.setReadTimeout(READ_TIME_OUT);
		if (null == cookies)
			cookies = conn.getHeaderField("Set-Cookie");
		// Log.e("Cookies", cookies + "");
		Log.v("Connection Helper",
				"response message" + conn.getResponseMessage());
		return conn;
	}

	// public static HttpEntity getConnection(String url){
	// HttpClient httpclient = new DefaultHttpClient();
	// HttpGet httpget = new HttpGet(url);
	//
	// try {
	// HttpResponse response = httpclient.execute(httpget);
	//
	//
	// return response.getEntity();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static String inputStreamToString(InputStream input) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}

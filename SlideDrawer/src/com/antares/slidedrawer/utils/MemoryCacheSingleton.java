package com.antares.slidedrawer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheSingleton {
	private static LruCache<String, Bitmap> mMemoryCache = null;

	private MemoryCacheSingleton(Context context) {

	}

	public static LruCache<String, Bitmap> getInstance() {
		if (mMemoryCache == null) {
			// Get max available VM memory, exceeding this amount will throw an
			// OutOfMemory exception. Stored in kilobytes as LruCache takes an
			// int in its constructor.
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory / 8;

			mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
		}
		return mMemoryCache;
	}

}
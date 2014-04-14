package com.antares.slidedrawer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.View;

@SuppressLint("ViewConstructor")
public class AnimatedImageView extends View {

	private Movie movie;
	private Integer x;
	private Integer y;

	public AnimatedImageView(Context context, Movie movie, Integer x, Integer y) {
		super(context);
		this.movie = movie;
		this.x = x;
		this.y = y;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (movie != null) {
			long now = android.os.SystemClock.uptimeMillis();
			int dur = Math.max(movie.duration(), 1); // is it really animated?
			int pos = (int) (now % dur);
			movie.setTime(pos);
			movie.draw(canvas, x, y);
		}
	}
}

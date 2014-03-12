package com.antares.slidedrawer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

public class NewTaskActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	public void onClick(View vi) {
		int viewId = vi.getId();
		switch (viewId) {
		case R.id.more:
			vi.setVisibility(View.GONE);
			findViewById(R.id.sPriority).setVisibility(View.VISIBLE);
			findViewById(R.id.sLabels).setVisibility(View.VISIBLE);
			findViewById(R.id.sSubtask).setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
}

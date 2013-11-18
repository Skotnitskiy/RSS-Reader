package com.itcuties.android.reader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class Detail extends ActionBarActivity {
	static String url;
	static WebView news;
	 /*
	static LinearLayout newsLayout;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deital_layout);
		news = (WebView) findViewById(R.id.webViewNews);
		url = getIntent().getData().toString();
		//progressLayout = (LinearLayout) findViewById(R.id.progressBarLayout);
		new GetNewsDataTask().execute();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(getResources().getBoolean(R.bool.isTablet)){
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

		}
		return super.onOptionsItemSelected(item);
	}
}

class GetNewsDataTask extends AsyncTask<Void, Void, Void> {

	

	@Override
	protected Void doInBackground(Void... params) {
		Detail.news.loadUrl(Detail.url);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
	}

}

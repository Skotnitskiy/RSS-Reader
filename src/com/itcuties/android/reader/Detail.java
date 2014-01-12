package com.itcuties.android.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Detail extends ActionBarActivity {
	static String url;
	static WebView news;
	public static Detail loc;

	/*
	 * static LinearLayout newsLayout;
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deital_layout);

		loc = this;
		news = (WebView) findViewById(R.id.webViewNews);
		url = getIntent().getExtras().getBundle("news").getString("link"); // getIntent().getData();
		news.setWebViewClient(new WbClient());
		ITCutiesReaderAppActivity.intent.putExtra(Intent.EXTRA_TEXT, url);
		// Устанавливаем интент
		ITCutiesReaderAppActivity.mShareActionProvider.setShareIntent(ITCutiesReaderAppActivity.intent);
		// progressLayout = (LinearLayout) findViewById(R.id.progressBarLayout);
		Detail.news.loadUrl(Detail.url);// http://www.ukr.net/
		Toast.makeText(Detail.loc, Detail.url, Toast.LENGTH_LONG).show();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (getResources().getBoolean(R.bool.isTablet)) {
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(2).setVisible(false);
		

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

		}
		return super.onOptionsItemSelected(item);
	}

	private class WbClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}

package com.itcuties.android.reader;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.itcuties.android.reader.data.MyContentProvider;
import com.itcuties.android.reader.data.RssItem;
import com.itcuties.android.reader.util.RssReader;

public class ITCutiesReaderAppActivity extends ActionBarActivity {

	// A reference to the local object
	private ITCutiesReaderAppActivity local;
	static List<RssItem> rs;
	WebView news;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getBoolean(R.bool.isTablet))
			setContentView(R.layout.main_sw600);
		else
			setContentView(R.layout.main);
		local = this;
		new MyContentProvider();
		new GetRSSDataTask().execute("http://news.liga.net/smi/rss.xml");
		

		// http://aerostat.rpod.ru/rss.xml

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		stopService(SplashActivity.updateRssIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.refresh:
			new GetRSSDataTask().execute("http://news.liga.net/smi/rss.xml");
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem>>
			implements OnItemClickListener {

		@Override
		protected List<RssItem> doInBackground(String... urls) {

			try {
				// Create RSS reader
				RssReader rssReader = new RssReader(urls[0]);

				// Parse RSS, get items
				return rssReader.getItems();

			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.ProgressLayout).setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {

			findViewById(R.id.ProgressLayout).setVisibility(View.GONE);

			ListView itcItems = (ListView) findViewById(R.id.listMainView);

			ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,
					android.R.layout.simple_list_item_1, result);

			itcItems.setAdapter(adapter);
			rs = result;
			
			news = (WebView) findViewById(R.id.webViewNews);

			if (getResources().getBoolean(R.bool.isTablet)) {
				news.loadUrl(rs.get(0).getLink());
				findViewById(R.id.progress_tablet_layout).setVisibility(
						View.GONE);

			}

			itcItems.setOnItemClickListener(this);
		}

		

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {

			//Toast.makeText(local, getLastPubDate(), Toast.LENGTH_LONG).show();
			// Set list view item click listener
			if (getResources().getBoolean(R.bool.isTablet)) {

				news.loadUrl(rs.get(pos).getLink());

			} else {
				Intent intent = new Intent(ITCutiesReaderAppActivity.this,
						Detail.class);
				intent.setData(Uri.parse(rs.get(pos).getLink()));
				ITCutiesReaderAppActivity.this.startActivity(intent);

			}

		}
	}

}
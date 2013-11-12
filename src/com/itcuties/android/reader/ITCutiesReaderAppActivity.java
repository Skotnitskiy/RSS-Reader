package com.itcuties.android.reader;

import java.util.List;

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

import com.itcuties.android.reader.data.RssItem;
import com.itcuties.android.reader.util.RssReader;

//import com.itcuties.android.reader.listeners.ListListener;

public class ITCutiesReaderAppActivity extends ActionBarActivity {

	// A reference to the local object
	private ITCutiesReaderAppActivity local;
	List<RssItem> rs;
	WebView newsTablet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set view
		setContentView(R.layout.main);
		// imgRefresh.setAnimation(anm);

		// Set reference to this activity
		local = this;
		new GetRSSDataTask().execute("http://www.itcuties.com/feed/");

		// http://aerostat.rpod.ru/rss.xml

		// Debug the thread name
		Log.d("ITCRssReader", Thread.currentThread().getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * LayoutInflater inflater = (LayoutInflater) getApplication()
		 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); ImageView iv =
		 * (ImageView) inflater.inflate(R.layout.actionbar_layout,null);
		 * 
		 * Animation rotation = AnimationUtils.loadAnimation(getApplication(),
		 * R.anim.rotate); rotation.setRepeatCount(5);
		 * iv.startAnimation(rotation);
		 */
		switch (item.getItemId()) {
		case R.id.refresh:
			// item.setActionView(iv);
			new GetRSSDataTask().execute("http://www.itcuties.com/feed/");
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem>>
			implements OnItemClickListener {

		@Override
		protected List<RssItem> doInBackground(String... urls) {

			// Debug the task thread name
			Log.d("ITCRssReader", Thread.currentThread().getName());

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
			// Get a ListView from main view
			ListView itcItems = (ListView) findViewById(R.id.listMainView);

			// Create a list adapter
			ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,
					android.R.layout.simple_list_item_1, result);
			// Set list adapter for the ListView
			itcItems.setAdapter(adapter);
			rs = result;
			newsTablet = (WebView) findViewById(R.id.newsTablet);

			if (getResources().getBoolean(R.bool.isTablet)) {
				newsTablet.loadUrl(rs.get(0).getLink());
			}

			// Set list view item click listener
			itcItems.setOnItemClickListener(this);
		}

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {

			if (getResources().getBoolean(R.bool.isTablet)) {
				//
				newsTablet.loadUrl(rs.get(pos).getLink());

			} else {
				Intent intent = new Intent(ITCutiesReaderAppActivity.this,
						Detail.class);
				intent.setData(Uri.parse(rs.get(pos).getLink()));
				ITCutiesReaderAppActivity.this.startActivity(intent);

			}

		}
	}

}
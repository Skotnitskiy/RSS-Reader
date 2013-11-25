package com.itcuties.android.reader.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.itcuties.android.reader.ITCutiesReaderAppActivity;
import com.itcuties.android.reader.R;
import com.itcuties.android.reader.SplashActivity;
import com.itcuties.android.reader.data.RssItem;
import com.itcuties.android.reader.util.RssReader;

public class UpdateRssService extends Service {

	public static NotificationManager notificationManager;
	public static NotificationCompat.Builder builder;
	private String lastDate;
	private Timer timer = new Timer();
	private long TIMER_START_DELAY = 10000L;
	private int delay = 30;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notifyShow(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Служба запущена", Toast.LENGTH_SHORT).show();
		//new GetRSSDate().execute("http://news.liga.net/smi/rss.xml");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				new GetRSSDate().execute("http://news.liga.net/smi/rss.xml");

			}
		}, TIMER_START_DELAY, delay * 1000 * 60);

		return START_REDELIVER_INTENT;

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
		notificationManager.cancel(0);

	}

	public static void notifyShow(Context context) {
		builder = new NotificationCompat.Builder(context).setOngoing(true)
				.setSmallIcon(R.drawable.ic_notify)
				.setContentTitle("Only news").setContentText("Read news");

		Intent resultIntent = new Intent(context, SplashActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(0, builder.build());
	}

	private class GetRSSDate extends AsyncTask<String, Void, List<RssItem>> {

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
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {

			lastDate = result.get(0).getpubDate();
			if(!lastDate.equals(ITCutiesReaderAppActivity.getLastPubDate())){
				Toast.makeText(UpdateRssService.this,"News updated", Toast.LENGTH_LONG).show();
			}
			else Toast.makeText(UpdateRssService.this,"0 news updated", Toast.LENGTH_LONG).show();
				
		}

	}
}

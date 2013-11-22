package com.itcuties.android.reader.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.itcuties.android.reader.R;
import com.itcuties.android.reader.SplashActivity;

public class UpdateRssService extends Service {
	
	public static NotificationManager notificationManager;
	public static NotificationCompat.Builder builder;

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

		return START_REDELIVER_INTENT;

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
		notificationManager.cancel(0);

	}
	
	public static void notifyShow(Context context){
         builder =
                new NotificationCompat.Builder(context)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setContentTitle("Only news")
                        .setContentText("Read news");

        Intent resultIntent = new Intent(context, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(0, builder.build());
    }
}

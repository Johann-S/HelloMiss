package com.jservoire.hellomiss;

import java.util.Calendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Calendar cal = Calendar.getInstance();
		if ( cal.get(Calendar.HOUR_OF_DAY) == 10 ) 
		{
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Une nouvelle Madame !")
			.setContentText("Une nouvelle Madame est disponible venez la voir !");
			
			Intent resultIntent = new Intent(context, MainActivity.class);
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);	
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);		
			mNotifyMgr.notify(0, mBuilder.build());
		}
	}

}

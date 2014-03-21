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
	private Context context;

	private void belleNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
				.setContentTitle("Bonjour ma belle !")
				.setSmallIcon(R.drawable.bmab)
				.setContentText(context.getResources().getString(R.string.belleNotif));

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hBll");	
		sendNotification(mBuilder, resultIntent);
	}

	private void bombNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
				.setContentTitle("Bonjour la bombe !")
				.setSmallIcon(R.drawable.blab)
				.setContentText(context.getResources().getString(R.string.bombeNotif));

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hBmb");	
		sendNotification(mBuilder, resultIntent);		
	}

	private void dailyNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
				.setContentTitle("Daily Demoiselle !")
				.setSmallIcon(R.drawable.ddmlle)
				.setContentText(context.getResources().getString(R.string.dailyNotif));

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "dMlle");
		sendNotification(mBuilder, resultIntent);	
	}

	private NotificationCompat.Builder getNotificationBuilder()
	{
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setAutoCancel(true);	
		return mBuilder;
	}

	private void missNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
				.setContentTitle("Bonjour Mademoiselle !")
				.setSmallIcon(R.drawable.bmlle)
				.setContentText(context.getResources().getString(R.string.mlleNotif));

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hMiss");
		sendNotification(mBuilder, resultIntent);		
	}

	private void mmeNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
				.setContentTitle("Bonjour Madame !")
				.setSmallIcon(R.drawable.bmme)
				.setContentText(context.getResources().getString(R.string.mmeNotif));

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hMrs");
		sendNotification(mBuilder, resultIntent);	
	}

	private void odobNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
				.setContentTitle("One day, One Babe !")
				.setSmallIcon(R.drawable.odob)
				.setContentText(context.getResources().getString(R.string.obobNotif));

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hOdob");	
		sendNotification(mBuilder, resultIntent);		
	}

	@Override
	public void onReceive(final Context context, final Intent intent) 
	{
		this.context = context;
		Calendar cal = Calendar.getInstance();

		if ( cal.get(Calendar.HOUR_OF_DAY) == 10 && cal.get(Calendar.MINUTE) == 0 ) {
			mmeNotification();
		}

		if ( cal.get(Calendar.HOUR_OF_DAY) == 00 && cal.get(Calendar.MINUTE) == 30 ) {
			missNotification();
		}

		if ( cal.get(Calendar.HOUR_OF_DAY) == 9 && cal.get(Calendar.MINUTE) == 2 ) {
			belleNotification();
		}

		if ( cal.get(Calendar.HOUR_OF_DAY) == 00 && cal.get(Calendar.MINUTE) == 02 ) {
			odobNotification();
		}

		if ( cal.get(Calendar.HOUR_OF_DAY) == 01 && cal.get(Calendar.MINUTE) == 00 ) {
			bombNotification();
		}

		if ( cal.get(Calendar.HOUR_OF_DAY) == 00 && cal.get(Calendar.MINUTE) == 01 ) {
			dailyNotification();
		}
	}

	private void sendNotification(final NotificationCompat.Builder mBuilder, final Intent intentNotif)
	{
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,(int)System.currentTimeMillis(),intentNotif,PendingIntent.FLAG_UPDATE_CURRENT);	
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);		
		mNotifyMgr.notify((int)System.currentTimeMillis(), mBuilder.build());	
	}
}

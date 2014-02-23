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
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		this.context = context;
		Calendar cal = Calendar.getInstance();
		
		if ( cal.get(Calendar.HOUR_OF_DAY) == 10 && cal.get(Calendar.MINUTE) == 0 ) {
			this.MmeNotification();
		}
		
		if ( cal.get(Calendar.HOUR_OF_DAY) == 00 && cal.get(Calendar.MINUTE) == 30 ) {
			this.MissNotification();
		}
		
		if ( cal.get(Calendar.HOUR_OF_DAY) == 9 && cal.get(Calendar.MINUTE) == 2 ) {
			this.BelleNotification();
		}
		
		if ( cal.get(Calendar.HOUR_OF_DAY) == 00 && cal.get(Calendar.MINUTE) == 02 ) {
			this.OdobNotification();
		}
		
		if ( cal.get(Calendar.HOUR_OF_DAY) == 01 && cal.get(Calendar.MINUTE) == 00 ) {
			this.BombNotification();
		}
	}
	
	private void MmeNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
		.setContentTitle("Bonjour Madame !")
		.setSmallIcon(R.drawable.bmme)
		.setContentText("Une nouvelle Madame est disponible !");
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hMrs");
		sendNotification(mBuilder, resultIntent);	
	}
	
	private void MissNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
		.setContentTitle("Bonjour Mademoiselle !")
		.setSmallIcon(R.drawable.bmlle)
		.setContentText("Une nouvelle demoiselle est disponible !");
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hMiss");
		sendNotification(mBuilder, resultIntent);		
	}
	
	private void BelleNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
		.setContentTitle("Bonjour ma belle !")
		.setSmallIcon(R.drawable.bmab)
		.setContentText("Une nouvelle belle demoiselle est disponible !");
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hBll");	
		sendNotification(mBuilder, resultIntent);
	}
	
	private void OdobNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
		.setContentTitle("One day, One Babe !")
		.setSmallIcon(R.drawable.odob)
		.setContentText("One day... One babe !");
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hOdob");	
		sendNotification(mBuilder, resultIntent);		
	}
	
	private void BombNotification()
	{
		NotificationCompat.Builder mBuilder = getNotificationBuilder()
		.setContentTitle("Bonjour la bombe !")
		.setSmallIcon(R.drawable.blab)
		.setContentText("Une nouvelle bombe !");
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra("prefix", "hBmb");	
		sendNotification(mBuilder, resultIntent);		
	}
	
	private NotificationCompat.Builder getNotificationBuilder()
	{
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setAutoCancel(true);	
		return mBuilder;
	}
	
	private void sendNotification(NotificationCompat.Builder mBuilder, Intent intentNotif)
	{
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,(int)System.currentTimeMillis(),intentNotif,PendingIntent.FLAG_UPDATE_CURRENT);	
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);		
		mNotifyMgr.notify((int)System.currentTimeMillis(), mBuilder.build());	
	}
}

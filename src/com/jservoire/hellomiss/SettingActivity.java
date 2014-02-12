package com.jservoire.hellomiss;

import java.util.Calendar;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SettingActivity extends Activity 
{
	private PendingIntent pendingIntentNotifMme;
	private PendingIntent pendingIntentNotifMiss;
	private PendingIntent pendingIntentNotifBelle;
	private PendingIntent pendingOdob;
	private AlarmManager alarmManager;
	private CheckBox chkMmeNotif;
	private CheckBox chkMissNotif;
	private CheckBox chkBelleNotif;
	private CheckBox chkOdobNotif;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		Intent intentNotif = new Intent(SettingActivity.this, NotificationReceiver.class);
		pendingIntentNotifMme = PendingIntent.getBroadcast(SettingActivity.this, 0, intentNotif, 0);
		pendingIntentNotifMiss = PendingIntent.getBroadcast(SettingActivity.this, 1, intentNotif, 0);
		pendingIntentNotifBelle = PendingIntent.getBroadcast(SettingActivity.this, 2, intentNotif, 0);
		pendingOdob = PendingIntent.getBroadcast(SettingActivity.this, 3, intentNotif, 0);
		alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		
		chkMmeNotif = (CheckBox)findViewById(R.id.NotifMmeCheckBox);
		chkMmeNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	alarmManager.cancel(pendingIntentNotifMme);
		        	startMmeNotification();
		        	setMmePrefNotif(true);
		        }
		        else 
		        {
		        	alarmManager.cancel(pendingIntentNotifMme);
		        	setMmePrefNotif(false);
		        }
		    }
		});
		
		chkMissNotif = (CheckBox)findViewById(R.id.NotifMissCheckBox);
		chkMissNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	alarmManager.cancel(pendingIntentNotifMiss);
		        	startMissNotification();
		        	setMissPrefNotif(true);
		        }
		        else 
		        {
		        	alarmManager.cancel(pendingIntentNotifMiss);
		        	setMissPrefNotif(false);
		        }
		    }
		});
		
		chkBelleNotif = (CheckBox)findViewById(R.id.NotifBelleCheckBox);
		chkBelleNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	alarmManager.cancel(pendingIntentNotifBelle);
		        	startBelleNotification();
		        	setBellePrefNotif(true);
		        }
		        else 
		        {
		        	alarmManager.cancel(pendingIntentNotifBelle);
		        	setBellePrefNotif(false);
		        }
		    }
		});
		
		chkOdobNotif = (CheckBox)findViewById(R.id.oDoBcheckBox);
		chkOdobNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	alarmManager.cancel(pendingOdob);
		        	startOdobNotification();
		        	setOdobPrefNotif(true);
		        }
		        else 
		        {
		        	alarmManager.cancel(pendingOdob);
		        	setOdobPrefNotif(false);
		        }
		    }
		});
		
		this.loadNotificationPref();
	}
	
	public void startMmeNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotifMme);	
	} 
	
	public void setMmePrefNotif(boolean isNotif)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("MmeNotifications", isNotif);
		editor.commit();
	}
	
	public void startMissNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotifMiss);		
	}
	
	public void setMissPrefNotif(boolean isNotif)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("MissNotifications", isNotif);
		editor.commit();
	}
	
	public void startBelleNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 2);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotifBelle);		
	}
	
	public void setBellePrefNotif(boolean isNotif)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("BelleNotifications", isNotif);
		editor.commit();
	}
	
	public void startOdobNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 2);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingOdob);	
	}
	
	public void setOdobPrefNotif(boolean isNotif)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("OdobNotifications", isNotif);
		editor.commit();
	}
	
	private void loadNotificationPref()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isNotifMme = preferences.getBoolean("MmeNotifications", false);
		boolean isNotifMiss = preferences.getBoolean("MissNotifications", false);
		boolean isNotifBelle = preferences.getBoolean("BelleNotifications", false);
		boolean isNotifOdob = preferences.getBoolean("OdobNotifications", false);
		chkMmeNotif.setChecked(isNotifMme);
		chkMissNotif.setChecked(isNotifMiss);
		chkBelleNotif.setChecked(isNotifBelle);
		chkOdobNotif.setChecked(isNotifOdob);
	}
}

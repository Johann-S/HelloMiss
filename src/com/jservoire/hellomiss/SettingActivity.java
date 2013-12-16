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
	private Intent intentNotif;
	private PendingIntent pendingIntentNotif;
	private AlarmManager alarmManagerMme;
	private AlarmManager alarmManagerMiss;
	private CheckBox chkMmeNotif;
	private CheckBox chkMissNotif;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		intentNotif = new Intent(SettingActivity.this, NotificationReceiver.class);
		pendingIntentNotif = PendingIntent.getBroadcast(SettingActivity.this, 0, intentNotif, 0);
		alarmManagerMme = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		alarmManagerMiss = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		
		chkMmeNotif = (CheckBox)findViewById(R.id.NotifMmeCheckBox);
		chkMmeNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	alarmManagerMme.cancel(pendingIntentNotif);
		        	startMmeNotification();
		        	setMmePrefNotif(true);
		        }
		        else 
		        {
		        	alarmManagerMme.cancel(pendingIntentNotif);
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
		        	alarmManagerMiss.cancel(pendingIntentNotif);
		        	startMissNotification();
		        	setMissPrefNotif(true);
		        }
		        else 
		        {
		        	alarmManagerMiss.cancel(pendingIntentNotif);
		        	setMissPrefNotif(false);
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
		alarmManagerMme.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotif);	
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
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		alarmManagerMiss.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotif);		
	}
	
	public void setMissPrefNotif(boolean isNotif)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("MissNotifications", isNotif);
		editor.commit();
	}
	
	private void loadNotificationPref()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isNotifMme = preferences.getBoolean("MmeNotifications", false);
		boolean isNotifMiss = preferences.getBoolean("MissNotifications", false);
		chkMmeNotif.setChecked(isNotifMme);
		chkMissNotif.setChecked(isNotifMiss);
	}
}

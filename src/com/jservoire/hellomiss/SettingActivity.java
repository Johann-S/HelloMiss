package com.jservoire.hellomiss;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity 
{
	private PendingIntent pendingIntentNotifMme;
	private PendingIntent pendingIntentNotifMiss;
	private PendingIntent pendingIntentNotifBelle;
	private PendingIntent pendingOdob;
	private PendingIntent pendingBomb;
	private PendingIntent pendingDaily;
	private AlarmManager alarmManager;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private CheckBox chkMmeNotif;
	private CheckBox chkMissNotif;
	private CheckBox chkBelleNotif;
	private CheckBox chkOdobNotif;
	private CheckBox chkBombe;
	private CheckBox chkDaily;

	private void loadNotificationPref()
	{
		boolean isNotifMme = preferences.getBoolean("MmeNotifications",false);
		boolean isNotifMiss = preferences.getBoolean("MissNotifications",false);
		boolean isNotifBelle = preferences.getBoolean("BelleNotifications",false);
		boolean isNotifOdob = preferences.getBoolean("OdobNotifications",false);
		boolean isNotifBomb = preferences.getBoolean("BombNotifications",false);
		boolean isNotifDaily = preferences.getBoolean("DailyNotifications",false);
		chkMmeNotif.setChecked(isNotifMme);
		chkMissNotif.setChecked(isNotifMiss);
		chkBelleNotif.setChecked(isNotifBelle);
		chkOdobNotif.setChecked(isNotifOdob);
		chkBombe.setChecked(isNotifBomb);
		chkDaily.setChecked(isNotifDaily);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		Intent intentNotif = new Intent(SettingActivity.this, NotificationReceiver.class);
		pendingIntentNotifMme = PendingIntent.getBroadcast(SettingActivity.this,0,intentNotif,0);
		pendingIntentNotifMiss = PendingIntent.getBroadcast(SettingActivity.this,1,intentNotif,0);
		pendingIntentNotifBelle = PendingIntent.getBroadcast(SettingActivity.this,2,intentNotif,0);
		pendingOdob = PendingIntent.getBroadcast(SettingActivity.this,3,intentNotif,0);
		pendingBomb = PendingIntent.getBroadcast(SettingActivity.this,4,intentNotif,0);
		pendingDaily = PendingIntent.getBroadcast(SettingActivity.this,5,intentNotif,0);
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

		chkMmeNotif = (CheckBox)findViewById(R.id.NotifMmeCheckBox);
		chkMmeNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
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
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
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
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
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
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
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

		chkBombe = (CheckBox)findViewById(R.id.bmbCheckBox);
		chkBombe.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
			{
				if ( isChecked )
				{
					alarmManager.cancel(pendingBomb);
					startBombNotification();
					setBombPrefNotif(true);
				}
				else 
				{
					alarmManager.cancel(pendingBomb);
					setBombPrefNotif(false);
				}
			}
		});

		chkDaily = (CheckBox)findViewById(R.id.NotifDailyCheckBox);
		chkDaily.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
			{
				if ( isChecked )
				{
					alarmManager.cancel(pendingDaily);
					startDailyNotification();
					setDailyPrefNotif(true);
				}
				else 
				{
					alarmManager.cancel(pendingDaily);
					setDailyPrefNotif(false);
				}
			}
		});

		loadNotificationPref();
	}

	public void setBellePrefNotif(final boolean isNotif)
	{
		editor.putBoolean("BelleNotifications", isNotif);
		editor.commit();
	}

	public void setBombPrefNotif(final boolean isNotif)
	{
		editor.putBoolean("BombNotifications", isNotif);
		editor.commit();
	}

	public void setDailyPrefNotif(final boolean isNotif)
	{
		editor.putBoolean("DailyNotifications", isNotif);
		editor.commit();
	}

	public void setMissPrefNotif(final boolean isNotif)
	{
		editor.putBoolean("MissNotifications", isNotif);
		editor.commit();
	}

	public void setMmePrefNotif(final boolean isNotif)
	{
		editor.putBoolean("MmeNotifications", isNotif);
		editor.commit();
	}

	public void setOdobPrefNotif(final boolean isNotif)
	{
		editor.putBoolean("OdobNotifications", isNotif);
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

	public void startBombNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 01);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingBomb);	
	}

	public void startDailyNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 01);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingDaily);		

	}

	public void startMissNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotifMiss);		
	}

	public void startMmeNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotifMme);	
	}

	public void startOdobNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 2);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingOdob);	
	}
}

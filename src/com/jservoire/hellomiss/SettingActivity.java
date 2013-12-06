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
	private AlarmManager alarmManager;
	private CheckBox chkNotif;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		intentNotif = new Intent(SettingActivity.this, NotificationReceiver.class);
		pendingIntentNotif = PendingIntent.getBroadcast(SettingActivity.this, 0, intentNotif, 0);
		alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		
		chkNotif = (CheckBox)findViewById(R.id.NotifcheckBox);
		chkNotif.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
	        	   alarmManager.cancel(pendingIntentNotif);
	        	   startNotification();
	        	   setPrefNotification(true);
		        }
		        else {
		        	alarmManager.cancel(pendingIntentNotif);
		        	setPrefNotification(false);
		        }
		    }
		});
		
		this.loadNotificationPref();
	}
	
	private void loadNotificationPref()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isNotif = preferences.getBoolean("notifications", false);
		this.chkNotif.setChecked(isNotif);
	}

	public void startNotification()
	{
		Calendar calendar = Calendar.getInstance();	
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNotif);	
	} 
	
	public void setPrefNotification(boolean isNotif)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("notifications", isNotif);
		editor.commit();
	}
}

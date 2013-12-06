package com.jservoire.hellomiss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private ImageView image;
	private MainActivity context;
	private Toast loading;
	private Intent imgService;
	private final static int ID_SAVE_DIALOG  = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		this.image = (ImageView)findViewById(R.id.mainImage);		
		IntentFilter filter = new IntentFilter("downloadFinished");
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,filter);
    	loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
    	loading.show();
    	imgService = new Intent(MainActivity.this, ImageService.class);
		startService(imgService);
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() 
	{
		  @Override
		  public void onReceive(Context context, Intent intent) 
		  {
			  loading.cancel();
			  File imgFile = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/newMiss.jpg");
			  if(imgFile.exists())
			  {
			      Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			      image.setImageBitmap(myBitmap);
			  }		  
		  }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingActivity.class);
				startActivity(intent);
				return true;
			case R.id.refresh:
		    	loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
		    	loading.show();
		    	startService(imgService);
				return true;
			case R.id.idSaveImage:
				this.saveImage();
				return true;	
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void saveImage()
	{
	    File savePath = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/");
	    if ( savePath != null )
	    {
	    	Calendar c = Calendar.getInstance();
	    	SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
	    	String nameFile = df.format(c.getTime())+".jpg";	    	
	    	File imgFile = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/newMiss.jpg");
	    	File newImg = new File(savePath.getAbsolutePath(),nameFile);

			try 
			{
				InputStream in = new FileInputStream(imgFile);
				OutputStream out = new FileOutputStream(newImg);
				
		        // Transfer bytes from in to out
		        byte[] buf = new byte[1024];
		        int len;
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }
		        in.close();
		        out.close();		        
		    	loading = Toast.makeText(context, "Image sauvegardée !",Toast.LENGTH_SHORT);
		    	loading.show();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Override
	public void onResume(){
	    super.onResume();
	}
}

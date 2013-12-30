package com.jservoire.hellomiss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.*;
import com.jservoire.tools.ListHello;
import com.jservoire.tools.PaginateHello;

public class MainActivity extends Activity
{
	private ImageView image;
	private MainActivity context;
	private Toast loading;
	private ProgressBar loader;
	private Intent imgService;
	private Bitmap imageLoaded;
	private SlidingMenu slidMenu;
	private String prefixFile;
	private PaginateHello paginator;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		this.image = (ImageView)findViewById(R.id.mainImage);
		this.loader = (ProgressBar)findViewById(R.id.loader);
		ImageButton nextBtn = (ImageButton)findViewById(R.id.imageButtonNext);
		nextBtn.setOnClickListener(NextListener);
		ImageButton prevBtn = (ImageButton)findViewById(R.id.imageButtonPrev);
		prevBtn.setOnClickListener(PrevListener);
		paginator = PaginateHello.getInstance(this);
		
		slidMenu = new SlidingMenu(this);
		slidMenu.setMode(SlidingMenu.LEFT);
		slidMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		slidMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidMenu.setFadeDegree(0.35f);
		slidMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidMenu.setMenu(R.layout.activity_slidingmenu_fragment);       
        getActionBar().setDisplayHomeAsUpEnabled(true);
		
		IntentFilter filter = new IntentFilter("downloadFinished");
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,filter);
		
		IntentFilter filterSlider = new IntentFilter("selectedItem");
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverSlider,filterSlider);
		
		IntentFilter filterErr = new IntentFilter("errorService");
		LocalBroadcastManager.getInstance(this).registerReceiver(receiverErr,filterErr);
		
    	loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
    	loading.show();
    	loader.setVisibility(View.VISIBLE);
    	
		Intent intActivity = getIntent();
		prefixFile = ( intActivity != null && intActivity.getExtras() != null ) ? intActivity.getStringExtra("prefix") : "hMrs";
		String urlHello = ListHello.getListHelloByPrefix(this).get(prefixFile);
		String titleActivity = ListHello.getHellosNameByPrefix(this).get(prefixFile);
		setTitle(titleActivity);
		
    	imgService = new Intent(MainActivity.this, ImageService.class);
    	imgService.putExtra("url", urlHello);
		startService(imgService);
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() 
	{
		  @Override
		  public void onReceive(Context ctx, Intent intent) 
		  {
			  loading.cancel();
			  loader.setVisibility(View.INVISIBLE);			  
			  prefixFile = ( intent != null && intent.getExtras() != null ) ? intent.getStringExtra("prefix") : "hMrs";
			  
			  File imgFile = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/newMiss.jpg");
			  if ( imgFile.exists() )
			  {
				  String titleActivity = ListHello.getHellosNameByPrefix(context).get(prefixFile);
				  context.setTitle(titleActivity);
			      imageLoaded = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			      image.setImageBitmap(imageLoaded);
			      imgFile.delete();
			  }		  
		  }
	};
	
	private BroadcastReceiver receiverErr = new BroadcastReceiver() 
	{
		  @Override
		  public void onReceive(Context context, Intent intent) 
		  {
			  loading.cancel();
			  loader.setVisibility(View.INVISIBLE);			  
			  byte idErr = intent.getByteExtra("idErr", (byte) 0);
			  displayError(idErr);
		  }
	};
	
	private BroadcastReceiver mMessageReceiverSlider = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
	        if ( slidMenu.isMenuShowing() ) 
	        {
	        	slidMenu.toggle();
	        	image.setImageBitmap(null);
				loader.setVisibility(View.VISIBLE);
				loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
				loading.show();
				paginator.setPageToNull();
	        }
		}
	};
	
	private OnClickListener NextListener = new OnClickListener() 
	{	
		@Override
		public void onClick(View v) 
		{			
    		image.setImageBitmap(null);
    		loader.setVisibility(View.VISIBLE);
        	loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
        	loading.show();
        	
			String urlHello = paginator.nextImage(prefixFile);
	    	imgService = new Intent(MainActivity.this, ImageService.class);
	    	imgService.putExtra("url", urlHello);
			startService(imgService);
		}
	};
	
	private OnClickListener PrevListener = new OnClickListener() 
	{	
		@Override
		public void onClick(View v) 
		{
    		image.setImageBitmap(null);
    		loader.setVisibility(View.VISIBLE);
        	loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
        	loading.show();
        	
        	String urlHello = paginator.prevImage(prefixFile);
	    	imgService = new Intent(MainActivity.this, ImageService.class);
	    	imgService.putExtra("url", urlHello);
			startService(imgService);
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
    public void onBackPressed() 
	{
        if ( slidMenu.isMenuShowing() ) {
            slidMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if ( keyCode == KeyEvent.KEYCODE_MENU ) 
        {
            slidMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch( item.getItemId() )
		{
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingActivity.class);
				startActivity(intent);
				return true;
			case R.id.refresh:
				refreshImage();
				return true;
			case R.id.idSaveImage:
				this.saveImage();
				return true;
			case R.id.idSetWall:
				this.setWallpaper();
				return true;
	        case android.R.id.home:
	            slidMenu.toggle();
	            return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void refreshImage()
	{
    	imgService = new Intent(MainActivity.this, ImageService.class);    	
    	String urlHello = ListHello.getListHelloByPrefix(this).get(prefixFile);    	
    	if ( urlHello != null )
    	{
    		image.setImageBitmap(null);
        	imgService.putExtra("url",urlHello);
    		loader.setVisibility(View.VISIBLE);
        	loading = Toast.makeText(context, "Chargement...",Toast.LENGTH_LONG);
        	loading.show();
        	startService(imgService);
    	}
	}

	public void saveImage()
	{
	    File savePath = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/");
	    if ( savePath != null )
	    {
	    	String nameFile = this.getFileName();
	    	File newImg = new File(savePath.getAbsolutePath(),nameFile);

			try 
			{
	            FileOutputStream f = new FileOutputStream(newImg);
	            imageLoaded.compress(Bitmap.CompressFormat.JPEG, 85, f);
		    	loading = Toast.makeText(context, "Image saved !",Toast.LENGTH_SHORT);
		    	loading.show();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private String getFileName()
	{
		String fileName = null;
		String cPage = paginator.getPage();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		if ( cPage == null ) {   	
	    	fileName = prefixFile+"_"+df.format(c.getTime())+".jpg";
		}
		else
		{
			int intPage = Integer.parseInt(cPage);
			intPage *= -1;			
			c.add(Calendar.DAY_OF_MONTH, intPage);
			fileName = prefixFile+"_"+df.format(c.getTime())+".jpg";
		}
		
		return fileName;
	}
	
	public void setWallpaper()
	{
	    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
	    
	    try {
			myWallpaperManager.setBitmap(imageLoaded);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void displayError(byte idErr)
	{
		String msg = null;		
		switch (idErr) 
		{
			case 1:
				msg = getResources().getString(R.string.errParse);
				break;

			default:
				msg = getResources().getString(R.string.errParse);
				break;
		};
		
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onResume(){
	    super.onResume();
	}
}

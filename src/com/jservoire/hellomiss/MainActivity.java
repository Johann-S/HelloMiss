package com.jservoire.hellomiss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jservoire.tools.ListHello;
import com.jservoire.tools.PaginateHello;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends SherlockFragmentActivity
{
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			selectItem(position);
		}
	}
	private ImageView image;
	private MainActivity context;
	private ProgressBar loader;
	private Intent imgService;
	private Bitmap imageLoaded;
	private String prefixFile;
	private PaginateHello paginator;
	private static boolean imageSave;
	private Crouton crtLoading;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] listSite;

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(final Context ctx, final Intent intent) 
		{
			if ( crtLoading != null ) {
				Crouton.hide(crtLoading);
			}

			loader.setVisibility(View.INVISIBLE);			  
			prefixFile = ( intent != null && intent.getExtras() != null ) ? intent.getStringExtra("prefix") : "hMrs";

			// Get daily demoiselle page
			int tmpPage = intent.getIntExtra("page",0);
			if ( tmpPage > 0 ) {
				paginator.setPage(tmpPage);
			}

			Bitmap[] tabBitmap = (Bitmap[]) intent.getParcelableArrayExtra("img");
			imageLoaded = tabBitmap[0];
			image.setImageBitmap(imageLoaded);
		}
	};

	private BroadcastReceiver receiverErr = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(final Context context, final Intent intent) 
		{
			Crouton.cancelAllCroutons();
			loader.setVisibility(View.INVISIBLE);
			displayError(intent.getIntExtra("idErr",0));
		}
	};

	private OnClickListener NextListener = new OnClickListener() 
	{	
		@Override
		public void onClick(final View v) 
		{			
			image.setImageBitmap(null);
			String urlHello = paginator.nextImage(prefixFile);
			startImageService(urlHello);
		}
	};

	private OnClickListener PrevListener = new OnClickListener() 
	{	
		@Override
		public void onClick(final View v) 
		{
			image.setImageBitmap(null);
			String urlHello = paginator.prevImage(prefixFile);
			startImageService(urlHello);
		}
	};

	public void displayAboutModal()
	{
		View modal = getLayoutInflater().inflate(R.layout.about_modal, null);
		TextView myAbout = (TextView)modal.findViewById(R.id.AboutTxtView);
		myAbout.setText(Html.fromHtml(getString(R.string.aboutText)));		
		Dialog dial = new Dialog(this);
		dial.setContentView(modal);
		dial.setTitle(getResources().getString(R.string.about));		
		dial.show();
	}

	public void displayError(final int idErr)
	{
		String msg = null;		
		switch (idErr) 
		{
		case 1:
			msg = getResources().getString(R.string.errParse);
			break;

		case 2:
			msg = getResources().getString(R.string.errInternet);
			break;

		default:
			msg = getResources().getString(R.string.errParse);
			break;
		};

		Crouton.makeText(context, msg, Style.ALERT).show();
	}

	private String getFileName()
	{
		String fileName = null;
		String cPage = paginator.getPage();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.FRENCH);

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageSave = false;
		context = this;
		image = (ImageView)findViewById(R.id.mainImage);
		loader = (ProgressBar)findViewById(R.id.loader);
		ImageButton nextBtn = (ImageButton)findViewById(R.id.imageButtonNext);
		nextBtn.setOnClickListener(NextListener);
		ImageButton prevBtn = (ImageButton)findViewById(R.id.imageButtonPrev);
		prevBtn.setOnClickListener(PrevListener);
		paginator = PaginateHello.getInstance(this);
		listSite = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ListSiteAdapter(MainActivity.this,listSite));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

			@Override
			public void onDrawerClosed(final View view) {
				super.onDrawerClosed(view);
				supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				super.onDrawerOpened(drawerView);
				supportInvalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);       
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		IntentFilter filter = new IntentFilter("downloadFinished");
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,filter);

		IntentFilter filterErr = new IntentFilter("errorService");
		LocalBroadcastManager.getInstance(this).registerReceiver(receiverErr,filterErr);

		if ( savedInstanceState != null && savedInstanceState.containsKey("currentMiss") ) 
		{
			prefixFile = savedInstanceState.getString("currentMiss");
			Bitmap[] tmpBitmap = (Bitmap[]) savedInstanceState.getParcelableArray("currentImg");
			if ( tmpBitmap[0] != null )
			{
				String titleActivity = ListHello.getHellosNameByPrefix(this).get(prefixFile);
				setTitle(titleActivity);
				setIconByPrefix(prefixFile);
				imageLoaded = tmpBitmap[0];
				image.setImageBitmap(imageLoaded);
			}
		}
		else 
		{
			Intent intActivity = getIntent();
			String urlHello = null;
			prefixFile = ( intActivity != null && intActivity.getExtras() != null ) ? intActivity.getStringExtra("prefix") : "hMrs";
			if ( prefixFile == null ) {
                prefixFile = "hMrs";
            }
			
			urlHello = ListHello.getListHelloByPrefix(this).get(prefixFile);
			String titleActivity = ListHello.getHellosNameByPrefix(this).get(prefixFile);
			setTitle(titleActivity);
			setIconByPrefix(prefixFile);
			startImageService(urlHello);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Crouton.cancelAllCroutons();
		if ( imageSave ) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		}
	}

	@Override
	public boolean onOptionsItemSelected(final com.actionbarsherlock.view.MenuItem item) 
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
			saveImage();
			return true;
		case R.id.idSetWall:
			this.setWallpaper();
			return true;
		case R.id.about:
			displayAboutModal();
			return true;
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onPrepareOptionsMenu(final com.actionbarsherlock.view.Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle savedInstanceState) 
	{
		super.onSaveInstanceState(savedInstanceState);
		if ( imageLoaded != null )
		{
			savedInstanceState.putString("currentMiss", prefixFile);
			savedInstanceState.putParcelableArray("currentImg",new Bitmap[] { imageLoaded });
		}
	}

	public void refreshImage()
	{   	
		String urlHello = ListHello.getListHelloByPrefix(this).get(prefixFile);    	
		if ( urlHello != null )
		{
			image.setImageBitmap(null);
			startImageService(urlHello);
		}
	}

	public void saveImage()
	{
		File savePath = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/");
		if ( !savePath.exists() && !savePath.isDirectory()) {
			savePath.mkdir();
		}

		if ( savePath != null )
		{
			String nameFile = getFileName();
			File newImg = new File(savePath.getAbsolutePath(),nameFile);

			try 
			{
				FileOutputStream f = new FileOutputStream(newImg);
				imageLoaded.compress(Bitmap.CompressFormat.JPEG, 85, f);
				Crouton.makeText(context, getResources().getString(R.string.imgSaved), Style.CONFIRM).show();
				imageSave = true;
			} 
			catch (FileNotFoundException e) {
				Log.e("Err FileNotFoundException",getResources().getString(R.string.errFileNotFound));
			}
		}
	}

	private void selectItem(final int position) 
	{
		mDrawerList.setItemChecked(position, true);
		String item = listSite[position];
		String url = ListHello.getListHello(this).get(item);

		if ( url != null )
		{
			setTitle(item);
			String prefix = ListHello.getHellosPrefixByName(this).get(item);
			setIconByPrefix(prefix);	
			if ( url != null && url.length() > 0 )
			{
				Intent intent = new Intent("selectedItem");
				LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
				startImageService(url);
			}
		}

		mDrawerLayout.closeDrawer(mDrawerList);
		image.setImageBitmap(null);
		paginator.setPageToNull();
	}

	private void setIconByPrefix(final String prefix)
	{
		int rIcon = 0;
		if ( prefix.equals("hMrs") ) {
			rIcon = R.drawable.bmme;
		}

		if ( prefix.equals("hMiss") ) {
			rIcon = R.drawable.bmlle;
		}

		if ( prefix.equals("hBmb") ) {
			rIcon = R.drawable.blab;
		}

		if ( prefix.equals("hBll") ) {
			rIcon = R.drawable.bmab;
		}

		if ( prefix.equals("hOdob") ) {
			rIcon = R.drawable.odob;
		}

		if ( prefix.equals("dMlle") ) {
			rIcon = R.drawable.ddmlle;
		}

		if ( rIcon == 0 ) {
			rIcon = R.drawable.ic_launcher;
		}

		getSupportActionBar().setIcon(rIcon);
	}

	public void setWallpaper()
	{
		WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

		try {
			myWallpaperManager.setBitmap(imageLoaded);
		} 
		catch (IOException e) {
			Log.e("Err Walpaper",getResources().getString(R.string.errSetWallpaper));
		}
	}

	public void showCroutonLoading()
	{
		crtLoading = Crouton.makeText(this,getResources().getString(R.string.load), Style.INFO)
				.setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build());
		crtLoading.show();
	}


	private void startImageService(final String url)
	{
		Crouton.cancelAllCroutons();
		imgService = new Intent(MainActivity.this, ImageService.class);
		imgService.putExtra("url", url);
		showCroutonLoading();
		loader.setVisibility(View.VISIBLE);
		startService(imgService);
	}
}
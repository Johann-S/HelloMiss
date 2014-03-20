package com.jservoire.hellomiss;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jservoire.exceptions.ParseException;
import com.jservoire.tools.WebParser;

public class ImageService extends Service 
{
	private class ImageTask extends AsyncTask<String, Void, Bitmap> 
	{
		@Override
		protected Bitmap doInBackground(final String... urls) {
			return downloadImage(urls[0]);
		}

		// Creates Bitmap from InputStream and returns it
		private Bitmap downloadImage(final String url) 
		{
			String href = parseHelloWebsite(url);  
			Bitmap bitmap = null;

			if ( href.length() > 0 && href != "" )
			{
				InputStream stream = null;
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;

				try
				{
					stream = getHttpConnection(href);
					if ( stream != null )
					{
						bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
						stream.close();
					}
					else {
						sendError(1);
					}
				} 
				catch (IOException e) {
					Log.e("Err stream close",getResources().getString(R.string.errStream));
				}
			}

			return bitmap;
		}

		private InputStream getHttpConnection(final String urlString)
		{
			InputStream stream = null;
			URL url;
			try 
			{
				url = new URL(urlString);
				URLConnection connection = url.openConnection();

				try 
				{
					HttpURLConnection httpConnection = (HttpURLConnection) connection;
					httpConnection.setRequestMethod("GET");
					httpConnection.connect();

					if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						stream = httpConnection.getInputStream();
					}
				} 
				catch (Exception e) {
					Log.e("Err HttpURLConnection",getResources().getString(R.string.errHttpConnection));
				}
			} 
			catch (MalformedURLException e) {
				Log.e("Err MalformedURLException",getResources().getString(R.string.errMalformedUrl));
			} 
			catch (IOException e) {
				Log.e("Err URLConnection",getResources().getString(R.string.errURLConnection));
			}

			return stream;
		}

		@Override
		protected void onPostExecute(final Bitmap result) {
			if ( result == null ) {
				sendError(1);
			}
			else {
				sendResult(result);
			}
		}

		private String parseHelloWebsite(final String urlSite)
		{
			WebParser parser = new WebParser(getBaseContext());
			String imageUrl = null;
			try 
			{
				imageUrl = parser.parseWebsite(urlSite);
				prefixFile = parser.getPrefixFile();
				page = parser.getPage();
			} 
			catch (ParseException e) 
			{
				sendError(1);
				Log.e("ParseException",e.getMessage());
			}

			return imageUrl;
		}
	}

	private String prefixFile;
	private ImageTask task;
	private int page;

	private boolean isNetworkAvailable() 
	{
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) 
	{
		if ( intent != null && intent.getExtras() != null ) 
		{
			if ( isNetworkAvailable() )
			{
				String url = intent.getStringExtra("url");      	        	
				task = new ImageTask();
				task.execute(new String[] { url });	
			}
			else {
				sendError(2);
			}
		}

		return START_NOT_STICKY;
	}

	public void sendError(final int idErr)
	{
		if ( task != null ) {
			task.cancel(true);
		}

		Intent intent = new Intent("errorService");
		intent.putExtra("idError", idErr);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		stopSelf();    	
	}

	public void sendResult(final Bitmap img)
	{
		task.cancel(true);
		Intent intent = new Intent("downloadFinished");
		intent.putExtra("prefix", prefixFile);
		intent.putExtra("page", page);
		intent.putExtra("img", new Bitmap[]{img});
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		stopSelf();
	}
}

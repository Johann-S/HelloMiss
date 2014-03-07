package com.jservoire.hellomiss;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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

			if ( href.length() > 0 && !href.isEmpty() )
			{
				InputStream stream = null;
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;

				try
				{
					stream = getHttpConnection(href);
					bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
					stream.close();
					sendResult(bitmap);
				} 
				catch (IOException e) {
					Log.e("Err stream close",e.getMessage());
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
					Log.e("Err HttpURLConnection",e.getMessage());
				}
			} 
			catch (MalformedURLException e) {
				Log.e("Err MalformedURLException",e.getMessage());
			} 
			catch (IOException e) {
				Log.e("Err URLConnection",e.getMessage());
			}

			return stream;
		}

		@Override
		protected void onPostExecute(final Bitmap result) {
		}

		private String parseHelloWebsite(final String urlSite)
		{
			String imgURL = "";
			Elements contents = null;

			if ( urlSite.indexOf("bonjourmadame") != -1 )
			{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("photo");
					if ( contents.first() != null )
					{
						Element imgElem = contents.first().getElementsByTag("img").first();
						imgURL = imgElem.attr("src");
						prefixFile = "hMrs";
					}
					else {
						sendError((byte)1);
					}
				} 
				catch (IOException e) 
				{
					Log.e("Err Parsage BonjourMadame",e.getMessage());
					sendError((byte)1);
				}
			}

			if ( urlSite.indexOf("bonjourmademoiselle") != -1 )
			{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("photo");
					if ( contents.first() != null )
					{
						Element imgElem = contents.first().getElementsByTag("img").first();
						imgURL = imgElem.attr("src");
						prefixFile = "hMiss";
					}
					else {
						sendError((byte)1);
					}
				} 
				catch (IOException e) 
				{
					Log.e("Err Parsage BonjourMademoiselle",e.getMessage());
					sendError((byte)1);
				}       		
			}

			if ( urlSite.indexOf("bonjourlabombe") != -1 )
			{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("photo");
					if ( contents.first() != null )
					{
						Element imgElem = contents.first().getElementsByTag("img").first();
						imgURL = imgElem.attr("src");
						prefixFile = "hBmb";
					}
					else {
						sendError((byte)1);
					}
				} 
				catch (IOException e) 
				{
					Log.e("Err Parsage BonjourLaBombe",e.getMessage());
					sendError((byte)1);
				}       		
			}

			if ( urlSite.indexOf("bonjourmabelle") != -1 )
			{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("photo");
					if ( contents.first() != null )
					{
						Element imgElem = contents.first().getElementsByTag("img").first();
						imgURL = imgElem.attr("src");
						prefixFile = "hBll";
					}
					else {
						sendError((byte)1);
					}
				} 
				catch (IOException e) 
				{
					Log.e("Err Parsage BonjourMaBelle",e.getMessage());
					sendError((byte)1);
				}       		
			}

			if ( urlSite.indexOf("1day1babe") != -1 )
			{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("photo");
					if ( contents.first() != null )
					{
						Element imgElem = contents.first().getElementsByTag("a").first();
						imgURL = imgElem.attr("href");
						prefixFile = "hOdob";
					}
					else {
						sendError((byte)1);
					}
				} 
				catch (IOException e) 
				{
					Log.e("Err Parsage 1day1babe",e.getMessage());
					sendError((byte)1);
				}       		
			}

			return imgURL;
		}
	}

	private String prefixFile;
	private ImageTask task;

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) 
	{
		if ( intent != null && intent.getExtras() != null ) 
		{
			String url = intent.getStringExtra("url");      	        	
			task = new ImageTask();
			task.execute(new String[] { url });	
		}

		return START_NOT_STICKY;
	}

	public void sendError(final byte idErr)
	{
		task.cancel(true);
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
		intent.putExtra("img", new Bitmap[]{img});
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		stopSelf();
	}
}

package com.jservoire.hellomiss;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

public class ImageService extends Service 
{
	private String prefixFile;
	private ImageTask task;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
    	if ( intent != null && intent.getExtras() != null ) 
    	{
        	String url = intent.getStringExtra("url");      	        	
    		task = new ImageTask();
    		task.execute(new String[] { url });	
    	}
	
        return START_NOT_STICKY;
    }
    
    public void sendResult(Bitmap img)
    {
    	task.cancel(true);
    	Bitmap[] tabImg = new Bitmap[1];
    	tabImg[0] = img;
    	
    	Intent intent = new Intent("downloadFinished");
    	intent.putExtra("prefix", prefixFile);
    	intent.putExtra("img", tabImg);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		stopSelf();
    }
    
    public void sendError(byte idErr)
    {
    	task.cancel(true);
    	Intent intent = new Intent("errorService");
    	intent.putExtra("idError", idErr);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		stopSelf();    	
    }
    
    private class ImageTask extends AsyncTask<String, Void, Bitmap> 
    {
        @Override
        protected Bitmap doInBackground(String... urls) 
        {
            try {
				return downloadImage(urls[0]);
			} catch (IOException e) 
			{
				e.printStackTrace();
				return null;
			}
        }
 
        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
        }
 
        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) throws IOException 
        {
			String href = this.parseHelloWebsite(url);  
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
                catch (IOException e1) {
                    e1.printStackTrace();
                }
    		}
            
			return bitmap;
        }
        
        private String parseHelloWebsite(String urlSite)
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
				catch (IOException e) {
					e.printStackTrace();
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
				catch (IOException e) {
					e.printStackTrace();
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
				catch (IOException e) {
					e.printStackTrace();
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
				catch (IOException e) {
					e.printStackTrace();
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
				catch (IOException e) {
					e.printStackTrace();
				}       		
        	}
        	
        	return imgURL;
        }
 
        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString) throws IOException 
        {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
 
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
 
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}

package com.jservoire.hellomiss;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ImageService extends Service 
{
	private String prefixFile;
	
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
    		ImageTask task = new ImageTask();
    		task.execute(new String[] { url });	
    	}
	
        return START_NOT_STICKY;
    }
    
    public void sendResult()
    {
    	Intent intent = new Intent("downloadFinished");
    	intent.putExtra("prefix", prefixFile);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
	                
	 		       try 
			       {
			            File root = new File(Environment.getExternalStorageDirectory()+"/HelloMiss/");
			            if ( !root.exists() && !root.isDirectory()) {
			            	root.mkdir();
			            }
			            
			            File file = new File(root.getAbsolutePath(),"newMiss.jpg");
			            try 
			            {
				            FileOutputStream f = new FileOutputStream(file);
				            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, f);
				            f.close();
				            sendResult();
			            }
			            catch(IOException ex) {
			            }
			        } 
			       catch (Exception e) {
			        	Log.d("Downloader", e.getMessage());
			        }
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
        	
        	if ( urlSite.equals(getResources().getString(R.string.urlBjrMadame)) )
        	{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("photo-url");
					imgURL = contents.first().attr("href");
					prefixFile = "hMrs";
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
        	if ( urlSite.equals(getResources().getString(R.string.urlBjrMademoiselle)) )
        	{
				try 
				{
					contents = Jsoup.connect(urlSite).get().body().getElementsByClass("highres");
					imgURL = contents.first().attr("src");
					prefixFile = "hMiss";
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

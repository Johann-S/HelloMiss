package com.jservoire.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.jservoire.exceptions.ParseException;
import com.jservoire.hellomiss.R;

public class WebParser 
{
	private Context ctx;
	private String prefixFile;
	private int page;

	public WebParser(final Context _ctx)
	{
		ctx = _ctx;
		prefixFile = null;
		page = 0;
	}

	private InputStream getInputStream(final String urlString)
	{
		InputStream stream = null;
		try 
		{
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try 
			{
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();
				stream = httpConnection.getInputStream();
			} 
			catch (Exception e) 
			{
				String msg = ( e.getMessage() != null ) ? e.getMessage() : ctx.getResources().getString(R.string.errHttpConnection);
				Log.e("Err HttpURLConnection",msg);
			}
		} 
		catch (MalformedURLException e) 
		{
			String msg = ( e.getMessage() != null ) ? e.getMessage() : ctx.getResources().getString(R.string.errMalformedUrl);
			Log.e("Err MalformedURLException",msg);
		} 
		catch (IOException e) 
		{
			String msg = ( e.getMessage() != null ) ? e.getMessage() : ctx.getResources().getString(R.string.errURLConnection);
			Log.e("Err URLConnection",msg);
		}

		return stream;
	}

	public int getPage() {
		return page;
	}

	public String getPrefixFile() {
		return prefixFile;
	}

	private String parseDailyDemoiselle(final String url) throws ParseException
	{
		Element contents = null;
		String imgURL = "";
		try 
		{
			InputStream stream = getInputStream(url);
			if ( stream != null )
			{
				contents = Jsoup.parse(stream,"UTF-8",url).body().getElementById("photo1");
				if ( contents != null )
				{
					imgURL = url+contents.attr("src");
					String[] hrefExplode = contents.attr("src").split("/");		
					page = Integer.parseInt(hrefExplode[hrefExplode.length-2]);
					prefixFile = "dMlle";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errDDmlle));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errURLConnection));
			}
		} 
		catch (IOException e) 
		{
			if ( url.indexOf(".jpg") != -1 )
			{
				imgURL = url;
				prefixFile = "dMlle";
			}
			else
			{
				Log.e("ErrParseBonjourSelfShot",ctx.getResources().getString(R.string.errDDmlle));
				throw new ParseException(ctx.getResources().getString(R.string.errDDmlle));
			}
		} 


		return imgURL;
	}

	private String parseLaBombe(final String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";	
		try 
		{
			InputStream stream = getInputStream(url);
			if ( stream != null )
			{
				contents = Jsoup.parse(stream,"UTF-8",url).body().getElementsByClass("photo");
				if ( contents.first() != null )
				{
					Element imgElem = contents.first().getElementsByTag("img").first();
					imgURL = imgElem.attr("src");
					prefixFile = "hBmb";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errBjBombe));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errURLConnection));
			}
		} 
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourLaBombe",ctx.getResources().getString(R.string.errBjBombe));
			throw new ParseException(ctx.getResources().getString(R.string.errBjBombe));
		}

		return imgURL;
	}

	private String parseMaBelle(final String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			InputStream stream = getInputStream(url);
			if ( stream != null )
			{
				contents = Jsoup.parse(stream,"UTF-8",url).body().getElementsByClass("photo");
				if ( contents.first() != null )
				{
					Element imgElem = contents.first().getElementsByTag("img").first();
					imgURL = imgElem.attr("src");
					prefixFile = "hBll";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errBjBelle));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errURLConnection));
			}
		} 
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourMaBelle",ctx.getResources().getString(R.string.errBjBelle));
			throw new ParseException(ctx.getResources().getString(R.string.errBjBelle));
		}

		return imgURL;
	}

	private String parseMadame(final String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			InputStream stream = getInputStream(url);
			if ( stream != null )
			{
				contents = Jsoup.parse(stream,"UTF-8",url).body().getElementsByClass("photo");
				if ( contents.first() != null )
				{
					Element imgElem = contents.first().getElementsByTag("img").first();
					imgURL = imgElem.attr("src");
					prefixFile = "hMrs";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errBjMme));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errURLConnection));
			}
		} 
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourMadame",ctx.getResources().getString(R.string.errBjMme));
			throw new ParseException(ctx.getResources().getString(R.string.errBjMme));
		}

		return imgURL;
	}

	private String parseMaDemoiselle(final String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			InputStream stream = getInputStream(url);
			if ( stream != null )
			{
				contents = Jsoup.parse(stream,"UTF-8",url).body().getElementsByClass("photo");
				if ( contents.first() != null )
				{
					Element imgElem = contents.first().getElementsByTag("img").first();
					imgURL = imgElem.attr("src");
					prefixFile = "hMiss";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errBjMlle));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errURLConnection));
			}
		} 
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourMademoiselle",ctx.getResources().getString(R.string.errBjMlle));
			throw new ParseException(ctx.getResources().getString(R.string.errBjMlle));
		}

		return imgURL;
	}

	private String parseOneBabe(final String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			InputStream stream = getInputStream(url);
			if ( stream != null )
			{
				contents = Jsoup.parse(stream,"UTF-8",url).body().getElementsByClass("photo");
				if ( contents.first() != null )
				{
					Element imgElem = contents.first().getElementsByTag("a").first();
					imgURL = imgElem.attr("href");
					prefixFile = "hOdob";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errOdob));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errURLConnection));
			}
		} 
		catch (IOException e) 
		{
			Log.e("ErrParse1day1babe",ctx.getResources().getString(R.string.errOdob));
			throw new ParseException(ctx.getResources().getString(R.string.errOdob));
		}   		
		return imgURL;
	}

	public String parseWebsite(final String url) throws ParseException
	{
		String imageUrl = "";
		if ( url.indexOf("bonjourmadame") != -1 ) {
			imageUrl = parseMadame(url);
		}

		if ( url.indexOf("bonjourmademoiselle") != -1 ) {
			imageUrl = parseMaDemoiselle(url);
		}

		if ( url.indexOf("bonjourlabombe") != -1 ) {
			imageUrl = parseLaBombe(url);
		}

		if ( url.indexOf("bonjourmabelle") != -1 ) {
			imageUrl = parseMaBelle(url);
		}

		if ( url.indexOf("1day1babe") != -1 ) {
			imageUrl = parseOneBabe(url);
		}

		if ( url.indexOf("daily-demoiselle") != -1 ) {
			imageUrl = parseDailyDemoiselle(url);
		}

		return imageUrl;
	}
}

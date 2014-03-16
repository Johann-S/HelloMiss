package com.jservoire.tools;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.jservoire.hellomiss.R;
import com.jservoire.exceptions.ParseException;

public class WebParser 
{
	private Context ctx;
	private String prefixFile;
	private int page;
	
	public WebParser(Context _ctx)
	{
		ctx = _ctx;
		prefixFile = null;
		page = 0;
	}
	
	public String parseWebsite(String url) throws ParseException
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
		
		if ( url.indexOf("bonjour-selfshot") != -1 ) {
			imageUrl = parseSelfShot(url);
		}
		
		if ( url.indexOf("daily-demoiselle") != -1 ) {
			imageUrl = parseDailyDemoiselle(url);
		}
		
		return imageUrl;
	}
	
	public String getPrefixFile() {
		return prefixFile;
	}
	
	public int getPage() {
		return page;
	}
	
	private String parseMadame(String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementsByClass("photo");
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
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourMadame",ctx.getResources().getString(R.string.errBjMme));
			throw new ParseException(ctx.getResources().getString(R.string.errBjMme));
		}
		
		return imgURL;
	}
	
	private String parseMaDemoiselle(String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementsByClass("photo");
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
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourMademoiselle",ctx.getResources().getString(R.string.errBjMlle));
			throw new ParseException(ctx.getResources().getString(R.string.errBjMlle));
		}
		
		return imgURL;
	}
	
	private String parseLaBombe(String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";	
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementsByClass("photo");
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
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourLaBombe",ctx.getResources().getString(R.string.errBjBombe));
			throw new ParseException(ctx.getResources().getString(R.string.errBjBombe));
		}
		
		return imgURL;
	}
	
	private String parseMaBelle(String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementsByClass("photo");
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
		catch (IOException e) 
		{
			Log.e("ErrParseBonjourMaBelle",ctx.getResources().getString(R.string.errBjBelle));
			throw new ParseException(ctx.getResources().getString(R.string.errBjBelle));
		}
		
		return imgURL;
	}
	
	private String parseOneBabe(String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementsByClass("photo");
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
		catch (IOException e) 
		{
			Log.e("ErrParse1day1babe",ctx.getResources().getString(R.string.errOdob));
			throw new ParseException(ctx.getResources().getString(R.string.errOdob));
		}   		
		return imgURL;
	}
	
	private String parseSelfShot(String url) throws ParseException
	{
		Elements contents = null;
		String imgURL = "";
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementById("Posts").getElementsByClass("PhotoPost");
			if ( contents.first() != null )
			{
				Element divPhoto = contents.first();
				divPhoto = divPhoto.getElementsByClass("PhotoWrapper").first();
				if ( divPhoto != null )
				{
					Element imgElem = divPhoto.getElementsByTag("img").first();
					imgURL = imgElem.attr("src");
					prefixFile = "helSShot";
				}
				else {
					throw new ParseException(ctx.getResources().getString(R.string.errSelfShot));
				}
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errSelfShot));
			}
		} 
		catch (IOException e) {
			Log.e("ErrParseBonjourSelfShot",ctx.getResources().getString(R.string.errSelfShot));
			throw new ParseException(ctx.getResources().getString(R.string.errSelfShot));
		} 
		
		return imgURL;
	}
	
	private String parseDailyDemoiselle(String url) throws ParseException
	{
		Element contents = null;
		String imgURL = "";
		try 
		{
			contents = Jsoup.connect(url).get().body().getElementById("photo1");
			if ( contents != null )
			{
				imgURL = url+contents.attr("src");
				String[] hrefExplode = contents.attr("src").split("/");		
				page = Integer.parseInt(hrefExplode[hrefExplode.length-2]);
				prefixFile = "dMlle";
			}
			else {
				throw new ParseException(ctx.getResources().getString(R.string.errSelfShot));
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
				Log.e("ErrParseBonjourSelfShot",ctx.getResources().getString(R.string.errSelfShot));
				throw new ParseException(ctx.getResources().getString(R.string.errSelfShot));
			}
		} 
		
		
		return imgURL;
	}
}

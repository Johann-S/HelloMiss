package com.jservoire.tools;

import com.jservoire.hellomiss.R;

import android.content.Context;

public class PaginateHello 
{
	private static PaginateHello instance;
	private Context ctx;
	private static String page;
	
	private PaginateHello(Context _ctx) 
	{
		page = null;
		this.ctx = _ctx;
	}
	
	public static PaginateHello getInstance(Context _ctx)
	{
		if ( instance == null ) {
			instance = new PaginateHello(_ctx);
		}
		
		return instance;
	}
	
	public String nextImage(String prefix)
	{
		String url = ListHello.getListHelloByPrefix(ctx).get(prefix);
		return buildNextUrl(url);		
	}
	
	public String prevImage(String prefix)
	{
		String url = ListHello.getListHelloByPrefix(ctx).get(prefix);
		
		return url;
	}
	
	private String buildNextUrl(String url)
	{
		String pattern = "/";		
		if ( url.equals(ctx.getResources().getString(R.string.urlBjrMadame)) )
		{
			if ( page == null ) 
			{
				pattern = pattern+"page/2/";
				page = "2";
			}
			else 
			{
				int intPage = Integer.parseInt(page);
				intPage++;
				page = Integer.toString(intPage);
				pattern = pattern+"page/"+page+"/";
			}
		}
		
		url += pattern;
		return url;
	}
}

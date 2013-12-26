package com.jservoire.tools;

import java.util.LinkedHashMap;
import java.util.Map;
import android.content.Context;

public class ListHello 
{
	static public Map<String,String> getListHello(Context ctx)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.array_url);
		
		if ( keys.length == values.length )
		{
		    for ( int i = 0; i < keys.length; i++ ) {
		        map.put(keys[i], values[i]);
		    }			
			return map;
		}
		else {
			return null;
		}
	}
	
	static public Map<String,String> getListHelloByPrefix(Context ctx)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();		
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.array_url);
		
		if ( keys.length == values.length )
		{
		    for ( int i = 0; i < keys.length; i++ ) {
		        map.put(keys[i], values[i]);
		    }			
			return map;
		}
		else {
			return null;
		}
	}
	
	static public Map<String,String> getHellosNameByPrefix(Context ctx)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();		
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		
		if ( keys.length == values.length )
		{
		    for ( int i = 0; i < keys.length; i++ ) {
		        map.put(keys[i], values[i]);
		    }			
			return map;
		}
		else {
			return null;
		}
	}
}

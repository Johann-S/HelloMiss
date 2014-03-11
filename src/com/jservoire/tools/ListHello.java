package com.jservoire.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;

public class ListHello 
{
	static public Map<String,String> getListHello(Context ctx)
	{
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.array_url);
		return buildStringMap(keys, values);
	}
	
	static public Map<String,String> getListHelloByPrefix(Context ctx)
	{	
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.array_url);
		return buildStringMap(keys, values);
	}
	
	static public Map<String,String> getHellosNameByPrefix(Context ctx)
	{	
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		return buildStringMap(keys, values);
	}
	
	static public Map<String,String> getHellosPrefixByName(Context ctx)
	{
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		return buildStringMap(keys, values);
	}
	
	static private Map<String,String> buildStringMap(String[] keys,String[] values)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();
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

package com.jservoire.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;

public class ListHello
{
	static private Map<String,String> buildStringMap(final String[] keys,final String[] values)
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

	static public Map<String,String> getHellosNameByPrefix(final Context ctx)
	{	
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		return buildStringMap(keys, values);
	}

	static public Map<String,String> getHellosPrefixByName(final Context ctx)
	{
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		return buildStringMap(keys, values);
	}

	static public Map<String,String> getListHello(final Context ctx)
	{
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.nav_drawer_items);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.array_url);
		return buildStringMap(keys, values);
	}

	static public Map<String,String> getListHelloByPrefix(final Context ctx)
	{	
		String[] keys = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.prefix_files);
		String[] values = ctx.getResources().getStringArray(com.jservoire.hellomiss.R.array.array_url);
		return buildStringMap(keys, values);
	}
}

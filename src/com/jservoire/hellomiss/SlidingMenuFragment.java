package com.jservoire.hellomiss;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SlidingMenuFragment extends ListFragment  
{
	private Activity mainActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mainActivity = this.getActivity();
		return inflater.inflate(R.layout.slidingmenufragment, container, false);
	}
	
    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) 
    {
		String item = ((TextView)v).getText().toString();
		String url = "";
		Intent imgService = new Intent(mainActivity,ImageService.class);
		
		String bjrMadame = getResources().getString(R.string.urlBjrMadame);
		String bjrMademoiselle =  getResources().getString(R.string.urlBjrMademoiselle);
		String bjrBombe =  getResources().getString(R.string.urlBjrBombes);
		String[] tabItem = getResources().getStringArray(R.array.nav_drawer_items);
		
		if ( item.equals(tabItem[0]) ) {
			url = bjrMadame;
		}
		
		if ( item.equals(tabItem[1]) ) {
			url = bjrMademoiselle;
		}
		
		if ( item.equals(tabItem[2]) ) {
			url = bjrBombe;
		}
		
		if ( !url.isEmpty() )
		{
			imgService.putExtra("url",url);
			mainActivity.startService(imgService);
		}
    }
}

package com.jservoire.hellomiss;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jservoire.tools.*;

public class SlidingMenuFragment extends ListFragment  
{
	private MainActivity mainActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mainActivity = (MainActivity)this.getActivity();
		return inflater.inflate(R.layout.slidingmenufragment, container, false);
	}
	
    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) 
    {
		String item = ((TextView)v).getText().toString();
		String url = ListHello.getListHello(mainActivity).get(item);
		
		if ( url != null )
		{
			mainActivity.setTitle(item);
			String prefix = ListHello.getHellosPrefixByName(mainActivity).get(item);
			mainActivity.setIconByPrefix(prefix);
			Intent imgService = new Intent(mainActivity,ImageService.class);	
			if ( !url.isEmpty() )
			{
		    	Intent intent = new Intent("selectedItem");
				LocalBroadcastManager.getInstance(mainActivity).sendBroadcast(intent);
				
				imgService.putExtra("url",url);
				mainActivity.startService(imgService);
			}
		}
    }
}

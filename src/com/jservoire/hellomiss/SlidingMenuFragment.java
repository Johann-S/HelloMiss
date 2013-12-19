package com.jservoire.hellomiss;

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

import com.jservoire.tools.*;

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
		String url = ListHello.getListHello(mainActivity).get(item);
		
		if ( url != null )
		{
			Intent imgService = new Intent(mainActivity,ImageService.class);	
			if ( !url.isEmpty() )
			{
				imgService.putExtra("url",url);
				mainActivity.startService(imgService);
			}
		}
    }
}

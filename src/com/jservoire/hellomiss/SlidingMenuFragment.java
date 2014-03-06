package com.jservoire.hellomiss;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jservoire.tools.ListHello;

public class SlidingMenuFragment extends ListFragment  
{
	private MainActivity mainActivity;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) 
	{
		mainActivity = (MainActivity)getActivity();
		return inflater.inflate(R.layout.slidingmenufragment, container, false);
	}

	@Override
	public void onListItemClick(final ListView lv, final View v, final int position, final long id) 
	{
		String item = ((TextView)v).getText().toString();
		String url = ListHello.getListHello(mainActivity).get(item);

		if ( url != null )
		{
			mainActivity.setTitle(item);
			String prefix = ListHello.getHellosPrefixByName(mainActivity).get(item);
			mainActivity.setIconByPrefix(prefix);	
			if ( !url.isEmpty() )
			{
				Intent intent = new Intent("selectedItem");
				LocalBroadcastManager.getInstance(mainActivity).sendBroadcast(intent);
				mainActivity.startImageService(url);
			}
		}
	}
}

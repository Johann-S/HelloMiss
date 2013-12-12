package com.jservoire.hellomiss;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SlidingMenuFragment extends ListFragment  
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slidingmenufragment, container, false);
	}
	
    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) 
    {
		String item = ((TextView)v).getText().toString();
		Log.d("info",item); 
    }
}

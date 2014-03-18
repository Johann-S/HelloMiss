package com.jservoire.hellomiss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jservoire.tools.ListHello;

public class ListSiteAdapter extends BaseAdapter
{
	private MainActivity mActivity;
	private String[] mListWeb;
	private LayoutInflater mInflater;

	public ListSiteAdapter(final MainActivity _activity,final String[] _aSite)
	{
		mActivity = _activity;
		mListWeb = _aSite;
		mInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		return mListWeb.length;
	}

	private Bitmap getImageSite(final String siteName)
	{
		String prefix = ListHello.getHellosPrefixByName(mActivity).get(siteName);
		Bitmap result = null;
		if ( prefix.equals("hMrs") ) {
			result = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.bmme);
		}

		if ( prefix.equals("hMiss") ) {
			result = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.bmlle);
		}

		if ( prefix.equals("hBmb") ) {
			result = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.blab);
		}

		if ( prefix.equals("hBll") ) {
			result = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.bmab);
		}

		if ( prefix.equals("hOdob") ) {
			result = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.odob);
		}

		return result;
	}

	@Override
	public Object getItem(final int position) {
		return mListWeb[position];
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LinearLayout cellItem;

		if (convertView == null) {
			cellItem = (LinearLayout) mInflater.inflate(R.layout.cell_hello_site, parent, false);
		}
		else {
			cellItem = (LinearLayout) convertView;
		}

		TextView nameSite = (TextView)cellItem.findViewById(R.id.nSiteTextView);
		nameSite.setText(mListWeb[position]);

		ImageView imageSite = (ImageView)cellItem.findViewById(R.id.siteImageView);
		imageSite.setImageBitmap(getImageSite(mListWeb[position]));

		return cellItem;
	}
}

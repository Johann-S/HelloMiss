package com.jservoire.tools;

import java.util.Calendar;
import com.jservoire.hellomiss.R;
import android.content.Context;

public class PaginateHello 
{
	private static PaginateHello instance;
	private Context ctx;
	private String page;
	private String currentURL;
	
	private PaginateHello(Context _ctx) 
	{
		page = null;
		currentURL = null;
		this.ctx = _ctx;
	}
	
	public static PaginateHello getInstance(Context _ctx)
	{
		if ( instance == null ) {
			instance = new PaginateHello(_ctx);
		}
		
		return instance;
	}
	
	public String getPage() {
		return this.page;
	}
	
	public void setPageToNull() {
		this.page = null;
		this.currentURL = null;
	}
	
	public String getCurrentURL() {
		return currentURL;
	}
	
	public String nextImage(String prefix)
	{
		String url = ListHello.getListHelloByPrefix(ctx).get(prefix);
		return buildNextUrl(url);		
	}
	
	public String prevImage(String prefix)
	{
		String url = ListHello.getListHelloByPrefix(ctx).get(prefix);		
		return buildPrevUrl(url);
	}
	
	private String buildNextUrl(String url)
	{
		String pattern = "/";		
		if ( url.equals(ctx.getResources().getString(R.string.urlBjrMadame)) || 
			url.equals(ctx.getResources().getString(R.string.urlBjrMademoiselle)) ||
			url.equals(ctx.getResources().getString(R.string.urlBjrBelle)) || 
			url.equals(ctx.getResources().getString(R.string.urlBjrBombes)) ||
			url.equals(ctx.getResources().getString(R.string.urlSShot)))
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
		
		if ( url.equals(ctx.getResources().getString(R.string.urlODOB)) )
		{
			Calendar cal = Calendar.getInstance();			
			if ( page == null )
			{
				cal.add(Calendar.DAY_OF_MONTH, -1);
				page = "1";
			}
			else
			{
				int intPage = Integer.parseInt(page);
				intPage++;
				page = Integer.toString(intPage);
				intPage *= -1;			
				cal.add(Calendar.DAY_OF_MONTH, intPage);			
			}
			
			int intNumDay = this.getNbDay(cal.get(Calendar.DAY_OF_WEEK));
		    int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
		    int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		    int intWeeks = (ordinalDay - weekDay + 10) / 7;	
		    intWeeks = ( intNumDay < 7 ) ? intWeeks-1:intWeeks-2;
			
			String nbWeeks = Integer.toString(intWeeks);
			if ( intWeeks < 10 ) {
				nbWeeks = "0"+nbWeeks;
			}
			
			String numDay = Integer.toString(intNumDay);
			String numYear = Integer.toString(cal.get(Calendar.YEAR));
			pattern = pattern+numYear+"/"+nbWeeks+"/"+numDay+"/";
		}
		
		url += pattern;
		currentURL = url;
		return url;
	}
	
	private String buildPrevUrl(String url)
	{
		String pattern = "/";		
		if ( url.equals(ctx.getResources().getString(R.string.urlBjrMadame)) || 
			url.equals(ctx.getResources().getString(R.string.urlBjrMademoiselle)) ||
			url.equals(ctx.getResources().getString(R.string.urlBjrBelle)) || 
			url.equals(ctx.getResources().getString(R.string.urlBjrBombes)) ||
			url.equals(ctx.getResources().getString(R.string.urlSShot)))
		{
			if ( page != null )  
			{
				int intPage = Integer.parseInt(page);
				intPage--;
				
				if ( intPage == 0 )
				{
					this.page = null;
					pattern = "/";
				}
				else
				{
					this.page = Integer.toString(intPage);
					pattern = pattern+"page/"+page+"/";
				}
			}
		}
		
		if ( url.equals(ctx.getResources().getString(R.string.urlODOB)) )
		{
			if ( page != null )
			{
				Calendar cal = Calendar.getInstance();
				int intPage = Integer.parseInt(page);
				intPage--;
				page = Integer.toString(intPage);
				intPage *= -1;
				
				cal.add(Calendar.DAY_OF_MONTH, intPage);
				int intNumDay = this.getNbDay(cal.get(Calendar.DAY_OF_WEEK));
				
			    int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
			    int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			    int intWeeks = (ordinalDay - weekDay + 10) / 7;	
				intWeeks--;
				
				String nbWeeks = Integer.toString(intWeeks);
				if ( intWeeks < 10 ) {
					nbWeeks = "0"+nbWeeks;
				}

				String numDay = Integer.toString(intNumDay);
				String numYear = Integer.toString(cal.get(Calendar.YEAR));
				pattern = pattern+numYear+"/"+nbWeeks+"/"+numDay+"/";
			}
		}
		
		url += pattern;
		currentURL = url;
		return url;
	}
	
	private int getNbDay(int numD)
	{	
		switch (numD) 
		{
			case Calendar.MONDAY:
				numD = 1;
				break;
				
			case Calendar.TUESDAY:
				numD = 2;
				break;
				
			case Calendar.WEDNESDAY:
				numD = 3;
				break;
			
			case Calendar.THURSDAY:
				numD = 4;
				break;
			
			case Calendar.FRIDAY:
				numD = 5;
				break;
			
			case Calendar.SATURDAY:
				numD = 6;
				break;
				
			case Calendar.SUNDAY:
				numD = 7;
				break;
				
			default:
				break;
		}
		
		return numD;
	}
}

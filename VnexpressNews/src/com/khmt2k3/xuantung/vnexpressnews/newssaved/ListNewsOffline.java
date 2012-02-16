package com.khmt2k3.xuantung.vnexpressnews.newssaved;

import java.util.ArrayList;

public class ListNewsOffline 
{
	public ArrayList<NewsOffline>listNews=new ArrayList<NewsOffline>();
	
	public ListNewsOffline(ArrayList<NewsOffline>listNews) 
	{
		this.listNews=listNews;
	}
	
	public ListNewsOffline() 
	{
	}
	
	public int getSize()
	{
		if(this.listNews!=null)
		{
			return this.listNews.size();
		}
		return 0;
	}
	
	public void addItem(NewsOffline newsOffline)
	{
		this.listNews.add(newsOffline);
	}
	
	public NewsOffline getItem(int position)
	{
		return this.listNews.get(position);
	}

}

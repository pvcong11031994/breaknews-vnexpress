package com.khmt2k3.xuantung.vnexpressnews;

import java.util.ArrayList;

public class ListNews 
{
	private ArrayList<News> listNews=new ArrayList<News>();
	
	public ListNews(ArrayList<News>listNews) 
	{
		this.listNews=listNews;
	}
	
	public int getSize()
	{
		if(this.listNews!=null)
		{
			return this.listNews.size();
		}
		return 0;
	}
	
	public void addItem(News news)
	{
		this.listNews.add(news);
	}
	
	public News getItem(int position)
	{
		return this.listNews.get(position);
	}
}

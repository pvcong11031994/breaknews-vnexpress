package com.khmt2k3.xuantung.vnexpressnews;

import android.graphics.Bitmap;

public class News
{
	protected String title;
	protected String description;
	protected String pubDate;
	protected Bitmap image;
	protected String link;
	public boolean isRead=false;//kiem tra xem tin duoc doc chua?
	public boolean check=false;//kiem tra xem tin co' duoc chon, de lam gi  do' khong? xoa', hoac luu.
	
	public News(String title,String description,String pubDate, Bitmap image,String link)
	{
		this.title=title;
		this.description=description;
		this.pubDate=pubDate;
		this.image=image;
		this.link=link;		
	}
	
	public News() 
	{
	}
	
	public void setTitle(String title)
	{
		this.title=title;
	}
	
	public void setDescription(String description)
	{
		this.description=description;
	}
	
	public void setPubDate(String pubDate)
	{
		this.pubDate=pubDate;
	}
	
	public void setImage(Bitmap image)
	{
		this.image=image;
	}
	public void setLink(String link)
	{
		this.link=link;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	public String getDescription()
	{
		return this.description;				
	}
	
	public String getPubDate()
	{
		return this.pubDate;
	}
	
	public Bitmap getImage()
	{
		return this.image;
	}
	public String getLink()
	{
		return this.link;				
	}
}

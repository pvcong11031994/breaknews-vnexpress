package com.khmt2k3.xuantung.vnexpressnews;

public class Menu 
{
	private String title;
	private String description;
	private int imageId;
	private String linkRSS;
	
	public Menu(String title,String description,int imageId,String linkRSS) 
	{
		this.title=title;
		this.description=description;
		this.imageId=imageId;
		this.linkRSS=linkRSS;
	}
	
	public void setTitle(String title)
	{
		this.title=title;
	}
	
	public void setDescription(String description)
	{
		this.description=description;
	}
	
	public void setImageId(int imageId)
	{
		this.imageId=imageId;
	}
	
	public void setLinkRSS(String linkRSS)
	{
		this.linkRSS=linkRSS;
	}	
	
	
	public String getTitle()
	{
		return this.title;
	}
	public String getDescription()
	{
		return this.description;				
	}
	
	public int getImageId()
	{
		return this.imageId;
	}
	
	public String getLinkRSS()
	{
		return this.linkRSS;
	}
	
}

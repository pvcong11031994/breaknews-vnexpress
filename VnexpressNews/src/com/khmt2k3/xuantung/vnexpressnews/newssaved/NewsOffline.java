package com.khmt2k3.xuantung.vnexpressnews.newssaved;

import android.graphics.Bitmap;

import com.khmt2k3.xuantung.vnexpressnews.News;

public class NewsOffline extends News 
{
	private String id;
	private String content;
	private String linkImage;
	

	public NewsOffline(String title,String description,String pubDate, Bitmap image,String link,String id,String content) 
	{
		super(title, description, pubDate, image, link);
		this.id=id;
		this.content=content;
	}
	
	public NewsOffline(News news,String id,String content,String linkImage) 
	{
		this.title=news.getTitle();
		this.description=news.getDescription();
		this.pubDate=news.getPubDate();
		this.image=news.getImage();
		this.link=news.getLink();
		this.linkImage=linkImage;
		this.id=id;
		this.content=content;
	}
	
	public NewsOffline() 
	{
	}
	
	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}
	

}

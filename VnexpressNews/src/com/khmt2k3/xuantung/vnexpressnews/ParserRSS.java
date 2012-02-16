package com.khmt2k3.xuantung.vnexpressnews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ParserRSS 
{
	private String linkRSS;
	public ArrayList<News>listNews=new ArrayList<News>();
	private InputStream intputStream; 
	private Document document;
	

	public ParserRSS(String linkRSS) 
	{
		this.linkRSS=linkRSS;
		loadDocument();
		Log.d("Load Document","Ok");
		loadNews();		
	}
	
	public ParserRSS()
	{
		
	}
	public void setLinkRSS(String linkRSS)
	{
		this.linkRSS=linkRSS;
	}
	
	@SuppressWarnings("finally")
	private boolean loadInputStream()
	{
		boolean check=false;
		try
		{
			
			URL link=new URL(linkRSS);
			this.intputStream=link.openConnection().getInputStream();
			check=true;
		}
		catch (Exception e) 
		{
			Log.d("Err","Khong co ket noi mang");
		}				
		finally
		{
			return check;
		}
	}
	
	public void loadDocument()
	{
		
		Document document=null;
		if(loadInputStream())
		{
			DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuilder;
		
			try 
			{
				
				docbuilder = builderFactory.newDocumentBuilder();
				document=docbuilder.parse(this.intputStream);
				this.intputStream.close();//dong' inputStream lai.
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				
			}
			
		}
		
		this.document=document;		
	}
	
	/**
	 * Cho truong hop can load it' tin mot. it' tin mot.
	 * @param start vi tri bat dau
	 * @param end vi tri ket thuc'
	 */
	public void loadNews(int start,int end)
	{
		Element element=this.document.getDocumentElement();
		NodeList nodeList=element.getElementsByTagName("item");
		
		//dieu chinh cho end chinh' xac
		if(end>nodeList.getLength())
		{
			end=nodeList.getLength();
		}
		
		for(int i=start;i<end;i++)
		{
			Node node=nodeList.item(i);
			
			NodeList nodeListSecond=node.getChildNodes();
			
			News news=new News();
			
			for(int j=0;j<nodeListSecond.getLength();j++)
			{				
				

				Node n=nodeListSecond.item(j);
				
				String name=n.getNodeName();
				String value=n.getTextContent();
				
				if(name.compareToIgnoreCase("title")==0)
				{
					news.setTitle(value);					
				}
				else
					if(name.compareToIgnoreCase("description")==0)
					{

						news.setDescription(getStandDescription(value));//Luu lai description sau khi da loc.		
						Log.d("description",getStandDescription(value));
						news.setImage(getImage(value));//Luu lai anh minh hoa.						
					}
					else
						if(name.compareToIgnoreCase("link")==0)
						{
							news.setLink(value);							
						}
						else
							if(name.compareToIgnoreCase("pubDate")==0)
							{
								
								news.setPubDate(value);
							}
				
			}
			
			this.listNews.add(news);
		}
		
	}
	
	/**
	 * Load taat'ca tin len
	 */
	public void loadNews()
	{
		
		Element element=this.document.getDocumentElement();
		NodeList nodeList=element.getElementsByTagName("item");
		
		Log.d("Num news",nodeList.getLength()+"");
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node=nodeList.item(i);
			
			NodeList nodeListSecond=node.getChildNodes();
			
			News news=new News();
			
			for(int j=0;j<nodeListSecond.getLength();j++)
			{				
				
				
				Node n=nodeListSecond.item(j);
				
				String name=n.getNodeName();
				String value=n.getTextContent();
				
				if(name.compareToIgnoreCase("title")==0)
				{
					news.setTitle(value);
					
				}
				else
					if(name.compareToIgnoreCase("description")==0)
					{
						
						news.setDescription(getStandDescription(value));//Luu lai description sau khi da loc.		
						
						news.setImage(getImage(value));//Luu lai anh minh hoa.
						
					}
					else
						if(name.compareToIgnoreCase("link")==0)
						{
							news.setLink(value);
						
						}
						else
							if(name.compareToIgnoreCase("pubDate")==0)
							{
								
								news.setPubDate(value);
						
							}
				
			}
			
			this.listNews.add(news);
		}
		
	}
	/**
	 * 
	 * @param description du lieu hon hop. cho vao
	 * @return linkImage Link Image o dang tinh
	 */
	private String getStandLinkImage(String description)
	{
		String linkImage="";
		int i=description.indexOf("src=");
		int j=description.indexOf("\"", i+5);
		
		if(i!=-1 && j!=-1)
		{
			linkImage=description.substring(i+5, j);
		}
		return linkImage;
		
	}
	
	private Bitmap getImage(String description)
	{
		String standLinkImage=getStandLinkImage(description);
		Bitmap bitmap=null;
		if(standLinkImage!="")
		try 
		{
			bitmap=BitmapFactory.decodeStream(new URL(standLinkImage).openConnection().getInputStream());
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}	
		return bitmap;
	}
	
	//lay' Description chuan tu description ban dau (vi ban dau description rat' nhieu the lung tung)
	private String getStandDescription(String description)
	{
		String standDescription="anc";
		int a=description.indexOf("<a href");
		int A=description.indexOf("<A"); // Hoac la: <A
		int a_end=description.indexOf("/a>");
		int br=description.indexOf("<BR");
		
		
		if(a!=-1)//Neu' co' <a href
		{
			if(br!=-1)//Neu' co the <BR
			{	
				standDescription=description.substring(a_end+3,br);
			}
			else
			{	
				if(A!=-1)//Neu' co' the <A
				{
					standDescription=description.substring(a_end+3,A);
				}
				else
					standDescription=description.substring(a_end+3);
				
			}
		}
		else
		{
			if(br!=-1)
			{	
				standDescription=description.substring(0, br);
			}
			else
			{	
				if(A!=-1)
				{
					standDescription=description.substring(0,A);
				}
				else
					standDescription=description.substring(0,description.length()-1);
				
			}
			
		}
		
		return standDescription;
		
	}
	

	
}

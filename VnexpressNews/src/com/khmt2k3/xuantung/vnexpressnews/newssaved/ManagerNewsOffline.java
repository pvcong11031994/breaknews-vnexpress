package com.khmt2k3.xuantung.vnexpressnews.newssaved;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.khmt2k3.xuantung.vnexpressnews.News;

public class ManagerNewsOffline 
{
	//file de luu tin offline
	public static final String pathFile=Environment.getExternalStorageDirectory()+"/vnexpress/listnews.xml";
	public static final String pathFileXSD=Environment.getExternalStorageDirectory()+"/vnexpress/listnews.xsd";
	public static final String pathFolderImage=Environment.getExternalStorageDirectory()+"/vnexpress/image";
	public static final String folderXML=Environment.getExternalStorageDirectory()+"/vnexpress";
	private Document doc;
	private Context context;
	
	public ManagerNewsOffline(Context context) throws Exception //Khong tao duoc doi' tuong.
	{
		this.context=context;
		DocumentBuilderFactory docBuilderF=DocumentBuilderFactory.newInstance();
		docBuilderF.setNamespaceAware(true);
		DocumentBuilder docBuilder=docBuilderF.newDocumentBuilder();
		//kiem tra su ton tai cua file, neu khong thi tao file moi
		checkFile();
		this.doc=docBuilder.parse(new File(pathFile));
	
	}
	
	private void checkFile()
	{
		File folder=new File(folderXML);
		//neu chua co' folder thi tao folder XMl
		if(!folder.exists()) folder.mkdir();
		
		//neu' chua co' file listnews.xml thi tao moi'
		File xml=new File(pathFile);
		if(!xml.exists()) 
		{
			convertFile();
			Log.d("make","file");
		}
		
		File folderImage=new File(pathFolderImage);
		if(!folderImage.exists())
		{
			folderImage.mkdir();
			Log.d("make","iamge");
		}
			
	}
	
	//chuyen listnews tu asset sang the nho
	private void convertFile()
	{
		File file=new File(pathFile);
		try 
		{
			BufferedInputStream bis=new BufferedInputStream(context.getAssets().open("listnews.xml"));
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
			
			byte[] b=new byte[1024];
			int num;
			while((num=bis.read(b))!=-1)
			{
				bos.write(b, 0, num);
			}
			bos.flush();
			bos.close();
			bis.close();			
		} catch (IOException e) 
		{
			Log.d("ERR","khong the luu duoc tin");
		}
	}

	

	/**
	 * luu them mot tin
	 * @param news tin can luu
	 * @return true neu' luu duoc, false neu khong luu duoc
	 */
	public void saveNews(News news) throws Exception
	{
		NewsOffline newsOffline=convertNewsToNewsOffline(news);
		addNews(newsOffline);		
	}
	
	/**
	 * Tu link cua news, se lay' duoc noi dung, tu Image cua news, ta luu lai vao file va cung cap mot duong link dan toi' no
	 * @param news tin can luu tin nhan'
	 * @return mot doi' tuong newsoffline
	 */
	private NewsOffline convertNewsToNewsOffline(News news)
	{
		Element element=doc.getDocumentElement();
		String id; //luu dia chi id cua tin
		String content=""; //luu noi dung cua tin
		String linkImage=""; //luu linkanh cua tin (duoc luu trong the nho')
		
		//Kiem tra xem co' anh nao chua?
		if(element.getElementsByTagName(XML.id).getLength()==0)
		{
			id="0";
		}
		else
		{
			//neu' co' roi, thi id=id cua node cuoi' cung + 1 don vi (nhu vay se tranh duoc truong hop khi xoa' tin, roi lai them vao->trung id
			NodeList nodeList=element.getElementsByTagName(XML.id);
			Node nodeLast=nodeList.item(nodeList.getLength()-1);
			String temp=nodeLast.getTextContent();
			int idTemp=Integer.parseInt(temp);
			idTemp++;
			id=idTemp+"";
		}
		
		try 
		{
			content=getContent(news.getLink());
			linkImage=saveImage(news.getImage());
			
		} catch (Exception e) 
		{
			Log.d("ERROR","Khong lay' duoc noi dung");
		}
		NewsOffline newsOffline=new NewsOffline(news, id, content,linkImage);
		return newsOffline;
	}
	/**
	 * Lay noi dung bao'
	 * @param link link cua bai viet'
	 * @return noi dung trang bao'
	 */
	private String getContent(String link) throws Exception
	{
		URL url=new URL(link);
		HttpURLConnection http=(HttpURLConnection)url.openConnection();
		
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
		
		String line="";
		String content="";
		
		
		while((line=bufferedReader.readLine())!=null)
		{
			content+=line;
		}
		
		http.disconnect();
		bufferedReader.close();
		
		return getContentStandard(content);
	}
	
	private String getContentStandard(String content)
	{
		String contentStandard="";
		
		Pattern pattern=Pattern.compile(Regex.replace);
		Matcher matcher=pattern.matcher(content);
		String content2=matcher.replaceAll(Regex.replaceChar);
		
		pattern=Pattern.compile(Regex.content);
		matcher=pattern.matcher(content2);
		
		while(matcher.find())
		{
			contentStandard+=matcher.group(1)+"</br>";
		}
		
		return contentStandard;
	}
	
	/**
	 * Luu tin vao` file
	 * @param newsOffline tin offline, sau khi da lay' duoc noi dung
	 * @return luu duoc thi true, else false
	 */
	private void addNews(NewsOffline newsOffline)throws Exception
	{
		Element element=doc.getDocumentElement();
		
		
		Element elementId=doc.createElement(XML.id);
		elementId.setTextContent(newsOffline.getId());
		
		Element elementpubDate=doc.createElement(XML.pubdate);
		elementpubDate.setTextContent(newsOffline.getPubDate());
		
		Element elementTitle=doc.createElement(XML.title);
		elementTitle.setTextContent(newsOffline.getTitle());
		
		Element elementDescription=doc.createElement(XML.description);
		elementDescription.setTextContent(newsOffline.getDescription());
		
		Element elementLink=doc.createElement(XML.link);
		elementLink.setTextContent(newsOffline.getLink());
		
		Element elementImage=doc.createElement(XML.linkImage);
		elementImage.setTextContent(newsOffline.getLinkImage());
		
		Element elementContent=doc.createElement(XML.content);
		elementContent.setTextContent(newsOffline.getContent());
		
		Element elementNews=doc.createElement(XML.news);
		//append cac' element vao News
		elementNews.appendChild(elementId);	
		elementNews.appendChild(elementpubDate);
		elementNews.appendChild(elementTitle);
		elementNews.appendChild(elementDescription);
		elementNews.appendChild(elementLink);
		elementNews.appendChild(elementImage);
		elementNews.appendChild(elementContent);
		
		//append  news vao ListNews
		element.appendChild(elementNews);
		
		//luu DOM xuong file
		
		Source source=new DOMSource(doc);
		File file=new File(pathFile);
		
		Result result=new StreamResult(file);
		Transformer tran=TransformerFactory.newInstance().newTransformer();
		tran.transform(source, result);
		
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document document=builder.parse(file);
		
		SchemaFactory schemaFactory=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source source1=new StreamSource(new File(pathFileXSD));
		Schema schema=schemaFactory.newSchema(source1);
		
		Validator validator=schema.newValidator();
		validator.validate(new DOMSource(document));
		Log.d("Success","success");
		
	}
	
	/**
	 * Luu lai anh vao file
	 * @param bitmap anh bitmap duoc luu
	 * @return tra lai duong dan toi' anh da luu
	 */
	private String saveImage(Bitmap bitmap)
	{
		
		String name=UUID.randomUUID().toString();//name: la ten file duy nhat cho mot anh?, UUID su dung de sinh ra mot. chuoi nhu vay
		String linkImage=pathFolderImage+"/"+name; //duong dan anh khi anh duoc luu tai sdcard
		File file=new File(linkImage);
		FileOutputStream fos=null;
		try 
		{
			fos= new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 50, fos);
		} catch (Exception e) 
		{
			e.printStackTrace();
			Log.d("ERROR","Khong luu duoc anh");
		}
		finally
		{
			
			try 
			{
				fos.flush();
				fos.close();
			} catch (IOException e){
				e.printStackTrace();
			}
				
		}
		return linkImage;
	}
	
	/**
	 * Lay' toan bo tin da luu bang DOM
	 * @return tin da luu
	 */
	public ListNewsOffline getAllNews()
	{
		ListNewsOffline listNewsOffline=new ListNewsOffline();
		Element element=doc.getDocumentElement();
		NodeList nodeList=element.getElementsByTagName(XML.news);
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node=nodeList.item(i);
			NodeList nodeList2=node.getChildNodes();
			NewsOffline newsOffline=new NewsOffline(); 
			
			for(int j=0;j<nodeList2.getLength();j++)
			{
				Node node2=nodeList2.item(j);
				String name=node2.getNodeName();
				String value=""+node2.getTextContent();
				
				if(name.compareToIgnoreCase(XML.id)==0)
				{
					newsOffline.setId(value);
				}
				else
					if(name.compareToIgnoreCase(XML.pubdate)==0)
					{
						newsOffline.setPubDate(value);
					}
					else
						if(name.compareToIgnoreCase(XML.description)==0)
						{
							newsOffline.setDescription(value);
						}
						else
							if(name.compareToIgnoreCase(XML.title)==0)
							{
								newsOffline.setTitle(value);
							}
							else
								if(name.compareToIgnoreCase(XML.linkImage)==0)
								{
									newsOffline.setLinkImage(value);
								}
								else
									if(name.compareToIgnoreCase(XML.link)==0)
									{
										newsOffline.setLink(value);
									}
									else
										if(name.compareToIgnoreCase(XML.content)==0)
										{
											newsOffline.setContent(value);
										}
				
			}
			//Lay lai hinh anh da luu vao file luc truoc
			newsOffline.setImage(getImage(newsOffline.getLinkImage()));		
			listNewsOffline.listNews.add(newsOffline);
		}
		
		return listNewsOffline;
	}
	
	/**
	 * Lay lai anh theo duong dan toi' file
	 * @param linkImage
	 * @return
	 */
	private Bitmap getImage(String linkImage)
	{
		return BitmapFactory.decodeFile(linkImage);
	}
	
	/**
	 * xoa' di element co' id nhu truyn vao
	 * @param id
	 * @throws Exception
	 */
	
	public void deleteElement(NewsOffline newsOffline)throws Exception
	{
		Element element=doc.getDocumentElement();
		NodeList nodeList=element.getElementsByTagName(XML.id);//lay tat ca nhung tag la ID
		
		//xoa' tren file xml.
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node=nodeList.item(i);
			String s=node.getTextContent();
			if(s.equalsIgnoreCase(newsOffline.getId())) //tag nao co' text la id truyen vao thi xoa'
			{
				//remove di cha cua cai tag id co' text=id truyen vao
				node.getParentNode().getParentNode().removeChild(node.getParentNode());
				doc.normalize();
				//ghi lai doc ra file
				TransformerFactory factory=TransformerFactory.newInstance();
				Transformer trans=factory.newTransformer();
				File file=new File(pathFile);
				Source source=new DOMSource(doc);
				Result result=new StreamResult(file);
				trans.transform(source, result);
			}
		}
		Log.d("link image",newsOffline.getLinkImage());
		//xoa' anh luu trong the nho'
		File file=new File(newsOffline.getLinkImage());
		file.delete();
	}
	
	/**
	 * Delete het' tat ca cac tin
	 * @throws Exception
	 */
	public void deleteAllElement(ArrayList<NewsOffline> listNews) throws Exception
	{
		Element element=doc.getDocumentElement();
		NodeList nodeList=element.getElementsByTagName(XML.news);
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node=nodeList.item(i);
			node.getParentNode().removeChild(node);
		}
		
		//ghi lai doc ra file
		doc.normalize();
		TransformerFactory factory=TransformerFactory.newInstance();
		Transformer trans=factory.newTransformer();
		File file=new File(pathFile);
		Source source=new DOMSource(doc);
		Result result=new StreamResult(file);
		trans.transform(source, result);
		
		//Xoa' cac anh da luu trong the nho'
		for(int i=0;i<listNews.size();i++)
		{
			File file2=new File(listNews.get(i).getLinkImage());
			file2.delete();
		}
		
	}
	
	public ListNewsOffline getNewsByXpath()throws Exception
	{
		ListNewsOffline listNewsOffline=new ListNewsOffline();
		XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr= xpath.compile("//news");
         
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        
        NodeList nodeList = (NodeList) result;
        
        for (int i = 0; i < nodeList.getLength(); i++) 
        {
        	NodeList nodes=nodeList.item(i).getChildNodes();
        	NewsOffline newsOffline=new NewsOffline(); 
			
			for(int j=0;j<nodes.getLength();j++)
			{
				Node node2=nodes.item(j);
				String name=node2.getNodeName();
				String value=""+node2.getTextContent();
				
				if(name.compareToIgnoreCase(XML.id)==0)
				{
					newsOffline.setId(value);
				}
				else
					if(name.compareToIgnoreCase(XML.pubdate)==0)
					{
						newsOffline.setPubDate(value);
					}
					else
						if(name.compareToIgnoreCase(XML.description)==0)
						{
							newsOffline.setDescription(value);
						}
						else
							if(name.compareToIgnoreCase(XML.title)==0)
							{
								newsOffline.setTitle(value);
							}
							else
								if(name.compareToIgnoreCase(XML.linkImage)==0)
								{
									newsOffline.setLinkImage(value);
								}
								else
									if(name.compareToIgnoreCase(XML.link)==0)
									{
										newsOffline.setLink(value);
									}
									else
										if(name.compareToIgnoreCase(XML.content)==0)
										{
											newsOffline.setContent(value);
										}
				
			}
			//Lay lai hinh anh da luu vao file luc truoc
			newsOffline.setImage(getImage(newsOffline.getLinkImage()));		
			listNewsOffline.listNews.add(newsOffline);
        }
		return listNewsOffline;
	}
	
}

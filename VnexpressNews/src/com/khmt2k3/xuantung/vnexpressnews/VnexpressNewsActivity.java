package com.khmt2k3.xuantung.vnexpressnews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.khmt2k3.xuantung.vnexpressnews.newssaved.AdapterNewsOffline;
import com.khmt2k3.xuantung.vnexpressnews.newssaved.ListNewsOffline;
import com.khmt2k3.xuantung.vnexpressnews.newssaved.ManagerNewsOffline;

public class VnexpressNewsActivity extends Activity 
{
	String[] title={"TRANG CHỦ","XÃ HỘI","THẾ GIỚI","KINH DOANH","VĂN HÓA",
			"THỂ THAO","PHÁP LUẬT","ĐỜI SỐNG","KHOA HỌC","VI TÍNH","Ô TÔ-XE MÁY",
			"BẠN ĐỌC","TÂM SỰ","CƯỜI"};
	String[] description={"Báo điện tử vnexpress.net"};
	
	String[] linkRSS={"http://vnexpress.net/rss/gl/trang-chu.rss","http://vnexpress.net/rss/gl/xa-hoi.rss","http://vnexpress.net/rss/gl/the-gioi.rss","http://vnexpress.net/rss/gl/kinh-doanh.rss",
			"http://vnexpress.net/rss/gl/van-hoa.rss","http://vnexpress.net/rss/gl/the-thao.rss","http://vnexpress.net/rss/gl/phap-luat.rss","http://vnexpress.net/rss/gl/doi-song.rss",
			"http://vnexpress.net/rss/gl/khoa-hoc.rss","http://vnexpress.net/rss/gl/vi-tinh.rss","http://vnexpress.net/rss/gl/oto-xe-may.rss","http://vnexpress.net/rss/gl/ban-doc-viet.rss",
			"http://vnexpress.net/rss/gl/ban-doc-viet-tam-su.rss","http://vnexpress.net/rss/gl/cuoi.rss"};
	
	final int numCategory=14; //So muc. lay tin 

	ListView listView_Menu;
	ArrayList<Menu>listMenu=new ArrayList<Menu>();	
	AdapterMenu adapter;
	AdapterNewsOffline adapterNewsOffline;
	boolean check=false;//kiem tra xem co doc tin offline hay chua
	public static ParserRSS parserRSS;
	
	//dialog 
	private final static int NOT_CONNECTED=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView_Menu=(ListView)findViewById(R.id.listview_menu);        
        loadInitial();
        
        adapter=new AdapterMenu(listMenu, getApplicationContext());
        listView_Menu.setAdapter(adapter);
       
      
        listView_Menu.setOnItemClickListener(new OnItemClickListener() 
        {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			{
				if(checkInternet())
				{
					Intent intent=new Intent("com.khmt2k3.xuantung.demorss.listnewsactivity");
					Bundle bundle=new Bundle();
					bundle.putString("linkRSS", listMenu.get(arg2).getLinkRSS());				
					intent.putExtras(bundle);
					startActivityForResult(intent,1);
				}
				else
				{
					showDialog(1);
				}
			}
		});
        
       
    }
    
    /**
     * Load cac' title, description len listview menu
     */    
    private void loadInitial()
    {
    	for(int i=0;i<numCategory;i++)
    	{
    		Menu menu=new Menu(title[i], description[0], R.drawable.rss, linkRSS[i]);
    		listMenu.add(menu);
    	}
    			
    }
    
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) 
    {
	try
        {
	    if(checkSDCard())
	    {
	        	MenuInflater inflater=getMenuInflater();
	        	inflater.inflate(R.menu.menunewsoffline, menu);
	        	return true;
	    }
	    else
	    {
		Toast.makeText(this, "Không có thẻ nhớ", Toast.LENGTH_LONG).show();
		return false;
	    }
		
        }
        catch (IOException e)
        {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
        }
    	return false;
    }
    
    /**
	 * Kiem tra SDcard co hay khong va tao folder luu du lieu tuong ung
	 * @return
	 */
	private boolean checkSDCard() throws IOException
	{
	    if(!exitsSDCard())
	    {
		return false;
	    }
	    else
	    {
		File vnexpress=new File(Environment.getExternalStorageDirectory()+"/vnexpress");
		//kiem tra xem co thu muc vnexpres hay chua, neu chua thi tao moi
		if(!vnexpress.exists()) vnexpress.mkdir();
		
		File file=new File(ManagerNewsOffline.pathFile);
		if(!file.exists())
                {
	            file.createNewFile();
                }
	    }
	    return true;
	}
	/**
	 * Kiem tra xem SD card co ton tai hay khong
	 * @return true neu co, false neu khong
	 */
	private boolean exitsSDCard()
	{
	    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
			case R.id.readNewsOffline:
				readNewsOffline();
				return true;
			case R.id.readNewsOnline:
				readNewsOnline();
				return true;
			case R.id.deleteAll:
				deleteAll();
				return true;
			default:
				break;
		}
    	return true;
	}
 
    private void readNewsOffline()
    {
    	ManagerNewsOffline mno;
		try 
		{
			mno = new ManagerNewsOffline(getApplicationContext());
			ListNewsOffline listNewsOffline=mno.getNewsByXpath();
			adapterNewsOffline=new AdapterNewsOffline(listNewsOffline.listNews, getApplicationContext());
			listView_Menu.setAdapter(adapterNewsOffline);
			check=true;
		} catch (Exception e) 
		{
			Log.d("ERROR","ERROR");
		}
    }
    
    private void  readNewsOnline() 
    {
		listView_Menu.setAdapter(adapter);
	}
    
    @Override
    protected void onPause() 
    {
    	super.onPause();
    	if(check)
    	adapterNewsOffline.notifyDataSetChanged();
    }
    
    private void deleteAll()
    {
    	try 
    	{
			ManagerNewsOffline managerNewsOffline=new ManagerNewsOffline(getApplicationContext());
			managerNewsOffline.deleteAllElement(adapterNewsOffline.listNews);
			readNewsOffline();
		} catch (Exception e) 
		{
			Log.d("ERROR","Khong xoa duoc tin");
		}
    }
    
    private void deleteSelected()
    {
    	for(int i=0;i<adapterNewsOffline.listNews.size();i++)
    	{
    		if(adapterNewsOffline.listNews.get(i).check)
    		{
    			try 
    			{
					ManagerNewsOffline managerNewsOffline=new ManagerNewsOffline(getApplicationContext());
					managerNewsOffline.deleteElement(adapterNewsOffline.listNews.get(i));
					readNewsOffline();
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
    	AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    	dialog.setIcon(R.drawable.icon);
    	dialog.setTitle("Thông báo");
    	dialog.setMessage("Không có mạng");
    	dialog.setPositiveButton("OK", new OnClickListener() 
    	{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
    	
    	
    	ProgressDialog proDialog=new ProgressDialog(this);
    	proDialog.setTitle("Load...");
    	proDialog.setIcon(R.drawable.icon);
    	
    	switch (id) 
    	{
			case NOT_CONNECTED:
				return dialog.create();
			
			default:
				return proDialog;
		}
    	
    }
    
    /**
     * Kiem tra trang thai ket noi mang
     * @return true neu co mang, else neu' khong co mang
     */
    private boolean checkInternet()
    {
    	boolean check=false;
    	ConnectivityManager connectivity=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
    	if(connectivity.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED||connectivity.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTING)
    	{
    		check=true;
    	}
    	Log.d("check",check+"");
    	return check;
    }
    
  }

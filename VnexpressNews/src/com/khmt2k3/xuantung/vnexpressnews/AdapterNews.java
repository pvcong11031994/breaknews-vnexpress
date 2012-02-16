package com.khmt2k3.xuantung.vnexpressnews;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.khmt2k3.xuantung.vnexpressnews.newssaved.ManagerNewsOffline;

public class AdapterNews extends BaseAdapter 
{
	public ArrayList<News>listNews;
	private Context context;
	
	public AdapterNews(ArrayList<News>listNews,Context context) 
	{
		this.listNews=listNews;
		this.context=context;	
		Log.d("create",this.listNews.size()+"");
	}
	
	@Override
	public int getCount() {
		return this.listNews.size();
	}
	@Override
	public Object getItem(int position) {
		return this.listNews.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		
		if(convertView==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.custom_newsactivity, null);					
		}
		
		Log.d("positionVnExpress",position+"");
		
		ImageView img_news =(ImageView)convertView.findViewById(R.id.img_news);
		TextView txt_pubdate_news=(TextView)convertView.findViewById(R.id.txt_pubdate_news);
		TextView txt_title_news=(TextView)convertView.findViewById(R.id.txt_title_news);
		TextView txt_description_news=(TextView)convertView.findViewById(R.id.txt_description_news);
		final CheckBox check=(CheckBox)convertView.findViewById(R.id.check);
		
		if(listNews.get(position).getImage()==null)//Neu' anh minh hoa. khong co'=> Gan anh mac dinh			
			img_news.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon));
		else
			img_news.setImageBitmap(listNews.get(position).getImage());
		
		txt_pubdate_news.setText(listNews.get(position).getPubDate());
		txt_title_news.setText(listNews.get(position).getTitle());

		if(listNews.get(position).isRead)txt_title_news.setTextColor(Color.BLUE); //neu tin da duoc doc thi doi mau title
		else txt_title_news.setTextColor(Color.GREEN);
		txt_description_news.setText(listNews.get(position).getDescription());
		
		if(listNews.get(position).check)check.setChecked(true);
		else check.setChecked(false);
		
		Log.d("Position",position+"");
		
		//set su kien click description de doc tin
		txt_description_news.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				listNews.get(position).isRead=true;//Danh dau' la tin da duoc doc.
				Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(listNews.get(position).getLink()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				
				ManagerNewsOffline managerNewsOffline;
				try 
				{
					managerNewsOffline = new ManagerNewsOffline(context);
					managerNewsOffline.saveNews(listNews.get(position));
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
		
		//set su kien click titile de doc tin
		txt_title_news.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				listNews.get(position).isRead=true;//Danh dau' la tin da duoc doc.
				Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(listNews.get(position).getLink()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		
		//xet su kien cho checkbox
		check.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(check.isChecked())listNews.get(position).check=true;
				else listNews.get(position).check=false;
			}
		});
		return convertView;	
		
	}

}

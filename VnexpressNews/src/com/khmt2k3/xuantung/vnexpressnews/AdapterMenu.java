package com.khmt2k3.xuantung.vnexpressnews;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMenu extends BaseAdapter 
{
	private ArrayList<Menu>listMenu;
	private Context context;
	
	public AdapterMenu(ArrayList<Menu>listMenu,Context context)
	{
		this.listMenu=listMenu;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return listMenu.size();
	}
	
	@Override
	public Object getItem(int position) {
		return listMenu.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		Log.d("menu","menu");
		if(convertView==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.custom_menuactivity, null);
		}
		ImageView imgView=(ImageView)convertView.findViewById(R.id.imageView1);
		TextView txt_title_menu=(TextView)convertView.findViewById(R.id.txt_title_menu);
		TextView txt_description_menu=(TextView)convertView.findViewById(R.id.txt_description_menu);
		
		imgView.setImageResource(R.drawable.rss);
		txt_title_menu.setText(listMenu.get(position).getTitle());
		txt_description_menu.setText(listMenu.get(position).getDescription());
		
		
		return convertView;
	}

}

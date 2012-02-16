package com.khmt2k3.xuantung.vnexpressnews;

import java.util.ArrayList;

public class ListMenu 
{
	private ArrayList<Menu>listMenu=new ArrayList<Menu>();
	
	public ListMenu(ArrayList<Menu>listMenu)
	{
		this.listMenu=listMenu;
	}
	
	
	public int getSize()
	{
		if(this.listMenu!=null)
		{
			return this.listMenu.size();
		}
		return 0;
	}
	
	public void addItem(Menu menu)
	{
		this.listMenu.add(menu);
	}
	
	public Menu getItem(int position)
	{
		return this.listMenu.get(position);
	}
}

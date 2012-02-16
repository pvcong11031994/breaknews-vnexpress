package com.khmt2k3.xuantung.vnexpressnews;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.khmt2k3.xuantung.vnexpressnews.newssaved.ManagerNewsOffline;

public class ListNewsActivity extends ListActivity
{
	private String linkRSS = ""; //link RSS de lay tin
	int first = 0; //tin dau tien
	int itemsPerScroll = 1; //so tin lay' them khi cuon. listview
	int end = 5; //tin cuoi'
	ParserRSS parserRSS;
	AdapterNews adapter;
	boolean check = false;
	View v;
	
	ProgressDialog progress;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.d("start","2");
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) 
		{
			this.linkRSS = bundle.getString("linkRSS");
		}		
		v=((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview, null);
		this.getListView().addFooterView(v);
		
		//load news tu internet ve
		parserRSS = new ParserRSS();
		parserRSS.setLinkRSS(linkRSS);	
		parserRSS.loadDocument();
		//------------------

		adapter = new AdapterNews(parserRSS.listNews, getApplicationContext());
		setListAdapter(adapter);

		this.getListView().setOnScrollListener(new OnScrollListener() 
		{

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{

			}
			
			//Khi nao load toi' news cuoi' cung ma van muon doc tin tiep thi se load them news, so tin duoc load ten = itemsperscroll

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount)
			{
				int count = firstVisibleItem + visibleItemCount;
				if (count == totalItemCount) 
				{	Log.d("more","run");
					
					runOnUiThread(loadMoreItems);					
				}

			}
		});
		
		
		
	}

	private Runnable loadMoreItems = new Runnable() 
	{

		@Override
		public void run() 
		{
				parserRSS.loadNews(first, end);
				first = end;
				end += itemsPerScroll;
	
				runOnUiThread(refesh);
			
		}
	};

	private Runnable refesh = new Runnable() 
	{

		@Override
		public void run() 
		{
			adapter.notifyDataSetChanged();			
		}
	};

	
	//chuyen mau su title cua tin duoc doc.
	protected void onPause() 
	{
		super.onPause();
		adapter.notifyDataSetChanged();
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    if(checkSDcard())
	    {
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	    }
	    {
		Toast.makeText(this, "Không có thẻ nhớ", Toast.LENGTH_LONG).show();
		return false;
	    }
	}
	
	//kiem tra xem may co the nho hay ko?
	private boolean checkSDcard()
	{
	    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case R.id.savechoice:
				savdChoice();
				return true;
			case R.id.saveall:
				savdAll();
				return true;
		default:
			break;
		}
		return true;
	}
	
	private void savdChoice()
	{
		createProgress();
		new Thread(runSaveChoice).start();
	}
	
	private Runnable runSaveChoice=new Runnable() 
	{
		
		@Override
		public void run() 
		{
			ManagerNewsOffline managerNewsOffline;
			try 
			{
				managerNewsOffline = new ManagerNewsOffline(getApplicationContext());
				for(int i=0;i<adapter.listNews.size();i++)
				{
					if(adapter.listNews.get(i).check)
					managerNewsOffline.saveNews(adapter.listNews.get(i));
				}
			} catch (Exception e) 
			{
				Log.d("ERROR","Khong the luu tin");
			}
			progress.dismiss();
		}
	};
	
	
	private Runnable runSaveAll=new Runnable() 
	{
		
		@Override
		public void run() 
		{
			ManagerNewsOffline managerNewsOffline;
			try
			{
				managerNewsOffline = new ManagerNewsOffline(getApplicationContext());
				for(int i=0;i<adapter.listNews.size();i++)
				{
					managerNewsOffline.saveNews(adapter.listNews.get(i));
				}
			} catch (Exception e) 
			{
				Log.d("ERROR","Khong the luu tin");
			}
			progress.dismiss();
		}
	};
	
	private void savdAll()
	{
		createProgress();
		new Thread(runSaveAll).start();
	}
	
	private void createProgress()
	{
		progress=new ProgressDialog(this);
		progress.setIcon(R.drawable.icon);
		progress.setTitle("Lưu tin");
		progress.setMessage("Hãy chờ một chút");
		progress.setCancelable(true);
		progress.show();
	}
	
	
}

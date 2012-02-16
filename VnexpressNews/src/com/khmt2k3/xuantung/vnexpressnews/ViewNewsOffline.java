package com.khmt2k3.xuantung.vnexpressnews;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewNewsOffline extends Activity 
{
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewnewsoffline);
		
		webView=(WebView)findViewById(R.id.webView1);
		Bundle intent=getIntent().getExtras();
		
		
		String title=intent.getString("title");
		String description=intent.getString("description");
		String content=intent.getString("content");
		
		String html="<b>"+title+"</b></br></br>"+description+"</br></br>"+content;
		webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
	}

}

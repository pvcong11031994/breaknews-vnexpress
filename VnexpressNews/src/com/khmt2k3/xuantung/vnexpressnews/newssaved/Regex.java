package com.khmt2k3.xuantung.vnexpressnews.newssaved;

public interface Regex 
{
	public static final String content="<P class=Normal>(.*?)</P>";//dung de lay noi dung tin html
	public static final String replace="\\s+";//dung de thay 2 hay nhieu: dau cach, tab thanh 1 dau' cach'
	public static final String replaceChar=" ";//ky' tu dung de thay the'
	
}


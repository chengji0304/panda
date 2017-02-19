package com.panda.store.utils;

public class StringUtil {
	/**
	 * 生产64位的String 数组
	 * @throws  
	 */
	public static String  toString(String s) {
		byte[] a=new byte[32];
		byte[] b=s.getBytes();
	
		System.arraycopy(b, 0,a, 0, b.length);
	  return new String(a);
	
		
	}
	/**
	 * 判断字符串是否为空，“”
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrNull(String str) {
		/*
		 * return str == null || str.length() == 0 || str.contentEquals("null")
		 * || str.trim().equals("");
		 */
		if (null == str)
	        return true;
	    if (str.length() == 0)
	        return true;
	    if (str.trim().length() == 0)
	        return true;
	   
	    return false;
	}
	
}

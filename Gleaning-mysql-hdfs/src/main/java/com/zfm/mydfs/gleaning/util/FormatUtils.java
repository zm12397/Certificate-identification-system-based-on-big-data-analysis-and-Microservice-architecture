package com.zfm.mydfs.gleaning.util;

public class FormatUtils {
	public static String formatPlace(String source,String str,String target){
		return source.replaceAll(str, target);
	}
}

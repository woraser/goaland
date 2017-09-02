package com.anosi.asset.util;

public class StringUtil {

	/***
	 * 将字符串按照括号外的","分隔
	 * 
	 * name,id,list*.(id,name,list*.(id,name))会被分成[name][id][list*.(id,name,list*.(id,name))]
	 * @param attributes
	 * @return
	 */
	public static String[] splitAttributes(String attributes){
		String replaceAll = attributes.replaceAll("(?<!\\([^()]{1,1000000}),(?![^()]{1,1000000}\\))", "!@#");
		return replaceAll.split("!@#");
	}
	
}

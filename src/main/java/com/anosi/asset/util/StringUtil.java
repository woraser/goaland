package com.anosi.asset.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	/***
	 * 将字符串按照括号外的","分隔
	 * 
	 * name,id,list*.(id,name,list*.(id,name))会被分成[name][id][list*.(id,name,list
	 * *.(id,name))]
	 * 
	 * @param attributes
	 * @return
	 */
	public static String[] splitAttributes(String attributes) {
		if (StringUtils.isBlank(attributes)) {
			return null;
		}
		String replaceAll = attributes.replaceAll("(?<!\\([^()]{1,1000000}),(?![^()]{1,1000000}\\))", "!@#");
		return replaceAll.split("!@#");
	}

}

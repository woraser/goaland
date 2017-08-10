package com.anosi.asset.test;

import org.junit.Test;

import com.anosi.asset.util.PinyinUtil;

public class TestPinyin {

	@Test
	public void testPinyin(){
		String hanyu="你好啊";
		System.out.println(PinyinUtil.getStringPinYin(hanyu)[0]);
		
		String[] charPinYin = PinyinUtil.getCharPinYin("你".charAt(0));
		for (String pinyin : charPinYin) {
			System.out.println(pinyin);
		}
	}
	
}

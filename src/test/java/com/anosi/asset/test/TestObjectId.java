package com.anosi.asset.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.anosi.asset.model.mongo.FileMetaData;

public class TestObjectId {

	@Test
	public void testConvert(){
		System.out.println(FileMetaData.BigIntegerToObjectIdConverter(new BigInteger("27723046081874865598250554642")));
	}
	
	@Test
	public void testSuffix(){
		String name="abc.txt";
		System.out.println(name.substring(0, name.lastIndexOf("."))+".pdf");
	}
	
	@Test
	public void testReg(){
		List<String> ls=new ArrayList<String>();
		Pattern regex = Pattern.compile("(?<!\\([^()]{1,1000000}),(?![^()]{1,1000000}\\))");
        Matcher matcher = regex.matcher("name,id,list*.(id,name,list*.(id,name,value),value),list2*.(name,key,list*.(hello,id))");
        while(matcher.find())
            ls.add(matcher.group());
        System.out.println(ls.size());
        System.out.println(ls);
	}
	
	@Test
	public void testSubString(){
		String value = "list*.(id,name,list*.(id,name))";
		String[] names = value.split("\\*.", 2);
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	@Test
	public void testReplace(){
		String a = "name,id,list*.(id,name,list*.(id,name))";
		String replaceAll = a.replaceAll("(?<!\\([^()]{1,1000000}),(?![^()]{1,1000000}\\))", "!@#");
		System.out.println(replaceAll);
	}
	
}

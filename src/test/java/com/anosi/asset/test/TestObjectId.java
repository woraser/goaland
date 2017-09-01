package com.anosi.asset.test;

import java.math.BigInteger;

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
	
}

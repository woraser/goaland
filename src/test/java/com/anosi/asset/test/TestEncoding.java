package com.anosi.asset.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class TestEncoding {

	@Test
	public void testEncoding() throws UnsupportedEncodingException {
		String s1 = "hello中国人";
		String iso = new String(s1.getBytes("utf-8"), "iso-8859-1");
		System.out.println(iso);// helloä¸­å½äºº
		String utf8 = new String(iso.getBytes("iso-8859-1"), "utf-8");
		System.out.println(utf8);// hello中国人

		String s2 = "版权所有(严禁翻印)";
		String gbk = new String(s2.getBytes("utf-8"), "gbk");
		System.out.println(gbk);// hello涓浗浜�
		String utf82 = new String(gbk.getBytes("gbk"), "utf-8");
		System.out.println(utf82);// hello中国�?
	}

}

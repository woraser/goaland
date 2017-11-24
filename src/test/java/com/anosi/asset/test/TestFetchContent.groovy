package com.anosi.asset.test

import org.junit.Test

import com.anosi.asset.model.elasticsearch.SearchRecord
import com.anosi.asset.util.FileFetchUtil
import com.anosi.asset.util.TXTUtil

class TestFetchContent {

	@Test
	void testTxt(){
		println FileFetchUtil.fetchContent(new File('E:/testFetch.txt'))
		println TXTUtil.readTXT(new File('E:/testFetch.txt'))
		println TXTUtil.readTXT(new File('C:/Users/jinyao/Downloads/20171121164102732.txt'))
	}
	
	@Test
	void testXLSX(){
		println FileFetchUtil.fetchContent(new File('F:/20161226155537.xlsx'))
	}
	
	@Test
	void testCSV(){
		println FileFetchUtil.fetchContent(new File('F:/online/onlinedata/2016/1024/1.csv'))
	}
	
	@Test
	void testPDF(){
		println FileFetchUtil.fetchContent(new File('G:/groovy+in+action_中文.pdf'))
	}
	
	@Test
	void testDOC(){
		println FileFetchUtil.fetchContent(new File('E:/水源产品生命周期跟踪系统技术文档7.11.docx'))
	}
	
}

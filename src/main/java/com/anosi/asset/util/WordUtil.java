package com.anosi.asset.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/****
 * 读取word
 * @author jinyao
 *
 */
public class WordUtil {

	/***
	 * 重载
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readWord(File file) throws IOException {
		return readWord(new FileInputStream(file));
	}
	
	/***
	 * 读取word
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readWord(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		HWPFDocument hdoc = null;
		XWPFDocument xdoc = null;
		XWPFWordExtractor extractor = null;

		// 针对03和07版本的区别
		// 发生异常会使得is流关闭,所以以防万一需要将流提取成byte数组,这样可以复用
		byte[] byteArray = IOUtils.toByteArray(is);
		try {
			hdoc = new HWPFDocument(new ByteArrayInputStream(byteArray));
			Range rang = hdoc.getRange();
			sb.append(rang.text());
		} catch (OfficeXmlFileException e) {
			// 捕获版本异常
			xdoc = new XWPFDocument(new ByteArrayInputStream(byteArray));
			extractor = new XWPFWordExtractor(xdoc);
			sb.append(extractor.getText());
		}finally {
			if(extractor!=null){
				extractor.close();
			}
		}

		return sb.toString();
	}

}

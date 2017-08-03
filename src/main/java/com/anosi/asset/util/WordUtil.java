package com.anosi.asset.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

	public static String readWord(File file) throws IOException {
		StringBuilder sb = new StringBuilder();

		HWPFDocument hdoc = null;
		XWPFDocument xdoc = null;
		XWPFWordExtractor extractor = null;

		try {
			hdoc = new HWPFDocument(new FileInputStream(file));
			Range rang = hdoc.getRange();
			sb.append(rang.text());
		} catch (OfficeXmlFileException e) {
			// 捕获版本异常
			xdoc = new XWPFDocument(new FileInputStream(file));
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

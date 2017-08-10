package com.anosi.asset.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFUtil {
	
	/***
	 * 获取pdf的文本内容
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public static String readTextFromPDF(InputStream is) throws IOException{
		PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
	    parser.parse();
	    PDDocument document = parser.getPDDocument();
		return readTextFromDocument(document);
	}
	
	/***
	 * 重载
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readTextFromPDF(File file) throws IOException{
		PDDocument document=PDDocument.load(file);
		return readTextFromDocument(document);
	}
	
	private static String readTextFromDocument(PDDocument document) throws IOException{
		// 获取页码
        int pages = document.getNumberOfPages();

        // 读文本内容
        PDFTextStripper stripper=new PDFTextStripper();
        // 设置按顺序输出
        stripper.setSortByPosition(true);
        stripper.setStartPage(1);
        stripper.setEndPage(pages);
        String content = stripper.getText(document);
        return content;
	}
	
}

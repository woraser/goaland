package com.anosi.asset.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.anosi.asset.ExcelUtil.parser.ExcelParser;
import com.anosi.asset.ExcelUtil.parser.ExcelParser.ParserType;
import com.aspose.cells.SaveFormat;
import com.google.common.collect.Table;

/***
 * 主要用于读取excel，导出excel大部分情况下使用easypoi
 * 
 * @author jinyao
 *
 */
public class ExcelUtil {

	/***
	 * 方法重载readExcelToText
	 * 
	 * @param file
	 * @param sheetIndex
	 * @return
	 * @throws IOException
	 */
	public static String readExcelToText(File file, Integer sheetIndex) throws Exception {
		return readExcelToText(new FileInputStream(file), sheetIndex);
	}

	/***
	 * 将excel读取string
	 * 
	 * @param is
	 * @param sheetIndex
	 *            sheet的序号，从0开始,如果传入的是-1,表示所有sheet
	 * @return
	 * @throws IOException
	 */
	public static String readExcelToText(InputStream is, Integer sheetIndex) throws Exception {
		return new ExcelParser().readExcelToString(is, sheetIndex);
	}

	/***
	 * 方法重载
	 * 
	 * @param file
	 * @param sheetIndex
	 * @return
	 * @throws IOException
	 */
	public static Table<Integer, String, Object> readExcel(File file, Integer sheetIndex) throws Exception {
		return readExcel(new FileInputStream(file), sheetIndex);
	}

	/**
	 * 读取excel
	 * 
	 * @param is
	 * @param sheetIndex
	 *            sheet的序号，从0开始,如果为-1,代表遍历全部sheet
	 * 
	 * @return 返回Guava的table类型
	 * 
	 *         可以看用例 {@link com.anosi.asset.test.TestExcel#testReadExcel()}
	 */
	public static Table<Integer, String, Object> readExcel(InputStream is, Integer sheetIndex) throws Exception {
		return new ExcelParser().readExcel(is, sheetIndex, ParserType.SAX);
	}

	/***
	 * 将excel转为pdf
	 * 
	 * @param inputStream
	 * @param outputStream
	 */
	public static void convert2PDF(InputStream inputStream, OutputStream outputStream) throws Exception {
		new com.aspose.cells.Workbook(inputStream).save(outputStream, SaveFormat.PDF);
	}

}

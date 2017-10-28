package com.anosi.asset.util

import java.security.PublicKey

import com.anosi.asset.exception.CustomRunTimeException

class FileFetchUtil {
	
	public static enum Suffix{
		TXT,XLS,XLSX,DOC,DOCX,CSV,PDF
	}

	/***
	 * 提取所有文件中的文本
	 * @param files
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<String> fetchContent(List<File> files) throws FileNotFoundException {
		//提前检查一遍，提高效率
		files.each{
			checkSuffix(it.getName().substring(it.getName().lastIndexOf(".") + 1))
		}
		def contents=[]
		files.collect(contents){ fetchContent(it) }
		return contents
	}
	
	/***
	 * 检查后缀
	 * @param suffixs
	 */
	public static void checkSuffixs(List<String> suffixs){
		suffixs.each{
			checkSuffix(it.toUpperCase())
		}
	}

	/***
	 * 提取文件中的文本内容
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String fetchContent(File file) throws FileNotFoundException {
		def suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase()
		return fetchContent(suffix, new FileInputStream(file))
	}

	/***
	 * 提取流中的文本内容
	 * @param suffix
	 * @param is
	 * @return
	 */
	public static String fetchContent(String suffix, InputStream is) {
		if(checkSuffix(suffix)){
			return "fetch$suffix"(is)
		}else{
			return ""
		}
	}

	private static Boolean checkSuffix(String suffix){
		try {
			//如果不在枚举类中直接报错
			Suffix.valueOf(suffix)
		} catch (IllegalArgumentException e) {
			return false
		}
		return true
	}

	/***
	 * 读取pdf的文本内容
	 */
	private static String fetchPDF(InputStream is) {
		return PDFUtil.readTextFromPDF(is)
	}

	/***
	 * 读取csv的文本内容
	 * @param is
	 * @return
	 */
	private static String fetchCSV(InputStream is) {
		return CSVUtil.readCSVToText(is)
	}

	/***
	 * 读取excel的文本内容(2007版)
	 * @param is
	 * @return
	 */
	private static String fetchXLSX(InputStream is) {
		return ExcelUtil.readExcelToText(is, -1)
	}
	
	/***
	 * 读取excel的文本内容(2002版)
	 * @param is
	 * @return
	 */
	private static String fetchXLS(InputStream is) {
		return fetchXLSX(is)
	}

	/***
	 * 读取word的文本内容(2007版)
	 * @param is
	 * @return
	 */
	private static String fetchDOCX(InputStream is) {
		return WordUtil.readWord(is)
	}
	
	/***
	 * 读取word的文本内容(2003版)
	 * @param is
	 * @return
	 */
	private static String fetchDOC(InputStream is) {
		return WordUtil.readWord(is)
	}

	/***
	 * 读取txt的文本内容
	 * @param is
	 * @return
	 */
	private static String fetchTXT(InputStream is){
		return TXTUtil.readTXT(is)
	}
	
}

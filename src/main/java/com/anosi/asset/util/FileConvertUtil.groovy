package com.anosi.asset.util

import org.apache.commons.io.IOUtils

import com.anosi.asset.exception.CustomRunTimeException
import com.anosi.asset.util.FileConvertUtil.Picture

class FileConvertUtil {

	static enum Suffix{
		TXT,XLS,XLSX,DOC,DOCX,CSV,PDF,GIF,PNG,JPEG,BMP,ICON
	}

	enum Picture{
		GIF,PNG,JPEG,BMP,ICON
	}

	/***
	 * 转换文件类型
	 * @param suffix 所要转换的类型
	 * @param is
	 * @param os
	 */
	static void convert(InputStream is,Suffix originalSuffix,OutputStream os,Suffix convertSuffix) {
		if(originalSuffix==convertSuffix){
			IOUtils.copy(is, os)
		}else if (checkPicture(originalSuffix)) {
			convertPicture(is, os, originalSuffix, convertSuffix)
		}else{
			"convert$originalSuffix"(is,os,convertSuffix)
		}
	}

	private static boolean checkPicture(Suffix originalSuffix){
		try {
			Picture.valueOf("$originalSuffix")
		} catch (IllegalArgumentException e) {
			return false
		}
		return true
	}

	/***
	 * 将图片转为convertSuffix
	 * @param is
	 * @param os
	 * @param convertSuffix
	 */
	private static void convertPicture(InputStream is,OutputStream os,Suffix originalSuffix,Suffix convertSuffix) {
		ImageUtil."convert2$convertSuffix"(is, os, "$originalSuffix")
	}

	/***
	 * 将pdf转为convertSuffix
	 * @param is
	 * @param os
	 * @param convertSuffix
	 */
	private static void convertPDF(InputStream is,OutputStream os,Suffix convertSuffix) {
		PDFUtil."convert2$convertSuffix"(is, os)
	}

	/***
	 * 将csv转为convertSuffix
	 * @param is
	 * @return
	 */
	private static void convertCSV(InputStream is,OutputStream os,Suffix convertSuffix) {
		CSVUtil."convert2$convertSuffix"(is, os)
	}

	/***
	 * 将excel转为convertSuffix(2007版)
	 * @param is
	 * @return
	 */
	private static void convertXLSX(InputStream is,OutputStream os,Suffix convertSuffix) {
		ExcelUtil."convert2$convertSuffix"(is, os)
	}

	/***
	 * 将excel转为convertSuffix(2002版)
	 * @param is
	 * @return
	 */
	private static void convertXLS(InputStream is,OutputStream os,Suffix convertSuffix) {
		ExcelUtil."convert2$convertSuffix"(is, os)
	}

	/***
	 * 将word转为convertSuffix(2007版)
	 * @param is
	 * @return
	 */
	private static void convertDOCX(InputStream is,OutputStream os,Suffix convertSuffix) {
		WordUtil."convert2$convertSuffix"(is, os)
	}

	/***
	 * 将word转为convertSuffix(2003版)
	 * @param is
	 * @return
	 */
	private static void convertDOC(InputStream is,OutputStream os,Suffix convertSuffix) {
		WordUtil."convert2$convertSuffix"(is, os)
	}

	/***
	 * 将txt转为convertSuffix
	 * @param is
	 * @return
	 */
	private static void convertTXT(InputStream is,OutputStream os,Suffix convertSuffix){
		TXTUtil."convert2$convertSuffix"(is, os)
	}
}

package com.anosi.asset.util

import java.io.ByteArrayInputStream
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils

class TXTUtil {

	/***
	 * 判断中文字符
	 * 这里只是简单判断，不是UTF-8就是GBK
	 * @param is
	 * @return
	 */
	public static String charsetName(InputStream is){
		byte[] b = new byte[3]
		is.read(b)

		def charsetName;
		if (b[0] == -17 && b[1] == -69 && b[2] == -65)
			charsetName='UTF-8'
		else
			charsetName='GBK'
		return charsetName
	}

	public static String readTXT(File file){
		def sb = new StringBuilder()
		file.eachLine(charsetName(new FileInputStream(file))) { sb.append(it+"\n") }
		return sb.toString()
	}

	public static String readTXT(InputStream is){
		byte[] byteArray = IOUtils.toByteArray(is);
		def sb = new StringBuilder()
		IOUtils.readLines(new ByteArrayInputStream(byteArray), Charset.forName(charsetName(is))).each{
			sb.append(it+"\n")
		}
		return sb.toString()
	}
}

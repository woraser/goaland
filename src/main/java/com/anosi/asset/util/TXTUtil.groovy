package com.anosi.asset.util

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils

import com.aspose.words.Document
import com.aspose.words.DocumentBuilder
import com.aspose.words.SaveFormat

class TXTUtil {

	/***
	 * 判断中文字符
	 * 这里只是简单判断，不是UTF-8就是GBK
	 * @param is
	 * @return
	 */
	static String charsetName(InputStream is){
		byte[] b = new byte[3]
		is.read(b)

		def charsetName;
		if (b[0] == -17 && b[1] == -69 && b[2] == -65)
			charsetName='UTF-8'
		else
			charsetName='GBK'
		return charsetName
	}

	static String readTXT(File file){
		def sb = new StringBuilder()
		file.eachLine(charsetName(new FileInputStream(file))) { sb.append(it+"\n") }
		return sb.toString()
	}

	static String readTXT(InputStream is){
		byte[] byteArray = IOUtils.toByteArray(is);
		def sb = new StringBuilder()
		IOUtils.readLines(new ByteArrayInputStream(byteArray), Charset.forName("UTF-8")).each{
			sb.append(it+"\n")
		}
		return sb.toString()
	}
	
	/***
	 * 将txt转为pdf
	 * @param inputStream
	 * @param outputStream
	 * @throws Exception
	 */
	static void convert2PDF(InputStream is, OutputStream os) throws Exception {
		def doc = new Document();
		def builder = new DocumentBuilder(doc);
		byte[] byteArray = IOUtils.toByteArray(is);
		IOUtils.readLines(new ByteArrayInputStream(byteArray), Charset.forName("UTF-8")).each{
			builder.write(it);  
	        builder.write("\n");
		}
		doc.save(os, SaveFormat.PDF);
	}
	
}

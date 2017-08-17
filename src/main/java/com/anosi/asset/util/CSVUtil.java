package com.anosi.asset.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import com.aspose.cells.LoadFormat;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.csvreader.CsvReader;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class CSVUtil {
	
	/***
	 * 重载
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Table<Integer, String, Object> readCsv(File file) throws FileNotFoundException{
		return readCsv(new FileReader(file));
	}
	
	/***
	 * 重载
	 * @param is
	 * @return
	 */
	public static Table<Integer, String, Object> readCsv(InputStream is){
		return readCsv(new InputStreamReader(is));
	}
	
	/***
	 * 读取csv
	 * @param file
	 * @return
	 */
	public static Table<Integer, String, Object> readCsv(Reader rd){
		// 使用Guava的table来存放读取excel的数据
		// 三个泛型分别为行(int)，列(string)，值(object)
		Table<Integer, String, Object> excelTable = HashBasedTable.create();
		
		CsvReader reader = null;
		try {
			reader = new CsvReader(rd);
			
			String[] headers = null;
	        int i = 0;
	        while (reader.readRecord()){
	        	//为0时表示表头，其余为内容
	        	if(i==0){
	        		headers=reader.getValues();//表头
	        	}else{
	        		String[] values = reader.getValues();
	        		for (int j=0;j<values.length;j++) {
	        			excelTable.put(i, headers[j], values[j]);//设置值
					}
	        	}
	        	i++;
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if(reader!=null){
				reader.close();
			}
		}
		
		return excelTable;
	}
	
	/***
	 * 重载readCSVToText
	 * 
	 * @param is
	 * @return
	 */
	public static String readCSVToText(InputStream is) {
		return readCSVToText(new InputStreamReader(is));
	}

	/***
	 * 将csv读取成文本
	 * 
	 * @param rd
	 * @return
	 */
	public static String readCSVToText(Reader rd) {
		StringBuilder sb = new StringBuilder();
		CsvReader reader = null;
		try {
			reader = new CsvReader(rd);

			while (reader.readRecord()) {
				// 为0时表示表头，其余为内容
				String[] values = reader.getValues();
				for (String value : values) {
					sb.append(value + '\t');
				}
				sb.append('\n');
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return sb.toString();
	}
	
	/***
	 * 将csv转换为pdf
	 * @param inputStream
	 * @param outputStream
	 * @throws Exception
	 */
	public static void convert2PDF(InputStream inputStream, OutputStream outputStream) throws Exception {
		new Workbook(inputStream,new LoadOptions(LoadFormat.CSV)).save(outputStream, SaveFormat.PDF);
	}
	
}

package com.anosi.asset.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class InsertIntoSearch {

	private static final Logger logger = LoggerFactory.getLogger(InsertIntoSearch.class);

	@Test
	public void insertInto() {
		File file = new File("F:/词库/基础扩展");
		File toFile = new File("F:/request.json");
		List<File> fileList = Arrays.asList(file.listFiles());
		for (File file2 : fileList) {
			readTXT(file2, toFile);
		}
	}
	
	public static String readTXT(File file,File toFile){
		List<String> lines = new ArrayList<>();
		try {
			lines = IOUtils.readLines(new FileInputStream(file), Charset.forName("UTF-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String line : lines) {
			writeToFile(line.split(" ")[0], toFile);
		}
		return "";
	}
	
	static File writeToFile(String content,File file){
		StringBuilder sb = new StringBuilder();
		createJson(content,sb);
		try {
			FileUtils.write(file, sb.toString(), Charset.forName("UTF-8"),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	static void createJson(String content,StringBuilder sb){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("_index", "goaland");
		jsonObject.put("_type", "searchRecord");
		jsonObject.put("_id", UUID.randomUUID().toString());
		JSONObject outJson = new JSONObject();
		outJson.put("index", jsonObject);
		JSONObject contentJson = new JSONObject();
		contentJson.put("searchContent", content);
		
		logger.debug(outJson.toString());
		logger.debug(contentJson.toString());
		sb.append(outJson.toString()+"\n");
		sb.append(contentJson.toString()+"\n");
	}
	
}

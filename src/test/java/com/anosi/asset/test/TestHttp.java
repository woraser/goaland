package com.anosi.asset.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.anosi.asset.util.URLConncetUtil;

public class TestHttp {

	@Test
	public void testGet() {
		String url = "http://127.0.0.1:8080//iotx/management/data/REMOTE";
		String param = "showAttributes=serialNo,id";
		URLConnection connection = URLConncetUtil.sendGet(url, param);
		StringBuilder result = new StringBuilder();
		try (InputStream inputStream = connection.getInputStream(); // 获取文件流
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* end:trywithresource */
		System.out.println(JSON.parseObject(result.toString()).toString());
	}
	
}

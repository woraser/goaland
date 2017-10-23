package com.anosi.asset.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/***
 * 地图相关工具类
 * 
 * @author jinyao
 *
 */
@Configuration
public class MapUtil {

	private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

	private static String baseUrl;

	private static String ak;

	// spring不允许绑定静态属性
	@Value("${baidu.map.baseUrl}")
	public void setBaseUrl(String baseUrl) {
		MapUtil.baseUrl = baseUrl;
	}

	@Value("${baidu.map.ak}")
	public void setAK(String ak) {
		MapUtil.ak = ak;
	}

	/***
	 * 根据经纬度获取位置信息
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 * 
	 * 		http://api.map.baidu.com/geocoder/v2/?ak=
	 *         5CszUV7dPeeTfhUi2OR8hXncqKYz2WqW&location=39.915,116.404&output=
	 *         json&pois=0
	 * 
	 *         返回值格式 { "status": 0, "result": { "location": { "lng":
	 *         116.40399999999993, "lat": 39.91500007703725 },
	 *         "formatted_address": "北京市东城区中华路甲10号", "business": "天安门,前门,和平门",
	 *         "addressComponent": { "country": "中国", "country_code": 0,
	 *         "province": "北京市", "city": "北京市", "district": "东城区", "adcode":
	 *         "110101", "street": "中华路", "street_number": "甲10号", "direction":
	 *         "西南", "distance": "64" }, "pois": [], "roads": [], "poiRegions":
	 *         [ { "direction_desc": "内", "name": "天安门", "tag": "旅游景点" } ],
	 *         "sematic_description": "天安门内", "cityCode": 131 } }
	 */
	public static JSONObject geocoderByLocation(String longitude, String latitude) {
		StringBuilder result = new StringBuilder();
		URLConnection connection = URLConncetUtil.sendGet(baseUrl + "/geocoder/v2/",
				"ak=" + ak + "&location=" + latitude + "," + longitude + "&output=json&pois=0");

		// 返回的是一个json 字符串，所以使用字符流读取
		/* start:trywithresource */
		// 定义BufferedReader输入流来读取URL的响应
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
		logger.debug("connect result:{}", result.toString());
		return JSON.parseObject(result.toString());
	}

	/***
	 * 获取地址有关的信息
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 * 
	 * 		"addressComponent": { "country": "中国", "country_code": 0,
	 *         "province": "北京市", "city": "北京市", "district": "东城区", "adcode":
	 *         "110101", "street": "中华路", "street_number": "甲10号", "direction":
	 *         "西南", "distance": "64" }
	 */
	public static JSONObject getAddressComponent(String longitude, String latitude) {
		JSONObject geocoderByLocation = geocoderByLocation(longitude, latitude);
		JSONObject result = geocoderByLocation.getJSONObject("result");
		return result.getJSONObject("addressComponent");
	}

	/***
	 * 将地球坐标转为百度坐标
	 * 
	 * url:http://api.map.baidu.com/ag/coord/convert?x=116.4136103013&y=39.
	 * 9110666857&from=0&to=4&mode=0,
	 * 返回值:{"error":0,"x":"MTE2LjQyNjI4MTM2MDk3","y":"MzkuOTE4NjQxMjY2MzIz"},
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static JSONObject convertLocation(String longitude, String latitude) {
		StringBuilder result = new StringBuilder();
		URLConnection connection = URLConncetUtil.sendGet(baseUrl + "/ag/coord/convert",
				"from=0&to=4&mode=0&x=" + longitude + "&y=" + latitude);

		// 返回的是一个json 字符串，所以使用字符流读取
		/* start:trywithresource */
		// 定义BufferedReader输入流来读取URL的响应
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
		logger.debug("connect result:{}", result.toString());
		return JSON.parseObject(result.toString());
	}

}

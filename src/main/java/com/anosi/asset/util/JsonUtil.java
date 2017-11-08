package com.anosi.asset.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.exception.CustomRunTimeException;

import groovy.json.JsonOutput;

@Component("jsonUtil")
public class JsonUtil<T> {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	/***
	 * 将pages转换成json
	 * 格式:{page:"当前页数,从0开始",total:"总页数",records:"总记录数",content:[{},{},{}.....]}
	 * 
	 * @param pages
	 * @param showAttributes
	 * @return
	 */
	public JSONObject parseAttributesToJson(Page<T> pages, String[] showAttributes) throws Exception {
		JSONObject jsonObject = new JSONObject();
		// 设置page
		jsonObject.put("page", pages.getNumber());
		// 设置total
		jsonObject.put("total", pages.getTotalPages());
		// 设置record
		jsonObject.put("records", pages.getTotalElements());
		// 设置content
		jsonObject.put("content", parseAttributesToJsonArray(showAttributes, pages.getContent()));
		return jsonObject;
	}

	/****
	 * 将对象t的attributes属性数组转换成json
	 * 
	 * @param attributes
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public JSONObject parseAttributesToJson(String[] attributes, T t) throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (t == null) {
			return jsonObject;
		}
		if (attributes == null) {
			return JSON.parseObject(JSON.toJSONString(t));
		}
		for (String attribute : attributes) {
			if (attribute.contains("*.")) {
				// 2代表只对*.分割一次
				jsonObject.put(attribute.split("\\*.", 2)[0], PropertyUtil.getNestedProperty(t, attribute));
			} else if (attribute.endsWith("*")) {
				// 如果以"*"结果,代表获取这个bean下的所有属性
				// key为attribute去掉结尾的*
				jsonObject.put(attribute.substring(0, attribute.length() - 1),
						PropertyUtil.getNestedProperty(t, attribute));
			} else {
				jsonObject.put(attribute, PropertyUtil.getNestedProperty(t, attribute));
			}
		}
		logger.debug("jsonUtil result:{}", JsonOutput.prettyPrint(jsonObject.toString()));
		return jsonObject;
	}

	/***
	 * 将ts中每个对象t的attributes属性数组转换成json
	 * 
	 * @param attributes
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public JSONArray parseAttributesToJsonArray(String[] attributes, Iterable<T> ts) throws Exception {
		JSONArray jsonArray = new JSONArray();
		for (T t : ts) {
			jsonArray.add(parseAttributesToJson(attributes, t));
		}
		return jsonArray;
	}

	/***
	 * 生成返回给autocomplete的jsonarray 格式为[ { label: "Choice1", value: "value1" },
	 * ... ] 如果value为空，格式为[ "Choice1", "Choice2" ]
	 * 
	 * @param label
	 * @param value
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public JSONArray parseAttributesToAutocomplete(String label, String value, Iterable<T> ts) throws Exception {

		if (StringUtils.isBlank(label)) {
			throw new CustomRunTimeException("lable cannot be null or ''");
		}

		JSONArray jsonArray = new JSONArray();
		for (T t : ts) {
			// 如果value为空
			if (StringUtils.isBlank(value)) {
				jsonArray.add(PropertyUtil.getNestedProperty(t, label));
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("label", PropertyUtil.getNestedProperty(t, label));
				jsonObject.put("value", PropertyUtil.getNestedProperty(t, value));
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}

}

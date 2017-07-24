package com.anosi.asset.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.exception.CustomRunTimeException;

import groovy.json.JsonOutput;

@Component("jsonUtil")
public class JsonUtil<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	public JSONObject parseAttributesToJson(String[] attributes,T t) throws Exception{
		JSONObject jsonObject = new JSONObject();
		for (String attribute : attributes) {
			try {
				//根据属性名获取属性
				String nestedProperty = BeanUtils.getNestedProperty(t, attribute);
				jsonObject.put(attribute, nestedProperty);
			} catch (NestedNullException e) {
				//发生这个异常表示找不到attribute，或者attribute的值为null
				jsonObject.put(attribute, null);
			}
		}
		logger.debug("jsonUtil result:{}",JsonOutput.prettyPrint(jsonObject.toString()));
		return jsonObject;
	}

	public JSONArray parseAttributesToJsonArray(String[] attributes,Iterable<T> ts) throws Exception{
		JSONArray jsonArray = new JSONArray();
		for (T t : ts) {
			jsonArray.add(parseAttributesToJson(attributes, t));
		}
		return jsonArray;
	}
	
	/***
	 * 生成返回给autocomplete的jsonarray
	 * 格式为[ { label: "Choice1", value: "value1" }, ... ]
	 * 如果value为空，格式为[ "Choice1", "Choice2" ]
	 * @param label
	 * @param value
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public JSONArray parseAttributesToAutocomplete(String label,String value,Iterable<T> ts) throws Exception{
		
		if(StringUtils.isBlank(label)){
			throw new CustomRunTimeException("lable cannot be null or ''");
		}
		
		JSONArray jsonArray = new JSONArray();
		for (T t : ts) {
			//如果value为空
			if(StringUtils.isBlank(value)){
				try {
					jsonArray.add(BeanUtils.getNestedProperty(t, label));
				} catch (NestedNullException e) {
					//发生这个异常表示找不到attribute，或者attribute的值为null
					continue;
				}
			}else{
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("label", BeanUtils.getNestedProperty(t, label));
				} catch (NestedNullException e) {
					//发生这个异常表示找不到attribute，或者attribute的值为null
					jsonObject.put("label", null);
				}
				
				try {
					jsonObject.put("value", BeanUtils.getNestedProperty(t, value));
				} catch (NestedNullException e) {
					//发生这个异常表示找不到attribute，或者attribute的值为null
					jsonObject.put("value", null);
				}
				
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}
	
}

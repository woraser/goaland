package com.anosi.asset.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import groovy.json.JsonOutput;

/***
 * 
 * @author jinyao
 *
 * @param <T>
 */
@Component("jqgridUtil")
public class JqgridUtil<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(JqgridUtil.class);

	/***
	 * 将page对象转换成可以被jqgrid加载的json字符串
	 * @param pages
	 * @param rowId 每行的id属性
	 * @param attributes row的每一行要展示的属性名字
	 * @return
	 * @throws Exception
	 * 
	 * 格式:{page:"",total:"",records:"",rows:[{id:"",cell:["",""]},{id:"",cell:["",""]}]}
	 * 
	 */
	public JSONObject parsePageToJqgridJson(Page<T> pages,String rowId,String[] showAttributes) throws Exception {
		logger.debug("rowId:{},attributes:{}",rowId,showAttributes);
		
		JSONObject jsonObject = new JSONObject();
		// 设置page
		jsonObject.put("page", pages.getNumber());
		// 设置total
		jsonObject.put("total", pages.getTotalPages());
		// 设置record
		jsonObject.put("records", pages.getTotalElements());
		// 设置rows
		jsonObject.put("rows", parseJsonArrayWithAttributes(pages.getContent(),rowId, showAttributes));
		
		logger.debug("json for jqgrid:{}",JsonOutput.prettyPrint(jsonObject.toString()));
		
		return jsonObject;
	}
	
	private JSONArray parseJsonArrayWithAttributes(List<T> datas,String rowId,String[] attributes) throws Exception{
		JSONArray jsonArray = new JSONArray();
		for (T t : datas) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", PropertyUtil.getNestedProperty(t, rowId));
			
			JSONArray attributeArray = new JSONArray();
			//遍历属性
			for (String attribute : attributes) {
				attributeArray.add(PropertyUtil.getNestedProperty(t, attribute));
			}
			jsonObject.put("cell", attributeArray);
			
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	

}

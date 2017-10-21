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
@Component("dataTablesUtil")
public class DataTablesUtil<T> {

	private static final Logger logger = LoggerFactory.getLogger(DataTablesUtil.class);

	/***
	 * 将page对象转换成可以被jqgrid加载的json字符串
	 * 
	 * @param pages
	 * @param attributes
	 *            row的每一行要展示的属性名字
	 * @return
	 * @throws Exception
	 * 
	 *             格式:{recordsTotal:"",recordsFiltered:"",data:[{"DT_RowId":"",
	 *             k1:v1,....},]
	 * 
	 */
	public JSONObject parsePageToDataTablesJson(Page<T> pages, String rowId, String[] showAttributes) throws Exception {
		logger.debug("rowId:{},attributes:{}",rowId,showAttributes);

		JSONObject jsonObject = new JSONObject();
		// 设置recordsTotal
		jsonObject.put("recordsTotal", pages.getTotalElements());
		// 设置recordsFiltered
		jsonObject.put("recordsFiltered", pages.getTotalElements());
		// 设置rows
		jsonObject.put("rows", parseJsonArrayWithAttributes(pages.getContent(), rowId, showAttributes));

		logger.debug("json for dataTables:{}", JsonOutput.prettyPrint(jsonObject.toString()));

		return jsonObject;
	}
	
	private JSONArray parseJsonArrayWithAttributes(List<T> datas,String rowId,String[] attributes) throws Exception{
		JSONArray jsonArray = new JSONArray();
		for (T t : datas) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("DT_RowId", PropertyUtil.getNestedProperty(t, rowId));
			
			//遍历属性
			for (String attribute : attributes) {
				jsonObject.put("attribute", PropertyUtil.getNestedProperty(t, attribute));
			}
		}
		return jsonArray;
	}

}

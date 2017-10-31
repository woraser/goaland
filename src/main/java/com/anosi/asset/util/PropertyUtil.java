package com.anosi.asset.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/***
 * 对PropertyUtils进行的一些封装改写,和jsonutil配合使用
 * 
 * @author jinyao
 * @see org.apache.commons.beanutils.PropertyUtils
 */
public class PropertyUtil extends PropertyUtils {

	/***
	 * 加入了对date的转换以及对异常的处理,这些异常不应让方法停止
	 * 
	 * @param bean
	 * @param name
	 *            属性名
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public static Object getNestedProperty(final Object bean, final String name)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object property = null;
		if (name.endsWith("*")) {
			// 如果以"*"结果,代表获取这个bean下的所有属性
			// 首先获取t中以attribute为名的属性
			String propertyName = name.substring(0, name.length() - 1);
			Object value = PropertyUtils.getNestedProperty(bean, propertyName);
			property = JSON.parseObject(JSON.toJSONString(value));
		} else {
			property = getPropertyByName(bean, name);
		}
		if (property instanceof Date) {
			Date date = (Date) property;
			return DateFormatUtil.getFormateDate(date);
		} else if (property instanceof List) {
			return parseListToJson((List<Object>) property, name.split("\\*.", 2));
		}
		return property;
	}

	/***
	 * 获取list中的属性
	 * 
	 * @param propertys
	 * @param names
	 * @return
	 */
	private static Object parseListToJson(List<Object> propertys, String[] names)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// 判断name[1]是否是(id,name)这种形式,如果有括号,去掉括号
		String value = names[1];
		if (value.startsWith("(") && value.endsWith(")")) {
			value = value.substring(1, value.length() - 1);
		}
		JSONArray jsonArray = new JSONArray();
		for (Object object : propertys) {
			JSONObject jsonObject = new JSONObject();
			String[] subNames = StringUtil.splitAttributes(value);
			for (String subName : subNames) {
				jsonObject.put(subName, PropertyUtil.getNestedProperty(object, subName));
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	private static Object getPropertyByName(Object bean, String name)
			throws IllegalAccessException, InvocationTargetException {
		Object property = null;
		// 作用2-1次,相当于list*.id会被分割成list,id
		// list*.(id,name,list*.(id,name))分割成[list,(id,name,list*.(id,name))]
		String[] names = name.split("\\*.", 2);
		try {
			property = PropertyUtils.getNestedProperty(bean, names[0]);
		} catch (NestedNullException e) {
			// 发生这个异常表示找不到attribute，或者attribute的值为null
			return null;
		} catch (NoSuchMethodException e) {
			// 发生这个异常表示没有这个属性
			return "there is no property with name:" + names[0];
		}
		return property;
	}

	/***
	 * 将获取的到的值返回toString,如果是嵌套属性,则将值拼接
	 * 
	 * @param bean
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getNestedPropertyString(final Object bean, final String name)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object property = getPropertyByName(bean, name);
		if (property == null) {
			return "";
		}
		if (property instanceof Date) {
			Date date = (Date) property;
			return DateFormatUtil.getFormateDate(date);
		} else if (property instanceof List) {
			return parseListToString((List<Object>) property, name.split("\\*.", 2));
		}
		return property.toString();
	}

	/****
	 * 将list的属性值拼接
	 * 
	 * @param propertys
	 * @param names
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private static String parseListToString(List<Object> propertys, String[] names)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// 判断name[1]是否是(id,name)这种形式,如果有括号,去掉括号
		String value = names[1];
		if (value.startsWith("(") && value.endsWith(")")) {
			value = value.substring(1, value.length() - 1);
		}
		StringBuilder sb = new StringBuilder();
		for (Object object : propertys) {
			String[] subNames = StringUtil.splitAttributes(value);
			for (String subName : subNames) {
				sb.append(PropertyUtil.getNestedPropertyString(object, subName) + "\t");
			}
		}
		return sb.toString();
	}

}

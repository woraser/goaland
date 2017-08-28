package com.anosi.asset.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

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
		String[] names = name.split("-", 2);// 作用2-1次
		try {
			property = PropertyUtils.getNestedProperty(bean, names[0]);
		} catch (NestedNullException e) {
			// 发生这个异常表示找不到attribute，或者attribute的值为null
			return null;
		} catch (NoSuchMethodException e) {
			// 发生这个异常表示没有这个属性
			return "there is no property with name:" + names[0];
		}
		if (property instanceof Date) {
			Date date = (Date) property;
			return DateFormatUtil.getFormateDate(date);
		} else if (property instanceof List) {
			return parseListToJson((List<Object>) property, names);
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
		JSONArray jsonArray = new JSONArray();
		for (Object object : propertys) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(names[1], PropertyUtil.getNestedProperty(object, names[1]));
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

}

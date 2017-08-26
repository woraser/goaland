package com.anosi.asset.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

/***
 * 对PropertyUtils进行的一些封装改写
 * 
 * @author jinyao
 * @see org.apache.commons.beanutils.PropertyUtils
 */
public class PropertyUtil extends PropertyUtils {

	/***
	 * 加入了对date的转换以及对异常的处理,这些异常不应让方法停止
	 * @param bean
	 * @param name
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object getNestedProperty(final Object bean, final String name)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object property = null;
		try {
			property = PropertyUtils.getNestedProperty(bean, name);
		} catch (NestedNullException e) {
			// 发生这个异常表示找不到attribute，或者attribute的值为null
			return null;
		} catch (NoSuchMethodException e) {
			// 发生这个异常表示没有这个属性
			return "there is no property with name:"+name;
		} 
		if (property instanceof Date) {
			Date date = (Date) property;
			return DateFormatUtil.getFormateDate(date);
		}
		return property;
	}

}

package com.anosi.asset.i18n;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nComponent {
	
	@Autowired
	protected MessageSource messageSource;
	
	Locale locale = LocaleContextHolder.getLocale();
	
	/***
	 * 根据key在相应i18n文件中查询
	 * @param key
	 * @return
	 */
	public String getMessage(String key){
		return messageSource.getMessage(key, null,locale);
	}
	
	/***
	 * 根据key在相应i18n文件中查询
	 * @param key
	 * @param defaultMessage 没有找到相应的value时返回defaultMessage
	 * @return
	 */
	public String getMessage(String key,String defaultMessage){
		return messageSource.getMessage(key, null, defaultMessage, locale);
	}
	
}

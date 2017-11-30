package com.anosi.asset.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

@Caching(  
		evict  = {  
				@CacheEvict(value = "device", key = "#device.id+'ForDevice'"),  
				@CacheEvict(value = "device", key = "#device.serialNo+'ForDevice'"),  
        }  
)  
@Target({ElementType.METHOD, ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Inherited  
public @interface DeviceEvictCache {

}

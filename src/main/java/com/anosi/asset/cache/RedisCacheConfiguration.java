package com.anosi.asset.cache;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/***
 * 使用类注解@CacheConfig,配置该类中会用到的一些共用的缓存配置
 * 
 * @CachePut：这个注释可以确保方法被执行，同时方法的返回值也被记录到缓存中。
 * @Cacheable：当重复使用相同参数调用方法的时候，法本身不会被调用执行，即方法本身被略过了，取而代之的是方法的结果直接从缓存中找到并返回了。 
 * @author jinyao
 *
 */
@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport{

	/***
	 * 采用RedisCacheManager作为缓存管理器
	 * 
	 * @param redisTemplate
	 * @return
	 */
	@Bean
	public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		redisCacheManager.setDefaultExpiration(60*60*24*1);//设置缓存1天后过期
		return redisCacheManager;
	}

	/****
	 * 自动义key的格式
	 * 
	 * @return
	 */
	@Bean
	public KeyGenerator customKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

}

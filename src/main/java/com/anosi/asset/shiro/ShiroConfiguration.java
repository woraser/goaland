package com.anosi.asset.shiro;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/***
 * shiro 配置文件
 * 
 * @author jinyao
 *
 */
@Configuration
public class ShiroConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

	@Bean
	public EhCacheManager getEhCacheManager() {
		EhCacheManager em = new EhCacheManager();
		em.setCacheManagerConfigFile("classpath:shiro/ehcache-shiro.xml");
		return em;
	}

	// 设置加密规则
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(2);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

	@Bean(name = "customRealm")
	public CustomRealm getShiroRealm() {
		// 自定义的realm
		CustomRealm realm = new CustomRealm();
		realm.setCredentialsMatcher(hashedCredentialsMatcher());
		realm.setCacheManager(getEhCacheManager());
		return realm;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/***
	 * shiro aop
	 * 
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(getDefaultWebSecurityManager());
		return new AuthorizationAttributeSourceAdvisor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	public SessionManager getRedisSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(getSessionRedisDao());
		sessionManager.setGlobalSessionTimeout(1000 * 60 * 30);// 毫秒，设置30分钟过期
		return sessionManager;
	}

	@Bean("sessionRedisDao")
	public SessionRedisDao getSessionRedisDao() {
		SessionRedisDao sessionRedisDao = new SessionRedisDao();
		return sessionRedisDao;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager() {
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
		dwsm.setRealm(getShiroRealm());
		// 用户授权/认证信息Cache, 采用EhCache 缓存
		// dwsm.setCacheManager(getEhCacheManager());
		// 注入记住我管理器
		dwsm.setRememberMeManager(rememberMeManager());
		// 用redis来持久化session,由于使用了redis,就没必要在用EhCache的缓存了
		dwsm.setSessionManager(getRedisSessionManager());
		return dwsm;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		Map<String, Filter> map = new HashMap<>();
		map.put("addPrincipal", addPrincipalToSessionFilter());
		shiroFilterFactoryBean.setFilters(map);
		shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());

		Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();

		// 配置记住我或认证通过可以访问的地址
		// filterChainDefinitionManager.put("/", "user");
		// anon 可以理解为不拦截
		filterChainDefinitionManager.put("/logout", "anon");

		/*------------start:需要从配置文件动态读取---------------*/
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties("shiro/filterChainDefinition.properties");
			for (Entry<Object, Object> entry : properties.entrySet()) {
				filterChainDefinitionManager.put((String) entry.getKey(), (String) entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		filterChainDefinitionManager.put("/**", "user");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);
		/*------------end:需要从配置文件动态读取---------------*/

		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/index");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		return shiroFilterFactoryBean;
	}

	@Bean
	public AddPrincipalToSessionFilter addPrincipalToSessionFilter() {
		return new AddPrincipalToSessionFilter();
	}

	/***
	 * 为了在thymeleaf里使用shiro的标签的bean
	 * 
	 * @return
	 */
	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * cookie对象;
	 * 
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		logger.debug("ShiroConfiguration.rememberMeCookie()");
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;
	 * 
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		logger.debug("ShiroConfiguration.rememberMeManager()");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}

}

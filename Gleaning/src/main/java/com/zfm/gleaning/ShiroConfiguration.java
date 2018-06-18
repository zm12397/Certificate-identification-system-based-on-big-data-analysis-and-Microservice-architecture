package com.zfm.gleaning;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zfm.gleaning.pojo.UrlPermissionInitDO;
import com.zfm.gleaning.service.ShiroService;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import com.zfm.gleaning.MyRealm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;

/**
 * Shiro权限配置类
 * @author zm
 *
 */
@Configuration
public class ShiroConfiguration {
	
	@Autowired
	private ShiroService shiroService;

	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		/*
		 * filterChainDefinitionMap.put("/static/**", "anon"); //配置退出
		 * 过滤器,其中的具体的退出代码Shiro已经替我们实现了 filterChainDefinitionMap.put("/logout",
		 * "logout"); //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		 * filterChainDefinitionMap.put("/add", "roles[admin]"); //<!--
		 * authc:所有url都必须登录才可以访问; anon:所有url都都可以匿名访问-->
		 * filterChainDefinitionMap.put("/**", "authc");
		 */
		filterChainDefinitionMap = shiroService.getInitUrlPermission();
		
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");

		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/error/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
//	引入thymeleaf对shiro的标签支持
	@Bean  
	public ShiroDialect shiroDialect() {  
	    return new ShiroDialect();  
	}  
	
	@Bean
	public MyRealm myShiroRealm(HashedCredentialsMatcher matcher) {
		MyRealm myShiroRealm = new MyRealm();
		myShiroRealm.setCredentialsMatcher(matcher);
		return myShiroRealm;
	}

	 /**  
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了  
     * 所以我们需要修改下doGetAuthenticationInfo中的代码; @return  
     */  
    @Bean  
    public HashedCredentialsMatcher hashedCredentialsMatcher() {  
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();  
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");	// 散列算法:这里使用MD5算法;  
        hashedCredentialsMatcher.setHashIterations(2);			// 散列的次数，比如散列两次，相当于md5(md5(""));  
//        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);	//表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64；  
        return hashedCredentialsMatcher;  
    }  
	
	@Bean
	public SecurityManager securityManager(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm
		securityManager.setRealm(myShiroRealm(matcher));
		// 设置
		SecurityUtils.setSecurityManager(securityManager);
		return securityManager;
	}
}

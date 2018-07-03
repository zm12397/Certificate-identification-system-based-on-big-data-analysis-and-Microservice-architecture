package com.zfm.gleaning;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;

import com.zfm.gleaning.service.ZookeeperService;

/**
 * Sprng Boot项目入口函数
 * 
 * @author zm
 *
 */
@SpringBootApplication
@EnableScheduling
//@EnableJpaRepositories(basePackages = "com.zfm.gleaning.dao", repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
public class App extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		/*String result = null;
		result = new SimpleHash("MD5","123",ByteSource.Util.bytes("admin5ZM"),2).toHex();
		System.out.println(result);*/
		/*ZookeeperService zs = context.getBean(ZookeeperService.class);
		zs.getJWProvice();*/
	}

}

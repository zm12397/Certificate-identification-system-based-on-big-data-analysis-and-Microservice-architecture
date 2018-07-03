package com.zfm.mydfs.gleaning;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;

import com.zfm.mydfs.gleaning.dao.impl.SendbackDaoImpl;
import com.zfm.mydfs.gleaning.service.AnysisService;
import com.zfm.mydfs.gleaning.service.GenerateService;
import com.zfm.mydfs.gleaning.service.JWGenerateService;
import com.zfm.mydfs.gleaning.service.TaskService;

/**
 * Sprng Boot项目入口函数
 * 
 * @author zm
 *
 */
@SpringBootApplication
@EnableScheduling
public class App{
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		TaskService taskService = context.getBean(TaskService.class);
		taskService.init();
		/*JWGenerateService s = context.getBean(JWGenerateService.class);
		s.generate();*/
	}

}

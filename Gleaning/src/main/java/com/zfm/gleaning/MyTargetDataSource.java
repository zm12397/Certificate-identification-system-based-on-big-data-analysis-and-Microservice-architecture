package com.zfm.gleaning;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于区分数据源调用
 * @author zm
 *
 */
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface MyTargetDataSource {
	//数据源
	MyDataSourceType value() default MyDataSourceType.SLAVE;
	enum MyDataSourceType{
		MASTER,SLAVE
	}
}

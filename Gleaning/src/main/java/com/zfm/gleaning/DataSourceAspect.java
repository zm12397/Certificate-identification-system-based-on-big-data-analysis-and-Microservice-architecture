package com.zfm.gleaning;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.Signature;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据源切面类
 * @author zm
 *
 */
@Aspect
@Component
@Slf4j
public class DataSourceAspect {
	
	/**
	 * 功能：切service包下所有方法，根据方法上或类型上的注解来判断是走主库还是从库
	 * @param joinPoint
	 * @param myTargetDataSource
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.zfm.gleaning.service.*.*(..)) && ( @target(myTargetDataSource) || @annotation(myTargetDataSource) )")
	public Object aroundByTransactional(ProceedingJoinPoint joinPoint,MyTargetDataSource myTargetDataSource) throws Throwable{
		Signature sig = joinPoint.getSignature();
		String method = sig.getName();
		log.info("方法名："+sig.getName());
		myTargetDataSource = myTargetDataSource == null ? joinPoint.getTarget().getClass().getAnnotation(MyTargetDataSource.class) : myTargetDataSource;
		if(myTargetDataSource != null){
			if(myTargetDataSource.value() == MyDataSourceType.SLAVE){
				log.info("切换到: slave");
				DynamicDataSourceHolder.setDataSource("readDruidDataSource");
			}else if(myTargetDataSource.value() == MyDataSourceType.MASTER){
				log.info("切换到: master");
				DynamicDataSourceHolder.setDataSource("writeDruidDataSource");
			}
		}
		Object res = joinPoint.proceed(joinPoint.getArgs());
		return res;
	}
}

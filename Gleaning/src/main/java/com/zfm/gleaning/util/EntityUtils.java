package com.zfm.gleaning.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

public class EntityUtils {
	public static void copyProperties(Object source, Object target, boolean ignoreNullProperties,
			boolean ignoreCollection, String... ignoreProperties) throws IntrospectionException {
		Class<?> targetClass = target.getClass(); // 先获取目标类型
		// 将可变的忽略参数封装成集合
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		for (Field field : targetClass.getDeclaredFields()) { // 根据字段开始遍历
//			System.out.println(field.getName());
			// 每个字段根据名称和目标对象生成该字段的属性描述器，该描述器封装的属性的getter（read）、setter（write）方法
			PropertyDescriptor writePropertyDescriptor = new PropertyDescriptor(field.getName(), targetClass);
			// 先获取目标对象的写方法（setter）
			Method writeMethod = writePropertyDescriptor.getWriteMethod();
			// 如果写方法存在，并且该字段不被忽略
			if (writeMethod != null
					&& (ignoreProperties == null || (!ignoreList.contains(writePropertyDescriptor.getName())))) {
				// 生成源对象的该字段的属性描述器
				PropertyDescriptor sourcePropertyDescriptor = new PropertyDescriptor(writePropertyDescriptor.getName(),
						source.getClass());
				// 获取源对象的读方法（getter）
				Method readMethod = sourcePropertyDescriptor.getReadMethod();
				// 如果读方法存在，并且源对象和目标对象的该属性相对应，即源对象的getter返回类型与目标对象的setter参数类型相同
				if (readMethod != null
						&& ClassUtils.isAssignable(readMethod.getReturnType(), writeMethod.getParameterTypes()[0])) {
					try {
						// 先用修饰符工具类判断读方法的修饰符是否是公共级别
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							// 如果不是公共级别的访问修饰符，则需要先设置为可访问
							readMethod.setAccessible(true);
						}
						// 根据读方法反射获取源对象的该属性的值
						Object value = readMethod.invoke(source);
						// 如果忽略集合并且读方法的返回类型为集合，则跳过该字段
						if (ignoreCollection && Collection.class.isAssignableFrom(readMethod.getReturnType())) {
							continue;
						}
						// 如果忽略空值并且该值为空，也跳过该字段
						if (ignoreNullProperties && value == null) {
							continue;
						}
						// 在用访问修饰符工具类判断目标对象写方法的声明类型的访问级别
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							// 如果不是公有的，则设置为可访问
							writeMethod.setAccessible(true);
						}
						// 用目标对象的写方法将属性值赋给目标对象
						writeMethod.invoke(target, value);
//						System.out.println(value);
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy property '" + writePropertyDescriptor.getName()
								+ "' from source to target", ex);
					}
				}
			}
		}
	}
}

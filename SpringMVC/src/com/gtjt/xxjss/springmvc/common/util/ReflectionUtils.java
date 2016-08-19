/*
 * @ReflectionUtils.java
 *
 * Copyright (c) 2011-2011 MOR, Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of MOR,
 * Inc. ("Confidential Information").  You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with MOR.
 */

package com.gtjt.xxjss.springmvc.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * ReflectionUtils Description.
 * 反射工具类.
 * 
 * 提供访问私有变量,获取泛型类型Class, 提取集合中元素的属性, 转换字符串到对象等Util函数.
 * import from springside.org.cn @author calvin
 * @version Change History:
 * @version  <1> Eric (Oct 17, 2011) create this file. 
 * @version  <1> Eric (2012/04/24) update this file for logging. 
 */
public class ReflectionUtils {

	private static Logger logger = Logger.getLogger(ReflectionUtils.class);

	/**
	 * 调用Getter方法.
	 */
	public static Object invokeGetterMethod(Object obj, String propertyName) {
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法.使用value的Class来查找Setter方法.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		invokeSetterMethod(obj, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 * 
	 * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e);
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Assert.notNull(obj, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况.
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Assert.notNull(obj, "object不能为空");

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;

			} catch (NoSuchMethodException e) {//NOSONAR
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.trace(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.trace("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.trace(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
	
	/**
	 * 拷贝更新Bean对象
	 * 如果传入的属性为空，则不更新Bean对应的属性
	 * 
	 * @param orig 传入的对象
	 * @param dest 更新的对象
	 */
	public static void updateBeanData(Object orig, Object dest) {
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(orig);

		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue;
			}

			try {
				Object value = getFieldValue(orig, name);
				setFieldValue(dest, name, value);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("setFieldValue throw Exception",e);
			}
		}
	}
	
	/**
	 * 
	* @Title: updateBeanData
	* @Description: 拷贝指定字段数据
	* @param orig
	* 	数据源
	* @param dest
	* 	目的源
	* @param fields
	* 	字段数组
	* @throws
	 */
	public static void updateBeanData(Object orig, Object dest, String[] fields) {
		
		for(String field : fields){
			try {
				Object value = getFieldValue(orig, field);
				if (value != null)
					setFieldValue(dest, field, value);
			} catch (Exception e) {
				logger.error("setFieldValue throw Exception",e);
			}
		}
		
	}
	/**
	 * 拷贝更新Bean对象
	 * 如果传入的属性为空，则不更新Bean对应的属性
	 * @param orig 传入的对象
	 * @param dest 更新的对象
	 */
	public static void updateBeanDataNull(Object orig, Object dest) {
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(orig);

		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue;
			}

			try {
				Object value = getFieldValue(orig, name);
				if (value != null)
					setFieldValue(dest, name, value);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("setFieldValue throw Exception",e);
			}
		}
	}
	/**
	 * @param orig
	 * @param dest
	 * 拷贝属性
	 * 拷贝相同name的属性
	 */
	public static void updateDifferentBeanData(Object orig, Object dest) {
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(orig);

		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue;
			}

			try {
				Object value = getFieldValue(orig, name);
				if (value != null){
//					setFieldValue(dest, name, value);
					Field field = getAccessibleField(dest, name);
					//目标对象没有此属性 忽略
					if (field == null)
						continue;
					try {
						field.set(dest,value);
					} catch (IllegalAccessException e) {
						logger.error("不可能抛出的异常:{}", e);
						
					}
				}
			} catch (Exception e) {
				logger.error("异常", e);
				//e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 填充Bean对象
	 * 从request中获取值填充bean对象
	 * 
	 * @param request HttpServletRequest的对象
	 * @param bean 要填充的对象
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void fillBeanData(HttpServletRequest request, Object bean) {
		Class c = bean.getClass();
        Method[] ms = c.getMethods();
        String prop = "";
        String param = null;
        for(int i=0; i<ms.length;i++){
        	String name = ms[i].getName();
            if(name.startsWith("set")) {
                Class[] cc = ms[i].getParameterTypes();
                if(cc.length==1) {
                    String type = cc[0].getName();
                    try {
                        // get property name:
                        prop = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                        // get parameter value:
                        param = request.getParameter(prop);
                        if(param!=null && !param.equals("")) {
                            //ms[i].setAccessible(true);
                            if(type.equals("java.lang.String")) {
                                ms[i].invoke(bean, new Object[] {param});
                            }
                            else if(type.equals("int") || type.equals("java.lang.Integer")) {
                                ms[i].invoke(bean, new Object[] {new Integer(param)});
                            }
                            else if(type.equals("long") || type.equals("java.lang.Long")) {
                                ms[i].invoke(bean, new Object[] {new Long(param)});
                            }
                            else if(type.equals("boolean") || type.equals("java.lang.Boolean")) {
                                ms[i].invoke(bean, new Object[] { Boolean.valueOf(param) });
                            }
                            else if(type.equals("java.util.Date")) {
                                Date date = DateUtils.getDate(DateUtils.sdfDate2, param);
                                if(date==null)
                                	date = DateUtils.getDate(DateUtils.sdfDate0, param);
                                if(date==null)
                                	date = DateUtils.getDate(DateUtils.sdfDate1, param);
                                if(date==null)
                                	date = DateUtils.getDate(DateUtils.sdfDate11, param);
                                
                                if(date!=null)
                                    ms[i].invoke(bean, new Object[] {date});
                            }
                        }
                    }
                    catch(Exception e) {
                	logger.error("Set field failed in Field ["+prop+"] Value ["+param+"].");
//                        e.printStackTrace();
                    }
                }
            }
        }
	}
	
	
	/**
	 * 获取对象的json格式（属性：值）
	 * @param arg：对象类
	 * @param obj: Object
	 * @param retMap：返回map
	 * @param restriction：是否添加限制
	 * @return
	 * @author 张斌
	 */
	public static <T> Map getObjectProperties2Map(	Class arg,
																		T obj,
																		Map<String, Object> retMap,
																		boolean restriction){
																
		Field[] fields = arg.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			if(getFieldValue(obj,fields[i].getName())!=null &&
					!"".equals(getFieldValue(obj,fields[i].getName()))){
				retMap.put(fields[i].getName(), getFieldValue(obj,fields[i].getName()));
			}
			if(restriction){
				if("flag".equals(fields[i].getName()))
					break;
			}
		}
		return retMap;
	}
	
	/**
	 * Gets the class. 获取类的class
	 * 
	 * @param clazzName
	 *            the clazz name
	 * @return the class
	 */
	public static Class getClass(String clazzName){
		try {
			return Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException",e);
		}
		return null;
	}
	
	public static Object[] getFieldValue(final Object obj, final String[] fieldNames) {
		Object[] data = new Object[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			data[i] = getFieldValue(obj, fieldNames[i]);
		}
		return data;
	}
}

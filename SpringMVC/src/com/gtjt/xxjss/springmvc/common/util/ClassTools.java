package com.gtjt.xxjss.springmvc.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/*
 * 类处理工具
 */
public class ClassTools {
	private static Logger logger = Logger.getLogger(ClassTools.class);

	/**
	 * @description from List<Object> to List<Map<String,Object>>
	 * @author Eric (Oct 19, 2011)
	 * 
	 * @version Change History:
	 * @version <1> Oct 19, 2011 Eric Modification purpose.
	 */
	public static List<Map<String, Object>> toListOfMap(List<Object> objs) {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		boolean wrong = false;
		for (Object object : objs) {
			try {
				results.add(getMapOfClass(object));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				wrong = true;
				break;

			} catch (IllegalAccessException e) {
				wrong = true;
				e.printStackTrace();
				break;
			} catch (InvocationTargetException e) {
				wrong = true;
				e.printStackTrace();
				break;
			}
		}
		if (wrong)
			return new ArrayList<Map<String, Object>>();
		else
			return results;
	}

	/*
	 * 参数为一个model对象，返回有对象属性组成的map
	 */
	public static Map<String, Object> getMapOfClass(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		Map<String, Object> map = new HashMap<String, Object>();
//		Field[] fields = o.getClass().getDeclaredFields();
//		Method[] methods = o.getClass().getDeclaredMethods();
//		for (Method method : methods) {
//			// 如果是get方法，并且没有参数
//			if (method.getName().matches("^get.*") && !method.isVarArgs()) {
//				map.put(StringUtils.toLowerOfFirstChar(method.getName().substring(3)), method.invoke(o, null));
//			}
//		}
//		return map;
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			Object fieldValue = ReflectionUtils.getFieldValue(obj, fields[i].getName());
			map.put(fields[i].getName(), fieldValue);
		}
		return map;
	}

	/**
	 * To map.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return the map
	 * @author Eric Nov 29, 2011
	 */
	public static <T> Map<String, Object> toMap(T object) {
		try {
			return getMapOfClass(object);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map buildClassMap(Object obj) {
		Map returnMap = new HashMap();

		buildClassMap(obj, obj.getClass(), returnMap);

		return returnMap;
	}

	@SuppressWarnings("unchecked")
	private static void buildClassMap(Object obj, Class arg, Map map) {
		if (arg == null) {
			return;
		}

		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true); // 取得对private属性的访问权限

				String attrName = fields[i].getName();

				Object val = fields[i].get(obj);
				if (val instanceof java.util.Collection) {
					val = val.toString();
				}

				map.put(attrName, val);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		buildClassMap(obj, arg.getSuperclass(), map);
	}

	/**
	 * 
	 * @param bean
	 * @param map 
	 */
	public static void buildMapBean(Object bean, Map<String,Object> map){
		if (bean==null ||  map==null || map.size()==0) {
			return;
		}
		
		Map<String,Object> newMap = new HashMap<String,Object>();
		Set<String> keys = map.keySet();
		for(String key:keys){
			Object value = map.get(key);
//			newMap.put(key.replace("_", "").toLowerCase(), value);
			newMap.put(key.toLowerCase(), value);
		}
		
		String prop = "";
		Object param = null;
		try {
			Class cls = bean.getClass();
	        Method[] ms = cls.getMethods();
			for(int i=0; i<ms.length;i++){
		        	String name = ms[i].getName();
		            if(name.startsWith("set")) {
		                Class[] cc = ms[i].getParameterTypes();
		                if(cc.length==1) {
		                    String type = cc[0].getName();
		                    try {
		                        // get property name:
		                        prop = Character.toLowerCase(name.charAt(3)) + name.substring(4).toLowerCase();
		                        // get parameter value:
		                        param = newMap.get(prop);
		                        if(param!=null && !param.equals("")) {
		                            //ms[i].setAccessible(true);
		                            if(type.equals("java.lang.String")) {
//		                                ms[i].invoke(bean, new Object[] {param});
		                            	ms[i].invoke(bean, new Object[] {param.toString()});
		                            }
		                            else if(type.equals("int") || type.equals("java.lang.Integer")) {
		                                ms[i].invoke(bean, new Object[] {Integer.parseInt(param.toString())});
		                            }
		                            else if(type.equals("long") || type.equals("java.lang.Long")) {
		                                ms[i].invoke(bean, new Object[] {Long.parseLong(param.toString())});
		                            }
		                            else if(type.equals("double") || type.equals("java.lang.Double")) {
		                                ms[i].invoke(bean, new Object[] {Double.parseDouble(param.toString())});
		                            }
		                            else if(type.equals("boolean") || type.equals("java.lang.Boolean")) {
		                                ms[i].invoke(bean, new Object[] { Boolean.valueOf(param.toString()) });
		                            }
		                            else if(type.equals("short") || type.equals("java.lang.Short")) {
		                                ms[i].invoke(bean, new Object[] { Short.valueOf(param.toString()) });
		                            }
		                            else if(type.equals("float") || type.equals("java.lang.Float")) {
		                                ms[i].invoke(bean, new Object[] { Float.valueOf(param.toString()) });
		                            }
		                            else if(type.equals("java.util.Date")) {
		                            	
		                                Date date = DateUtils.getdate(param.toString());
		                                
		                                if(date!=null)
		                                    ms[i].invoke(bean, new Object[] {date});
		                            }
		                        }
		                    }
		                    catch(Exception e) {
		                	logger.error("Set field failed in Field ["+prop+"]["+type+"] Value ["+param+"].");
		                    }
		                }
		            }
		        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param bean
	 * @param map 
	 */
	public static void buildRequestBean(Object bean, HttpServletRequest request){
		if (bean==null) {
			return;
		}
		
		
		String prop = "";
		Object param = null;
		try {
			Class cls = bean.getClass();
	        Method[] ms = cls.getMethods();
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
		                                ms[i].invoke(bean, new Object[] {Integer.parseInt(param.toString())});
		                            }
		                            else if(type.equals("long") || type.equals("java.lang.Long")) {
		                                ms[i].invoke(bean, new Object[] {Long.parseLong(param.toString())});
		                            }
		                            else if(type.equals("double") || type.equals("java.lang.Double")) {
		                                ms[i].invoke(bean, new Object[] {Double.parseDouble(param.toString())});
		                            }
		                            else if(type.equals("boolean") || type.equals("java.lang.Boolean")) {
		                                ms[i].invoke(bean, new Object[] { Boolean.valueOf(param.toString()) });
		                            }
		                            else if(type.equals("short") || type.equals("java.lang.Short")) {
		                                ms[i].invoke(bean, new Object[] { Short.valueOf(param.toString()) });
		                            }
		                            else if(type.equals("float") || type.equals("java.lang.Float")) {
		                                ms[i].invoke(bean, new Object[] { Float.valueOf(param.toString()) });
		                            }
		                            else if(type.equals("java.util.Date")) {
		                            	
		                                Date date = DateUtils.getdate(param.toString());
		                                
		                                if(date!=null)
		                                    ms[i].invoke(bean, new Object[] {date});
		                            }
		                        }
		                    }
		                    catch(Exception e) {
		                	logger.error("Set field failed in Field ["+prop+"]["+type+"] Value ["+param+"].");
		                    }
		                }
		            }
		        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

	}
	
}

package xlo.util.reflect;

import xlo.util.StringUtil;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author XiaoLOrange
 * @time 2020.11.03
 * @title
 */

public class FindMethodUtil {

	/**
	 * 在不知道参数的情况下
	 * 获取一个指定方法名的方法
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getMethod(Class clazz, String name){
		Method[] ms = clazz.getDeclaredMethods();

		for (int i = 0; i < ms.length; i++){
			if(ms[i].getName().equals(name)) return ms[i];
		}

		return null;
	}

	/**
	 * 获取具有指定参数的方法
	 * @param clazz
	 * @param name
	 * @param pts
	 * @return
	 */
	public static Method getMethod(Class clazz, String name, Class... pts){
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(name, pts);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * 在不知道参数的情况下
	 * 获取指定方法名的所有方法
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method[] getMethods(Class clazz, String name){
		Method[] ms = clazz.getDeclaredMethods();
		ArrayList<Method> preEcho = new ArrayList<>();

		for (int i = 0; i < ms.length; i++){
			if(ms[i].getName().equals(name)) preEcho.add(ms[i]);
		}
		Method[] echo = new Method[preEcho.size()];
		preEcho.toArray(echo);

		return echo;
	}

	/**
	 * 获取具有指定参数的set方法
	 * @param clazz
	 * @param field
	 * @param pts paramTypes
	 * @return
	 */
	public static Method getSetMethod(Class clazz, Field field, Class... pts){
		String name = "set" + StringUtil.toUpperFirstCase(field.getName());
		return getMethod(clazz, name, pts);
	}

	/**
	 * 获取clazz中field字段的set方法
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method[] getSetMethods(Class clazz, Field field){
		String name = "set" + StringUtil.toUpperFirstCase(field.getName());
		Method[] ms = getMethods(clazz, name);
		return ms;
	}

	/**
	 * 获取具有指定参数的get方法
	 * @param clazz
	 * @param field
	 * @param pts
	 * @return
	 */
	public static Method getGetMethod(Class clazz, Field field, Class... pts){
		String name = "get" + StringUtil.toUpperFirstCase(field.getName());
		return getMethod(clazz, name, pts);
	}

	/**
	 * 获取clazz中field字段的get方法
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method[] getGetMethods(Class clazz, Field field){
		String name = "get" + StringUtil.toUpperFirstCase(field.getName());
		Method[] ms = getMethods(clazz, name);
		return ms;
	}

}

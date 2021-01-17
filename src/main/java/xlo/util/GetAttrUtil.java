package xlo.util;

import java.lang.reflect.Field;

/**
 * @author XiaoLOrange
 * @time 2020.10.29
 * @title 获取类中指定的属性
 */

public class GetAttrUtil {

	/**
	 * 获取类中指定字段的属性值
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object get(Object obj, String field) {
		Field fild = null;
		try{
			fild = obj.getClass().getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return get(obj, fild);
	}

	/**
	 * 获取类中指定字段的属性值
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object get(Object obj, Field field){
		if(obj == null || field == null) throw new NullPointerException();
		Object echo = null;
		try{
			field.setAccessible(true);
			echo = field.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return echo;
	}

}

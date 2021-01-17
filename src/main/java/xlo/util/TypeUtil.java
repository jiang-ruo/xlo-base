package xlo.util;

/**
 * @author XiaoLOrange
 * @time 2020.12.02
 * @title
 */

public class TypeUtil {

	public static boolean isNum(Class clazz){
		return (isNumPacking(clazz)
		|| clazz == byte.class
		|| clazz == short.class
		|| clazz == int.class
		|| clazz == long.class
		|| clazz == float.class
		|| clazz == double.class);
	}

	/**
	 * 判断传入的类型是否是数字类型的包装类
	 * @param clazz
	 * @return
	 */
	public static boolean isNumPacking(Class clazz){
		return (clazz == Byte.class
		|| clazz == Short.class
		|| clazz == Integer.class
		|| clazz == Long .class
		|| clazz == Float.class
		|| clazz == Double.class);
	}

	/**
	 * 判断是否是基本数据类型的包装类
	 * @param clazz
	 * @return
	 */
	public static boolean isBasePacking(Class clazz){
		return (isNumPacking(clazz)
		|| clazz == Character.class
		|| clazz == Boolean.class);
	}

	/**
	 * 判断是否是包装类
	 * @param clazz
	 * @return
	 */
	public static boolean isPacking(Class clazz){
		return (isBasePacking(clazz) || clazz == String.class);
	}

}

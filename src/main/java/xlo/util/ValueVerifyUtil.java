package xlo.util;

import java.math.BigDecimal;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title 值校验工具
 */

public class ValueVerifyUtil {

	/**
	 * 判断传入的值是否是该类型的初始值
	 * boolean - false;
	 * 其它基本数据类型是0;
	 * 其它类型是null
	 * @param clazz
	 * @param value
	 * @return 全部是初始值返回true,
	 */
	public static boolean isInitial(Class clazz, Object value){
		boolean rs = true;
		if(clazz.isPrimitive()){
			//是基本数据类型
			if(clazz == boolean.class){
				rs = value.equals(false);
			}else{
				if(clazz == char.class){
					rs = (byte)(char)value == 0;
				}else if(clazz == byte.class){
					rs = (byte)value == 0;
				}else if(clazz == short.class){
					rs = (short)value == 0;
				}else if(clazz == int.class){
					rs = (int)value == 0;
				}else if(clazz == long.class){
					rs = (long)value == 0;
				}else if(clazz == float.class){
					rs = (float)value == 0;
				}else if(clazz == double.class){
					rs = (double)value == 0;
				}
			}
		}else{
			rs = value == null;
		}
		return rs;
	}

	/**
	 * 获取传入数据类型的初始值
	 * @param clazz
	 * @return
	 */
	public static Object getInitial(Class clazz){
		//不是基本数据类型返回null
		if(!clazz.isPrimitive()) return null;

		//是基本数据类型
		if(clazz == boolean.class) return false;

		return 0;
	}

	/**
	 * 传入的值全部为非null
	 * @param objs
	 * @return
	 */
	public static boolean notNull(Object... objs){
		for (int i = 0; i < objs.length; i++){
			if(objs == null) return false;
		}
		return true;
	}

}

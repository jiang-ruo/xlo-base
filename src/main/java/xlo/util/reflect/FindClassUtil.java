package xlo.util.reflect;

/**
 * @author XiaoLOrange
 * @time 2020.12.27
 * @title
 */

public class FindClassUtil {
	/**
	 * 一直向上查找，
	 * 寻找传入的类clazz是否存在指定的父类，
	 * @param clazz
	 * @return
	 */
	public static boolean hasFather(Class clazz, Class father){
		//获取clazz的父类
		clazz = clazz.getSuperclass();
		while (clazz != null){
			if(clazz == father) return true;
			clazz = clazz.getSuperclass();
		}
		return false;
	}
}

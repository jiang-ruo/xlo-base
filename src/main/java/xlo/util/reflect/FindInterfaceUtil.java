package xlo.util.reflect;

/**
 * @author XiaoLOrange
 * @time 2021.01.02
 * @title
 */

public class FindInterfaceUtil {
	/**
	 * 一直向上查找，
	 * 寻找传入的类clazz(该类本身的接口或者其父类是否有该接口)是否存在指定的接口
	 * @param clazz
	 * @param interfase
	 * @return
	 */
	public static boolean hasImpl(Class clazz, Class interfase){
		//传入的不是接口直接返回
		if(interfase.getModifiers() != 1537) return false;

		//获取clazz的父类
		while (clazz != null){
			//沿着传入的类，一直向上遍历其父类是否具有指定接口
			if(hasImpl(clazz.getInterfaces(), interfase)) return true;
			//获取父类
			clazz = clazz.getSuperclass();
		}
		return false;
	}

	/**
	 * 传入接口数组，遍历及向上查找是否存在指定的接口
	 * @param interfaces
	 * @param interfase
	 * @return
	 */
	public static boolean hasImpl(Class[] interfaces, Class interfase){
		//传入的不是接口直接返回
		if(interfase.getModifiers() != 1537) return false;
		for (Class iface: interfaces){
			if(iface == interfase) return true;
			if(hasImpl(iface.getInterfaces(), interfase)) return true;
		}
		return false;
	}
}

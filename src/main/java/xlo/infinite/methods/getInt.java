package xlo.infinite.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author XiaoLOrange
 * @time 2020年10月29日
 * @title 直接读取对象obj在偏移量offset位置的一个int值
 */
public abstract class getInt {

	public abstract int readMenory(Object obj, long offset) throws Exception;
	
	public static getInt create() {
		try {
			Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
			Field f = unsafeClass.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			final Object unsafe = f.get(null);
			final Method getInt = unsafeClass.getMethod("getInt", Object.class, long.class);
			return new getInt() {
				public int  readMenory(Object obj, long offset) throws Exception{
					return (int)getInt.invoke(unsafe, obj, offset);
				}
			};
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}

package xlo.infinite.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author XiaoLOrange
 * @title 释放内存，不大清楚怎么用，(传入的long参数好像是指针？)
 * @time 2020年11月2日
 */
public abstract class freeMemory {

	public abstract void free(long address) throws Exception;
	
	public static freeMemory create() {
		try {
			Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
			Field f = unsafeClass.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			final Object unsafe = f.get(null);
			final Method freeMemory = unsafeClass.getMethod("freeMemory", long.class);
			return new freeMemory() {
				public void free(long address) throws Exception{
					freeMemory.invoke(unsafe, address);
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

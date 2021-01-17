package xlo.infinite.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author XiaoLOrange
 * @time 2020年10月29日
 * @title 在对象obj偏移量为offset的位置放入一个int值
 */
public abstract class putInt {

	public abstract void putMemory(Object obj, long offset, int addr) throws Exception;
	
	public static putInt create() {
		try {
			Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
			Field f = unsafeClass.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			final Object unsafe = f.get(null);
			final Method putInt = unsafeClass.getMethod("putInt", Object.class, long.class, int.class);
			return new putInt() {
				public void putMemory(Object obj, long offset, int addr) throws Exception{
					putInt.invoke(unsafe, obj, offset, addr);
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

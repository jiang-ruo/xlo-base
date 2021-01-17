package xlo.infinite.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author XiaoLOrange
 * @time 2020年10月29日
 * @title 直接获取类中的属性，强行获取私有属性
 * 获取字段在内存中的偏移量。返回偏移量,long类型
 */
public abstract class objectFieldOffset {

	public abstract long getOffset(Field field) throws Exception;
	
	public static objectFieldOffset create() {
		try {
			Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
			Field f = unsafeClass.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			final Object unsafe = f.get(null);
			final Method objectFieldOffset = unsafeClass.getMethod("objectFieldOffset", Field.class);
			return new objectFieldOffset() {
				public long getOffset(Field field) throws Exception{
					return (long) objectFieldOffset.invoke(unsafe, field);
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

package xlo.infinite.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author XiaoLOrange
 * @time 2020.10.10
 * @title 通过反射调用创建类(不通过构造方法)
 */

/**
 * 使用方法。
 * ex:创建一个Example类
 * Example obj = XLOUnsafeClazz.create().newInstance(Example.class);
 */

public abstract class allocateInstance {

	public abstract <T> T newInstance(Class<T> clazz) throws Exception;

	public static allocateInstance create() {
		try {
			Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
			Field f = unsafeClass.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			final Object unsafe = f.get((Object)null);
			final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
			return new allocateInstance() {
				public <T> T newInstance(Class<T> c) throws Exception{
					assertInstantiable(c);
					return (T) allocateInstance.invoke(unsafe, c);
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

	static void assertInstantiable(Class<?> c) {
		int modifiers = c.getModifiers();
		if (Modifier.isInterface(modifiers)) {
			throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + c.getName());
		} else if (Modifier.isAbstract(modifiers)) {
			throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + c.getName());
		}
	}

}

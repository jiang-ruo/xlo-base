package xlo.util.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author XiaoLOrange
 * @time 2020.11.30
 * @title
 */

public class AnnoMethod extends AnnoClass{

	private Method method;

	/**
	 *
	 * @param clazz
	 * @param annos 方法的注解
	 * @param method
	 */
	public AnnoMethod(Class clazz, Annotation[] annos, Method method) {
		super(clazz, annos);
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}
}

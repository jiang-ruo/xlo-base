package xlo.util.reflect.annotation;

import java.lang.annotation.Annotation;

/**
 * @author XiaoLOrange
 * @time 2020.11.30
 * @title
 */

public class AnnoClass{

	private Class clazz;
	private Annotation[] annos;

	/**
	 *
	 * @param clazz
	 * @param annos class的注解
	 */
	public AnnoClass(Class clazz, Annotation[] annos) {
		this.clazz = clazz;
		this.annos = annos;
	}

	public Class getClazz() {
		return clazz;
	}

	public Annotation[] getAnnos() {
		return annos;
	}

}

package xlo.util.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author XiaoLOrange
 * @time 2020.11.30
 * @title
 */

public class AnnoField extends AnnoClass{

	private Field field;

	/**
	 *
	 * @param clazz
	 * @param field
	 * @param annos field的注解
	 */
	public AnnoField(Class clazz, Annotation[] annos, Field field) {
		super(clazz, annos);
		this.field = field;
	}

	public Field getField() {
		return field;
	}
}

package xlo.util.reflect.annotation;

import xlo.util.file.JFileUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title
 */

public class AnnoUtil {

	/**
	 * 获取具有指定注解的类
	 * 为空则获取所有带有注解的类
	 * @param annos
	 * @return
	 */
	public static LinkedList<AnnoClass> getClasses(Class... annos){
		LinkedList container = new LinkedList();
		//获取当前工程下的所有.class文件，不包括架包中的文件
		Class[] cs = JFileUtil.getClasses();

		Annotation[] as;
		for (int i = 0; i < cs.length; i++){
			//
			as = getAnno(cs[i], annos);
			if(as.length > 0) container.add(new AnnoClass(cs[i], as));
		}
		return container;
	}

	/**
	 * 获取所有带有指定注解的属性
	 * 为空则获取所有带有注解的属性
	 * @param annos
	 * @return
	 */
	public static LinkedList<AnnoField> getFields(Class... annos){
		LinkedList container = new LinkedList();
		//获取所有的类。
		Class[] cs = JFileUtil.getClasses();

		Class clazz;
		Field[] fs;
		Annotation[] as;
		for (int i = 0; i < cs.length; i++){
			clazz = cs[i];
			//获取类中的所有字段
			fs = clazz.getDeclaredFields();
			for (int n = 0; n < fs.length; n++){
				as = getAnno(fs[i], annos);
				if(as.length > 0) container.add(new AnnoField(clazz, as, fs[i]));
			}
		}
		return container;
	}

	/**
	 * 获取所有指定注解的方法
	 * 为空则获取所有带有注解的方法
	 * @param annos
	 * @return
	 */
	public static LinkedList<AnnoMethod> getMethods(Class... annos){
		LinkedList container = new LinkedList();
		//获取所有的类。
		Class[] cs = JFileUtil.getClasses();

		Class clazz;
		Method[] ms;
		Annotation[] as;
		for (int i = 0; i < cs.length; i++){
			clazz = cs[i];

			//获取类中的所有方法
			ms = clazz.getDeclaredMethods();
			for (int n = 0; n < ms.length; n++){
				as = getAnno(ms[n], annos);
				if(as.length > 0) container.add(new AnnoMethod(clazz, as, ms[n]));
			}
		}
		return container;
	}

	/**
	 * 返回所有注解
	 * @param obj
	 * @return
	 */
	public static Annotation[] getAnno(AnnotatedElement obj){
		Annotation[] as = obj.getDeclaredAnnotations();
		return as;
	}

	/**
	 * 输入一个注解数组，返回obj具有的输入注解中的所有注解
	 * @param obj
	 * @param annos 没有输入则返回所有注解
	 * @return
	 */
	public static Annotation[] getAnno(AnnotatedElement obj, Class... annos){
		if(annos.length == 0) return getAnno(obj);
		ArrayList<Annotation> as = new ArrayList<>(annos.length);
		Annotation anno;
		for (int i = 0; i < annos.length; i++){
			anno = obj.getDeclaredAnnotation(annos[i]);
			if (anno != null) as.add(anno);
		}
		Annotation[] list = new Annotation[as.size()];
		as.toArray(list);
		return list;
	}

}

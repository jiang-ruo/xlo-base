package xlo.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author XiaoLOrange
 * @time 2020.12.27
 * @title 方法具有的参数详情
 */

public class ParamsDetail {

	/**
	 * 方法
	 */
	private Method method;
	/**
	 * 方法具有的参数
	 */
	private Parameter[] params;
	/**
	 * 具体参数的参数类型
	 */
	private Class[] types;
	/**
	 * 参数具有的注解
	 */
	private Annotation[][] annos;

	public ParamsDetail(Method method){
		this.method = method;
		params = method.getParameters();
		types = method.getParameterTypes();
		annos = method.getParameterAnnotations();
	}

	/**
	 * 返回方法参数的参数类型
	 * @return
	 */
	public Class[] getTypes(){
		return types;
	}

	public Method getMethod() {
		return method;
	}
}

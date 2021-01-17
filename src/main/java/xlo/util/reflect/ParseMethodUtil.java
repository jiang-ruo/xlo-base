package xlo.util.reflect;

import java.lang.reflect.Method;

/**
 * @author XiaoLOrange
 * @time 2020.12.27
 * @title
 */

public class ParseMethodUtil {

	/**
	 * 获取方法的详情信息
	 * @param method
	 * @return
	 */
	public static ParamsDetail parse(Method method){
		return new ParamsDetail(method);
	}

}

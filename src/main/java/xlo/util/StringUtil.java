package xlo.util;

import xlo.util.math.RandomUtil;

/**
 * @author XiaoLOrange
 * @time 2020.12.01
 * @title
 */

public class StringUtil {

	/**
	 * 生成指定长度的字符串
	 * @param bits
	 * @return
	 */
	public static String randomString(int bits){
		return RandomUtil.randomString(bits);
	}

	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String toUpperFirstCase(String str){
		char[] cs=str.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

}

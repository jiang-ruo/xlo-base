package xlo.util.math;

import java.text.DecimalFormat;

/**
 * @author XiaoLOrange
 * @time 2020.11.28
 * @title 数字工具类
 */

public class NumUtil {

	/**
	 * 将对象转换为数字，
	 * @param value
	 * @return
	 */
	public static Byte toByte(Object value){
		Long rs = toLong(value);
		return rs == null ? null : rs.byteValue();
	}

	/**
	 * 将对象转换为数字，
	 * @param value
	 * @return
	 */
	public static Short toShort(Object value){
		Long rs = toLong(value);
		return rs == null ? null : rs.shortValue();
	}

	/**
	 * 将对象转换为数字，
	 * @param value
	 * @return
	 */
	public static Integer toInt(Object value){
		Long rs = toLong(value);
		return rs == null ? null : rs.intValue();
	}

	/**
	 * 将对象转换为数字，
	 * 直接转换为长整型，这样子对于长度比long小的数据类型，强转的时候直接截取低位
	 * @param value
	 * @return
	 */
	public static Long toLong(Object value){
		if(value == null) return null;
		//先将输入格式化为整数，防止输入小数点
		String format = null;
		try{
			format = new DecimalFormat("0").format(value);
		}catch (IllegalArgumentException e){
			format = value.toString();
		}
		Long num = null;
		if(value != null){
			try {
				num = Long.valueOf(format);
			}catch (NumberFormatException e){
//				e.printStackTrace();
			}
		}
		return num;
	}

	/**
	 * 将传入的值转换为数字
	 * @param value
	 * @return
	 */
	public static int toNumber(Object value){
		Long rs = toLong(value);
		return rs == null ? 0 : rs.intValue();
	}

	/**
	 * 判断传入的对象是否是数字
	 * @param obj
	 * @return
	 */
	public static boolean isNumber(Object obj){
		return toLong(obj) != null;
	}

	/**
	 * 数字前补充指定字符到指定长度
	 * @param num 数字
	 * @param length 长度
	 * @param charachter 字符
	 * @return
	 */
	public String formatNumber(int num, int length, char charachter){
		StringBuilder format = new StringBuilder(num + "");

		int numLength = format.length();
		if(numLength < length){
			int gap = length - numLength;
			for (int i = 0; i < gap; i++){
				format.insert(0, charachter);
			}
		}

		return format.toString();
	}
}

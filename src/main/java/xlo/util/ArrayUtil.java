package xlo.util;

import java.util.Arrays;

/**
 * @author XiaoLOrange
 * @time 2020.12.03
 * @title
 */

public class ArrayUtil {

	/**
	 * 合并所有数组
	 * @param first
	 * @param rest
	 * @param <T>
	 * @return
	 */
	public static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

}

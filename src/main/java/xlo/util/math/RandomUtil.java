package xlo.util.math;

import java.util.Random;

/**
 * @author XiaoLOrange
 * @time 2020.11.30
 * @title
 */

public class RandomUtil {

	/**
	 * 返回随机数，最小值为min，最大值为max
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max){
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}

	/**
	 * 返回bits个随机数
	 * @param min
	 * @param max
	 * @param bits
	 * @return
	 */
	public static int[] random(int min, int max, int bits){
		int[] echo = new int[bits];
		for (int i = 0; i < bits; i++){
			echo[i] = random(min, max);
		}
		return echo;
	}

	/**
	 * 生成指定长度的字符串
	 * @param bits
	 * @return
	 */
	public static String randomString(int bits){
		StringBuilder container = new StringBuilder();

		int type;
		for(int i = 0; i < bits; i++){
			type = random(0, 2);
			switch (type){
				case 0:
					container.append((char)random(48, 57));
					break;
				case 1:
					container.append((char)random(65, 90));
					break;
				case 2:
					container.append((char)random(97, 122));
					break;
			}
		}
		return container.toString();
	}

}

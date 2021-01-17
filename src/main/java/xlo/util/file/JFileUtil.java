package xlo.util.file;

import java.io.File;
import java.util.LinkedList;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title
 */

public class JFileUtil {

	/**
	 * 将文件夹或文件转换为java的包或类
	 * @param root 根目录
	 * @param floder
	 * @return
	 */
	public static String parse(File root, File floder){
		String rPath = root.getAbsolutePath();
		String prePn = floder.getAbsolutePath();
		prePn = prePn.replace(rPath, "");
		prePn = prePn.replace(".class", "");
		prePn = prePn.replace("\\", ".");
		prePn = prePn.substring(1);
		return prePn;
	}

	/**
	 * 获取工程目录下所有的类
	 * @return
	 */
	public static Class[] getClasses(){
		LinkedList container = new LinkedList();
		File root = FileUtil.getRoot();

		File[] fs = FileUtil.scan(root, new JFileFilter());

		String packageName;
		Class clazz;

		for (int i = 0; i < fs.length; i++){
			packageName = JFileUtil.parse(root, fs[i]);

			//获取类
			try {
				clazz = Class.forName(packageName);
				container.add(clazz);
			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
			}
		}
		Class[] cs = new Class[container.size()];

		container.toArray(cs);
		return cs;
	}

}

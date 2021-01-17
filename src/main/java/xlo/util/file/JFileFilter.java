package xlo.util.file;

import java.io.File;
import java.io.FileFilter;

/**
 * @author XiaoLOrange
 * @time 2020.11.03
 * @title 返回.class文件和文件夹
 */

public class JFileFilter implements FileFilter {
	/**
	 * Tests whether or not the specified abstract pathname should be
	 * included in a pathname list.
	 *
	 * @param pathname The abstract pathname to be tested
	 * @return {@code true} if and only if {@code pathname}
	 * should be included
	 */
	@Override
	public boolean accept(File pathname) {
		if(pathname.isFile()){
			String path = pathname.getPath();
			return path.endsWith(".class");
		}else{
			return true;
		}
	}
}

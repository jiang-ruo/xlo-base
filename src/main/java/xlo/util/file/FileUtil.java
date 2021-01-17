package xlo.util.file;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title 扫描指定文件下的所有目录
 */

public class FileUtil {

	/**
	 * 获取当前工程的根目录
	 */
	public static File getRoot(){

		//获取当前项目的根目录
		URL url = FileUtil.class.getResource("/");
		String path = null;
		try {
			path = URLDecoder.decode(url.getFile(), "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return new File(path);
	}

	/**
	 * 扫描指定文件下的所有文件
	 * @param path
	 * @return
	 */
	public static File[] scan(String path){
		return scan(path, null);
	}

	/**
	 * 扫描文件下的所有文件并使用filter过滤
	 * @param path
	 * @param filter
	 * @return
	 */
	public static File[] scan(String path, FileFilter filter){
		File folder = new File(path);
		return scan(folder, filter);
	}

	/**
	 * 扫描目录下的所有文件
	 * @param folder
	 * @return
	 */
	public static File[] scan(File folder){
		return scan(folder, null);
	}

	/**
	 * 扫描文件下的所有文件并使用filter过滤
	 * @param folder
	 * @param filter
	 * @return
	 */
	public static File[] scan(File folder, FileFilter filter){
		if(!folder.isDirectory()) return null;

		//输出
		LinkedList<File> preEcho = new LinkedList<>();
		//栈
		LinkedList<File> stack = new LinkedList<>();
		stack.push(folder);
		File[] fs;
		File file;
		while (stack.size() > 0){
			file = stack.pop();
			if(file.isFile()){
				//是文件，放入echo中
				preEcho.push(file);
			}else{
				fs = file.listFiles(filter);
				for (int i = 0; i < fs.length; i++){
					stack.push(fs[i]);
				}
			}
		}

		File[] echo = new File[preEcho.size()];
		preEcho.toArray(echo);

		return echo;
	}

}

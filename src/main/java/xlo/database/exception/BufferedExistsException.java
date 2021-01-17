package xlo.database.exception;

/**
 * @author XiaoLOrange
 * @time 2020.11.26
 * @title buffered已存在错误，在bufferedStart后为调用bufferedClose方法，并再次调用bufferedStart方法会抛出该错误
 */

public class BufferedExistsException extends RuntimeException{

	public BufferedExistsException(){
		super();
	}

	public BufferedExistsException(String s){
		super(s);
	}
}

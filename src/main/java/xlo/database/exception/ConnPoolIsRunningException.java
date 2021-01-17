package xlo.database.exception;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title 连接池正在运行
 */

public class ConnPoolIsRunningException extends RuntimeException{

	public ConnPoolIsRunningException(){
		super();
	}

	public ConnPoolIsRunningException(String s){
		super(s);
	}
}

package xlo.database.exception;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title 连接池未准备
 */

public class ConnPoolNotReadyException extends RuntimeException{

	public ConnPoolNotReadyException(){
		super();
	}

	public ConnPoolNotReadyException(String s){
		super(s);
	}
}

package xlo.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author XiaoLOrange
 * @time 2020.10.17
 * @title 为了支持批处理的类
 * 		  没有public,private之类的声明，默认为仅同一个包的类可用。
 */

class BufferedPstmt {

	private Connection conn;
	private PreparedStatement pstmt;

	public BufferedPstmt(Connection conn, PreparedStatement pstmt) {
		this.conn = conn;
		this.pstmt = pstmt;
	}

	void setConn(Connection conn) {
		this.conn = conn;
	}

	Connection getConn() {
		return conn;
	}

	PreparedStatement getPstmt() {
		return pstmt;
	}
}

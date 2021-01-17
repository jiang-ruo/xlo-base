package xlo.database.dao;

import xlo.database.exception.BufferedExistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author XiaoLOrange
 * @time 2020.11.26 BaseDao
 * @time 2020.12.01 BaseDao不能使用单例模式
 * @title
 */

public abstract class BaseDao<T> extends PreBaseDao<T> {

	/**
	 * 用于支持批处理的变量
	 */
	private BufferedPstmt bpstmt = null;

	/**
	 * 初始化批处理缓冲区
	 * 仅支持一条语句批量执行，
	 * 同一语句的不同参数
	 * 从getConn方法中获取conn，因此在调用bufferedClose方法时关闭连接，因此要将conn存入bpstmt中
	 * @param sql
	 * @return
	 */
	public boolean bufferedStart(String sql){
		if(bpstmt != null) throw new BufferedExistsException();

		Connection conn = getConn();
		PreparedStatement pstmt = super.bufferedStart(conn, sql);
		//bufferedStart方法不将conn存入bpstmt中，因此在此将bpstmt存入
		if(pstmt == null) return false;

		bpstmt = new BufferedPstmt(conn, pstmt);
		return true;
	}

	/**
	 * 向缓冲区中添加执行参数
	 * 1次仅能添加1条语句的参数
	 * @param params
	 * @return
	 */
	public boolean BufferedAdd(Object... params) {
		PreparedStatement pstmt = bpstmt.getPstmt();

		return super.BufferedAdd(pstmt, params);
	}

	/**
	 * 执行缓冲内容
	 * @return 返回每天语句影响的记录数
	 */
	public int[] executeBuffered() {
		//bpstmt不存在会直接报空指针错误
		PreparedStatement pstmt = bpstmt.getPstmt();

		return super.executeBuffered(pstmt);
	}

	/**
	 * 关闭批处理缓冲区
	 */
	public void bufferedClose() {
		super.bufferedClose(bpstmt.getPstmt());

		if(bpstmt.getConn() != null) closeConn(bpstmt.getConn());

		bpstmt = null;
	}
}

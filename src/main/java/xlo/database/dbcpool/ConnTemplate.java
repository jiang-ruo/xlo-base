package xlo.database.dbcpool;

import java.sql.Connection;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title 模板，用于初始化借出的连接和重置归还的连接
 */

public interface ConnTemplate {

	/**
	 * 初始化借出的连接
	 * @param conn
	 */
	public void init(Connection conn);

	/**
	 * 重置归还的连接
	 * @param conn
	 */
	public void reset(Connection conn);

}

package xlo.database.dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author XiaoLOrange
 * @time 2020.10.09
 * @title 创建数据库连接
 */

public class ConnectDb {

	/**
	 * sqlite驱动
	 */
	public static String Sqlite = "org.sqlite.JDBC";
	/**
	 * oracle驱动
	 */
	public static String Oracle = "oracle.jdbc.OracleDriver";

	/**
	 * oracle数据库连接
	 */
	public static String Oracle_link = "jdbc:oracle:thin:@localhost:1521:orcl";

	public Connection getConn(String driver, String link, String user, String pwd){
		return Conn(driver, link, user, pwd);
	}

	public Connection getConn_Sqlite(String link){
		return Conn(Sqlite, link, null, null);
	}

	public Connection getConn_Oracle(String user, String pwd){
		return Conn(Oracle, Oracle_link, user, pwd);
	}

	public Connection getConn_Oracle(String link, String user, String pwd){
		return Conn(Oracle, link, user, pwd);
	}

	/**
	 *
	 * @param driver 数据库类型
	 * @param link 数据库的连接
	 * @param user 登陆数据库的用户名
	 * @param pwd 密码
	 * @return
	 */
	private Connection Conn(String driver, String link, String user, String pwd){
		//检查驱动
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("未导入jar包");
			return null;
		}

		//连接/创建数据库。
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(link, user, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("连接数据库" + link + "失败");
			e.printStackTrace();
		}

		return conn;
	}

}

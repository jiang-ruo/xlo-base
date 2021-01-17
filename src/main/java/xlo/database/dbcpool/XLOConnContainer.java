package xlo.database.dbcpool;

import xlo.database.dbc.ConnectDb;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author XiaoLOrange
 * @time 2020.11.28
 * @title
 */

class XLOConnContainer {

	//记录连接被使用的次数
	private int used = 0;

	//连接池
	private ConnPool pool;
	//连接
	private Connection conn;
	//该连接容器在连接池中的位置，下标
	private int pos = -1;

	//
	private String driver, link, user, pwd;
	//借出连接时对连接进行初始化，归还连接时重置连接
	private ConnTemplate template;

	XLOConnContainer(int pos, ConnPool pool, ConnTemplate template, String driver, String link, String user, String pwd) {
		this.pos = pos;
		this.pool = pool;
		this.template = template;
		//
		this.driver = driver;
		this.link = link;
		this.user = user;
		this.pwd = pwd;
		init();
	}

	/**
	 * 重置Conn容器
	 */
	void init(){
		used = 0;
		close();
		this.conn = new ConnectDb().getConn(driver, link, user, pwd);
	}

	/**
	 * 借出连接
	 */
	Connection getConn(){
		used++;
		if(template != null) template.init(conn);
		return new XLOConnection(conn, this);
	}

	/**
	 * 归还连接
	 * 避免用户在template方法中直接使用conn.close()方法造成真连接被关闭，
	 * 使用close()方法会造成无限递归直到报错
	 */
	void back(XLOConnection conn){
		pool.back(pos);
		if(template != null) template.reset(conn);

		//检查连接是否到达最大使用限制
		if(used < pool.getMaxCase()) return;
		init();
	}

	void close(){
		try {
			if(conn != null) conn.close();
		} catch (SQLException throwables) {
//			throwables.printStackTrace();
		}
	}
}

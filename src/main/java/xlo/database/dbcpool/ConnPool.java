package xlo.database.dbcpool;

import xlo.boolgroup.BoolGroup;
import xlo.database.exception.ConnPoolIsRunningException;
import xlo.database.exception.ConnPoolNotReadyException;
import xlo.util.ValueVerifyUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author XiaoLOrange
 * @time 2020.11.28
 * @title
 */

public class ConnPool {

	/**
	 * 用户获取连接时的等待时间，
	 */
	private final static int WAIT = 3 * 1000;

	/**
	 * 连接池状态
	 * 未知状态，不知道是处于NEW状态还是READY状态
	 */
	public final static int UNKNOW = 0;
	/**
	 * 连接池状态
	 * 新建连接池，连接池还没设置账号密码
	 */
	public final static int NEW = 100;
	/**
	 * 连接池状态
	 * 连接池准备完成，设置账号密码
	 */
	public final static int READY = 200;
	/**
	 * 连接池状态
	 * 连接池正在运行
	 */
	public final static int RUN = 300;
	/**
	 * 连接池状态
	 * 连接池生命周期结束
	 */
	public final static int END = 190;

	//连接空闲，没有被借出
	final static boolean CONN_FREE = true;
	//连接被借出
	final static boolean CONN_USED = false;


	//连接池初始连接数
	private int initialCapacity = 10;
	//连接池最大连接数
	private int maxCase = 100;
	//当前应当具有的连接数
	private int casese = initialCapacity;
	//连接最大使用次数，单个连接最大借出100次后关闭
	private int maxUsed = 100;
	/**
	 * 当前连接数量的状态
	 * 当前连接数=当前应答具有的连接数，不做任何操作
	 */
	final static int NUMS_EQUAL = 0;
	/**
	 * 当前连接数量的状态
	 * 当前连接数<当前应答具有的连接数，应当创建连接
	 */
	final static int NUMS_LESS = 1;
	/**
	 * 当前连接数量的状态
	 * 当前连接数>当前应答具有的连接数，应当关闭连接
	 */
	final static int NUMS_MORE = 2;

	/**
	 * 连接池
	 */
	private ArrayList<XLOConnContainer> pool = null;
	/**
	 * 连接池中连接的状态, 借出，空闲，默认状态为空闲true
	 */
	private BoolGroup connStates= null;
	/**
	 * 连接池管理线程
	 */
	private CPManager manager = null;

	//连接池状态
	private int state = UNKNOW;

	//连接的驱动，连接，账号密码
	private String driver, link, user, pwd;

	//借出连接时对连接进行初始化，归还连接时重置连接
	private ConnTemplate template;

	public ConnPool(){
		this.state = NEW;
	}

	public ConnPool(int initialCapacity){
		setInitialCapacity(initialCapacity);
		this.state = NEW;
	}

	public ConnPool(String driver, String link, String user, String pwd){
		setConnParams(driver, link, user, pwd);
	}

	public ConnPool(int initialCapacity, String driver, String link, String user, String pwd) {
		setInitialCapacity(initialCapacity);
		setConnParams(driver, link, user, pwd);
	}

	/**
	 * 设置连接池参数，
	 * 只有连接池状态不是run时才能设置
	 * @param driver
	 * @param link
	 * @param user
	 * @param pwd
	 */
	public void setConnParams(String driver, String link, String user, String pwd){
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.driver = driver;
		this.link = link;
		this.user = user;
		this.pwd = pwd;
		//将状态设为ready
		this.state = READY;
	}

	/**
	 * 连接池的初始连接数
	 * 只有连接池状态不是run时才能设置
	 * @param initialCapacity
	 */
	public void setInitialCapacity(int initialCapacity) {
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.initialCapacity = initialCapacity;
		this.casese = this.initialCapacity;
		//如果初始连接数量大于最大连接，则将最大连接数设为初始连接数
		if(this.initialCapacity > this.maxCase) this.maxCase = this.initialCapacity;
	}

	/**
	 * 设置连接池的最大连接数量，随时可更改
	 * 目前采用不可在运行时修改最大连接数的连接池
	 * @param maxCase
	 */
	public void setMaxCase(int maxCase) {
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.maxCase = maxCase;
		if(maxCase < this.initialCapacity) setInitialCapacity(maxCase);
	}

	/**
	 * 设置每个连接最大使用次数
	 * @param maxUsed
	 */
	public void setMaxUsed(int maxUsed) {
		this.maxUsed = maxUsed;
		/**
		 * 可以修改，唤醒管理线程关闭所有超过最大使用次数的连接
		 */
	}


	/**
	 * 开启连接池
	 */
	public void start(){
		if(!(this.state == READY || this.state == END)) throw new ConnPoolNotReadyException();

		this.pool = new ArrayList<>(initialCapacity);
		this.manager = new CPManager(this);
		new Thread(this.manager).start();
		this.connStates = new BoolGroup();
		this.connStates.setDefaultValue(true);

		this.state = RUN;
	}

	/**
	 * 检查连接数量是否正常，仅管理线程可以调用该方法
	 * @return
	 */
	int verifyConnNums(){
		int rs = NUMS_EQUAL;

		if(this.pool.size() < this.casese) rs = NUMS_LESS;
		if(this.pool.size() > this.casese) rs = NUMS_MORE;

		return rs;
	}

	/**
	 * 创建一个新的连接
	 */
	synchronized void create(){
		int pos = this.pool.size();
		this.pool.add(new XLOConnContainer(pos, this, template, driver, link, user, pwd));
		connStates.setSize(this.pool.size());

		//唤醒正在等待新连接的线程
		synchronized (this){
			this.notify();
		}
	}

	/**
	 * 释放队尾的一个连接，不释放正在使用的连接
	 */
	synchronized void freeConn(){
		int pos = pool.size() - 1;
		XLOConnContainer c = pool.get(pos);
		if(connStates.get(pos) == CONN_USED){
			//队尾的连接正在使用，获取一个空闲连接并交换位置
			int freepos = connStates.get(CONN_FREE);
			connStates.set(freepos, CONN_USED);
			//
			pool.set(pos, pool.get(freepos));
			pool.set(freepos, c);
		}

		c.close();
		pool.remove(pos);
		connStates.setSize(this.pool.size());
	}

	/**
	 * 用户借用连接
	 */
	public synchronized Connection getConn (){
		//获取下一个空闲的连接
		int pos = connStates.next(CONN_FREE);
		if(pos == -1){
			//当前没有连接
			//新增连接
			extend();
			synchronized (this){
				try {
					this.wait(WAIT);
				} catch (InterruptedException e) {
//					e.printStackTrace();
				}
			}

			pos = connStates.next(CONN_FREE);
		}

		if(pos == -1) throw new InstantiationError();

		connStates.reverse(pos);
		return this.pool.get(pos).getConn();
	}

	/**
	 * 扩大连接数量
	 */
	private void extend(){
		//正在新增连接当中
		if(pool.size() < casese) return;

		if(casese < maxCase){
			//可以新增连接

			//判断新增的连接的数量
			int newcase = (casese >> 1) + casese;
			if(newcase > maxCase) newcase = maxCase;

			this.casese = newcase;
			callManager(manager.CREATE);
		}
	}

	/**
	 * 缩小连接数量
	 */
	void borrow(){
		if(casese == initialCapacity) return;

		int newcase = casese * 2 / 3;
		if(newcase < initialCapacity) newcase = initialCapacity;

		this.casese = newcase;
		callManager(manager.CLOSE);
	}

	/**
	 * 满足条件，唤醒管理线程
	 */
	private void callManager(int opera){
		manager.addOpera(opera);
		synchronized (manager){
			manager.notifyAll();
		}
	}

	/**
	 * 用户归还连接，最后都会跳回到XLOContainer归还连接，并且可以通过其它连接池的back方法归还连接
	 * @param conn
	 */
	public void back(Connection conn){
		try {
			conn.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	/**
	 * XLOContainer归还连接
	 */
	synchronized void back(int pos){
		connStates.reverse(pos);
		synchronized (this){
			this.notify();
		}
	}

	/**
	 * 获取所有正在使用的连接数
	 * @return
	 */
	int getUsed(){
		int rs = 0;
		for(int i = 0; i < pool.size(); i++){
			if(connStates.get(i) == CONN_USED) rs++;
		}
		return rs;
	}

	/**
	 * 关闭连接池。
	 */
	public void close(){
		callManager(manager.EXIT);
	}

	void exit(){
		for (int i = 0; i < pool.size(); i++){
			pool.get(i).close();
		}
		this.pool = null;
		this.connStates = null;
		this.manager = null;
		this.state = END;
	}

	public int getState() {
		return state;
	}

	public int size(){
		return pool.size();
	}

	public int getMaxCase() {
		return maxCase;
	}

	public void setDriver(String driver) {
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.driver = driver;
		isReady();
	}

	public void setLink(String link) {
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.link = link;
		isReady();
	}

	public void setUser(String user) {
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.user = user;
		isReady();
	}

	public void setPwd(String pwd) {
		if(this.state >= RUN) throw new ConnPoolIsRunningException();
		this.pwd = pwd;
		isReady();
	}

	public void setTemplate(ConnTemplate template) {
		this.template = template;
	}

	/**
	 * 将连接池状态设为ready
	 */
	private void isReady(){
		if(this.state > NEW) return;
		if(ValueVerifyUtil.notNull(driver, link)) this.state = READY;
	}
}

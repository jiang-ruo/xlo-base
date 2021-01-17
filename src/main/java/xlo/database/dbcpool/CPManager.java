package xlo.database.dbcpool;

import java.util.LinkedList;

/**
 * @author XiaoLOrange
 * @time 2020.11.29
 * @title 连接池管理线程
 */

class CPManager implements Runnable{

	private ConnPool pool;

	//如果线程没被唤醒，则自己每10s运行一次
	private final static long wait = 10 * 1000;

	/**
	 * 用于统计连接使用率
	 */
	//记录的次数
	private final static int NUMS = 10;
	//使用率不到70则释放多余的连接
	private final static int RATE = 70;
	//上次记录使用率的时间
	private long time = System.currentTimeMillis();
	//每次记录间的时间间隔，每分钟记录一次
	private final static long FRAG = 1000 * 60;
	//用于计数的数组，计数满后就新建一个
	private int[] count = new int[NUMS];
	//记录计数数组的长度
	private int countNums = 0;


	//用户储存操作的队列
	private LinkedList<Integer> operator = new LinkedList<>();

	/**
	 * 操作类型
	 * 创建连接
	 */
	final static int CREATE = 1;
	/**
	 * 操作类型
	 * 连接过多，释放连接
	 */
	final static int CLOSE = 2;
	/**
	 * 操作类型
	 * 关闭连接， 结束线程
	 */
	final static int EXIT = 3;

	CPManager(ConnPool pool) {
		this.pool = pool;
		//创建该线程时向操作队列放入创建连接操作
		addOpera(CREATE);
	}

	/**
	 * 向管理线程中添加操作
	 */
	synchronized void addOpera(int opera){
		//检查管理线程中是否存在该操作，存在则直接跳过。
		if(hasOpera(opera)) return;
		this.operator.add(opera);
	}

	private boolean hasOpera(int opera){
		for (int i = 0; i < this.operator.size(); i++){
			if(this.operator.get(i) == opera) return true;
		}
		return false;
	}

	@Override
	public void run() {
		int opera = 0;
		while (true){
			//执行必须的操作，统计连接使用率
			census();

			while (operator.size() > 0){
				opera = operator.pop();
				switch (opera){
					case CREATE:
						create();
						break;
					case CLOSE:
						freeConn();
						break;
					case EXIT:
						close();
						return;
				}
			}

			//执行必须的操作
			synchronized (this){
				try {
					this.wait(wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 循环创建新连接
	 */
	private void create(){
		while (pool.verifyConnNums() == pool.NUMS_LESS){
			pool.create();
		}
	}

	/**
	 * 释放空闲连接
	 */
	private void freeConn(){
		while (pool.verifyConnNums() == pool.NUMS_MORE){
			pool.freeConn();
		}
	}

	/**
	 * 统计连接使用率
	 */
	private void census(){
		if(countNums == NUMS){
			//计算总连接使用率
			int rate = 0;
			for (int i = 0; i < NUMS; i++){
				rate += count[i];
			}
			rate = rate / NUMS;
			if(rate < RATE) pool.borrow();

			countNums = 0;

		}else{
			//不到正常计算间隔，直接跳过
			if(System.currentTimeMillis() - time < FRAG) return;
			//计算单位时间连接使用率
			int used = pool.getUsed();
			int nums = pool.size();
			if(nums == 0) return;
			count[countNums] = (used * 100) / nums;
			countNums++;
		}
	}

	void close(){
		pool.exit();
	}
}

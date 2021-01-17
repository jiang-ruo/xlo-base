package xlo.infinite;

import java.lang.reflect.Field;

public class Infinite {

	/**
	 * 在内存中声明/申请？一块区域放clazz类
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T allocateInstance(Class<T> clazz) throws Exception {
		return xlo.infinite.methods.allocateInstance.create().newInstance(clazz);
	}
	
	/**
	 * 获取属性field在类中的偏移位置
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public long objectFieldOffset(Field field) throws Exception {
		return xlo.infinite.methods.objectFieldOffset.create().getOffset(field);
	}
	
	/**
	 * 从obj对象偏移位置为offset的位置获取一个int值
	 * @param obj
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public int getInt(Object obj, long offset) throws Exception {
		return xlo.infinite.methods.getInt.create().readMenory(obj, offset);
	}
	
	/**
	 * 在obj对象偏移位置为offset的位置放入一个int值
	 * @param obj
	 * @param offset
	 * @param addr
	 * @throws Exception
	 */
	public void putInt(Object obj, long offset, int addr) throws Exception {
		xlo.infinite.methods.putInt.create().putMemory(obj, offset, addr);
	}
	
	/**
	 * 
	 * @param address
	 * @throws Exception
	 */
	public void freeMemory(long address) throws Exception {
		xlo.infinite.methods.freeMemory.create().free(address);
	}
	
}

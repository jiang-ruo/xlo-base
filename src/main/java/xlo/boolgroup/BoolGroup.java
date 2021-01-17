package xlo.boolgroup;

import xlo.util.math.NumUtil;

import java.util.Arrays;

/**
 * @author XiaoLOrange
 * @time 2020.11.28 算法中有许多地方可以优化
 * @title bool值组，用于标记大量的boolean值状态
 * 		  为便于操作，采用和数组下标一样的计数方式
 */

public class BoolGroup {

	private final static byte FREE = 0;
	private final static byte FULL = -1;
	/**
	 * 1	: 0, 0, 0, 0, 0, 0, 0, 1 :	对应求余 = 0
	 * 2	: 0, 0, 0, 0, 0, 0, 1, 0 :	对应求余 = 1
	 * 4	: 0, 0, 0, 0, 0, 1, 0, 0 :	对应求余 = 2
	 * 8	: 0, 0, 0, 0, 1, 0, 0, 0 :	对应求余 = 3
	 * 16	: 0, 0, 0, 1, 0, 0, 0, 0 :	对应求余 = 4
	 * 32	: 0, 0, 1, 0, 0, 0, 0, 0 :	对应求余 = 5
	 * 64	: 0, 1, 0, 0, 0, 0, 0, 0 :	对应求余 = 6
	 * -128	: 1, 0, 0, 0, 0, 0, 0, 0 :	对应求余 = 7
	 */
	private final static byte[] VALUES = {1, 2, 4, 8, 16, 32, 64, -128};
	//
	private final static int UNIT = 8;

	//需要表记的boolean数量
	private int size;

	/**
	 * 储存数组的地方
	 * 数组长度称为群组: group
	 */
	private byte[] list = new byte[0];

	/*
	 * 设置默认状态，默认0为false, 1为true
	 * 因为一个新的byte[]默认值是0，因此将默认值设为与0对应，
	 * 当defaultValue为false时，0为false,
	 * 当defaultValue为true时，0为true
	 */
	private boolean defaultValue = false;

	/**
	 * 指针，指向当前元素
	 */
	private int pointer = 0;

	public BoolGroup(){}

	public BoolGroup(int size){
		setSize(size);
	}

	/**
	 * 修改bool值组的长度，长度超过范围的抛弃，
	 * @param size
	 */
	public void setSize(int size) {
		if(size > this.size){
			//扩大现有容量，判断lenth是否大于现有数组length
			int length = NumUtil.toInt(Math.ceil((double)size / UNIT));
			if(length > this.list.length){
				this.list = Arrays.copyOf(this.list, length);
			}
		}else{
			//缩小数组，将超出部分全部设为0
			setDefaultRange(size, this.size);
		}
		this.size = size;
	}

	/**
	 * 设置默认值为true还是false
	 * @param value
	 */
	public void setDefaultValue(boolean value){
		this.defaultValue = value;
	}

	/**
	 * 获取bool值组的长度
	 * @return
	 */
	public int size(){
		return this.size;
	}

	/**
	 * 给指定位置赋值
	 * @param offset
	 * @param value
	 */
	public void set(int offset, boolean value){
		if(offset >= size) throw new ArrayIndexOutOfBoundsException();

		//计算要改变的值在list中的位置
		int group = offset / UNIT;
		offset = offset % UNIT;

		//对应offset标准的byte值
		byte standard = VALUES[offset];
		//list中对应group的byte值
		byte listvalue = this.list[group];
		//获取listvalue中对应offset的值。
		byte lovalue = (byte) (listvalue & standard);

		if(value == defaultValue){
			//将指定位置的值设为0

			//判断指定位置的值是否为0;
			if(lovalue == 0) return;

			//不是0
			listvalue = (byte) (listvalue ^ standard);
		}else{
			//将指定位置的值设为1

			//判断指定位置的值是否为1;
			if(lovalue != 0) return;

			//不是1
			listvalue = (byte) (listvalue | standard);
		}

		this.list[group] = listvalue;
	}

	/**
	 * 将指定范围内的bool值设为value
	 * @param offsetBegin
	 * @param offsetEnd
	 * @param value
	 */
	public void set(int offsetBegin, int offsetEnd, boolean value){
		for (int i = offsetBegin; i <= offsetEnd; i++){
			set(i, value);
		}
	}

	/**
	 * 将指定位置设为默认值0
	 * @param offsets
	 */
	public void setDefault(int... offsets){
		for (int i = 0; i < offsets.length; i++){
			set(offsets[i], this.defaultValue);
		}
	}

	/**
	 * 批量设置为true
	 * @param offsets
	 */
	public void setTrue(int... offsets){
		for (int i = 0; i < offsets.length; i++){
			set(offsets[i], true);
		}
	}

	/**
	 * 批量设置为true
	 * @param offsets
	 */
	public void setFalse(int... offsets){
		for (int i = 0; i < offsets.length; i++){
			set(offsets[i], false);
		}
	}

	/**
	 * 将指定范围的bool值设为默认值0
	 * @param offsetBegin
	 * @param offsetEnd
	 */
	public void setDefaultRange(int offsetBegin, int offsetEnd){
		set(offsetBegin, offsetEnd, this.defaultValue);
	}

	/**
	 * 将指定范围的bool值设为true
	 * @param offsetBegin
	 * @param offsetEnd
	 */
	public void setTrueRange(int offsetBegin, int offsetEnd){
		set(offsetBegin, offsetEnd, true);
	}

	/**
	 * 将指定范围的bool值设为false
	 * @param offsetBegin
	 * @param offsetEnd
	 */
	public void setFalseRange(int offsetBegin, int offsetEnd){
		set(offsetBegin, offsetEnd, false);
	}

	/**
	 * 获取指定位置的boolean值
	 * @param offset
	 * @return
	 */
	public boolean get(int offset){
		if(offset >= size) throw new ArrayIndexOutOfBoundsException();

		//计算要改变的值在list中的位置
		int group = offset / UNIT;
		offset = offset % UNIT;

		//对应offset标准的byte值
		byte standard = VALUES[offset];
		//list中对应group的byte值
		byte listvalue = this.list[group];
		//获取listvalue中对应offset的值。
		byte lovalue = (byte) (listvalue & standard);

		//判断指定位置的值是否为0
		return lovalue == 0 ? defaultValue : !defaultValue;
	}

	/**
	 * 将指定位置的bool值取反
	 * @param offset
	 */
	public void reverse(int offset){
		boolean value = get(offset);
		set(offset, !value);
	}

	/**
	 * 将指定范围的bool值取反
	 * @param offsetBegin
	 * @param offsetEnd
	 */
	public void reverse(int offsetBegin, int offsetEnd){
		for(int i = offsetBegin; i <= offsetEnd; i++){
			reverse(i);
		}
	}

	/**
	 * 获取第一个值为value的元素
	 * @param value
	 * @return
	 */
	public int get(boolean value){
		//标记是否找到
		for (int i = 0; i < this.size; i++){
			if(get(i) == value) return i;
		}
		//没找到返回-1
		return -1;
	}

	/**
	 * 返回下一个值为value的元素的位置
	 * @param value
	 * @return
	 */
	public int next(boolean value){
		if(this.pointer >= this.size) this.pointer = 0;

		for (int i = this.pointer; i < this.size; i++){
			if(get(i) == value){
				this.pointer = i + 1;
				return i;
			}
		}

		//在当前位置之后的位置找不到，查找当前元素之前的位置
		int pos = get(value);
		if(pos != -1) this.pointer = pos + 1;
		return pos;
	}

}
